# Exercise — HTTP Middleware Chain

> Decorator pattern · LLD practice
> Real-world version of Decorator. Every web framework you've used (Express, Spring filters, Django middleware) is *literally* this pattern. Doing it well sets you apart in backend interviews — most candidates have only seen the cliché coffee/pizza version.

---

## Problem Statement

You are building the request-handling layer of a tiny web framework. Every incoming HTTP request must pass through a stack of **middleware** — code that does cross-cutting work (logging, authentication, caching) — before reaching the actual **handler** that produces the response.

Each middleware can:

- **Pre-process** the request (e.g., parse a header, check a token)
- **Short-circuit** (e.g., return `401` immediately without calling the inner handler)
- **Post-process** the response (e.g., add a header, log timing)
- **Pass through** (do nothing, just delegate)

Use the **Decorator** pattern: every middleware is itself an `HttpHandler` that wraps another `HttpHandler`. Composition happens at call site, not in the middlewares.

---

## Requirements

1. Define value objects:
   - `HttpRequest` — immutable: `method`, `path`, `headers` (`Map<String,String>`), `body`.
   - `HttpResponse` — immutable: `statusCode`, `headers`, `body`. Static factories `ok(body)` and `unauthorized()`. A `withHeader(k, v)` method that returns a **new** response with the extra header (don't mutate).

2. Define `HttpHandler`:
   ```java
   interface HttpHandler { HttpResponse handle(HttpRequest request); }
   ```
   Implemented by **both** the base handler and every middleware. That uniformity is the whole point of Decorator — wrappers and wrapped share one type, so they're freely composable.

3. Implement one **base handler** (innermost layer): `UserListHandler` returns `200` with body `[{"id":1,"name":"Alice"},{"id":2,"name":"Bob"}]` for `/api/users`.

4. Implement three **middleware decorators**, each takes an inner `HttpHandler` in its constructor and itself implements `HttpHandler`:
   - **`LoggingMiddleware`** — prints `[LOG] -> METHOD PATH` before delegating, and `[LOG] <- STATUS in Xms` after. Always passes through. *(Teaches: pre/post hooks, pass-through.)*
   - **`AuthMiddleware(inner, expectedToken)`** — checks `Authorization: Bearer <token>` header. If missing or wrong, returns `401` without calling inner. *(Teaches: conditional short-circuit.)*
   - **`CachingMiddleware`** — keys by `METHOD + PATH`. Cache hit: returns the cached response with `X-Cache: HIT` header. Cache miss: delegates, stores, returns with `X-Cache: MISS`. *(Teaches: state, post-processing, immutability of response.)*

5. Adding a new middleware tomorrow (e.g., `CompressionMiddleware`, `CorsMiddleware`) requires zero edits to existing middleware, handlers, or value objects.

---

## Class Hints

```java
record HttpRequest (String method, String path, Map<String,String> headers, String body) { }

class HttpResponse {                                     // immutable
    int statusCode;
    Map<String,String> headers;
    String body;
    static HttpResponse ok(String body);
    static HttpResponse unauthorized();
    HttpResponse withHeader(String key, String value);   // returns NEW response
}

interface HttpHandler { HttpResponse handle(HttpRequest req); }

class UserListHandler        implements HttpHandler { ... }            // the Component (base)
class LoggingMiddleware      implements HttpHandler { ctor: (inner) }  // Decorator
class AuthMiddleware         implements HttpHandler { ctor: (inner, expectedToken) }
class CachingMiddleware      implements HttpHandler { ctor: (inner) }

class WebFrameworkDemo { public static void main(String[] args) { ... } }
```

> 💡 **The structural fingerprint of Decorator:** every wrapper holds a field of the same interface it implements.
> ```java
> class AuthMiddleware implements HttpHandler {
>     private final HttpHandler inner;     // ← same type as what it implements
> }
> ```
> If those two types differ, you don't have Decorator.

---

## Expected Output

Stack assembled outside-in: `Caching → Logging → Auth → UserListHandler`. (Logging is **outside** Auth so 401 responses still get logged — see the "stack-order matters" discussion below.)

```
=== Stack: Cache → Logging → Auth(token=secret-123) → UserListHandler ===

--- Req 1: GET /api/users (no auth header) ---
[LOG] -> GET /api/users
[LOG] <- 401 in 0ms
Response: 401 headers={X-Cache=MISS} body=Unauthorized

--- Req 2: GET /api/users (Authorization: Bearer secret-123) ---
[LOG] -> GET /api/users
[LOG] <- 200 in 1ms
Response: 200 headers={X-Cache=MISS} body=[{"id":1,"name":"Alice"},{"id":2,"name":"Bob"}]

--- Req 3: GET /api/users (same auth, should hit cache) ---
Response: 200 headers={X-Cache=HIT} body=[{"id":1,"name":"Alice"},{"id":2,"name":"Bob"}]

--- Req 4: GET /api/users?page=2 (different path, bypasses cache) ---
[LOG] -> GET /api/users?page=2
[LOG] <- 200 in 0ms
Response: 200 headers={X-Cache=MISS} body=[{"id":1,"name":"Alice"},{"id":2,"name":"Bob"}]
```

**Three things to notice in this output**:

1. **Req 1 — auth short-circuit.** Auth returned 401 without ever calling the handler. You *still* see `[LOG]` lines because `Logging` is **outside** `Auth` in the stack: a request hits Logging first, Logging delegates to Auth, Auth short-circuits with 401, the 401 flows back through Logging on its way out — and Logging post-processes it (printing `<- 401`). **Stack order = nesting depth.** If we'd put Logging *inside* Auth instead, Auth's short-circuit would skip Logging entirely and 401s would never appear in logs. That's exactly the kind of design choice interviewers probe.

2. **Req 3 — cache hit short-circuit.** Cache returned its stored response *immediately*. Nothing else fired — no logging, no auth check. Performance win. *But*: a cached response served to an unauthenticated user is a **security bug**. Be ready to discuss this trade-off.

3. **Req 4 — different cache key.** The path includes `?page=2`, so the cache key differs and the request flows all the way through.

---

## What the Interviewer is Looking For

- **Every wrapper holds the wrapped object as `HttpHandler`** (the interface) — never as a concrete class. That's the polymorphic spine.
- **Composition lives in client code**, not in the middlewares. A middleware does NOT know what's inside it (other than "another HttpHandler").
- **Open/Closed:** adding `CorsMiddleware` is one new class — zero edits anywhere else.
- **Stack order is a deliberate design choice.** Be ready to discuss:
  - Cache outermost = performance win, but cached unauth requests are a security risk. Solution: put auth outside cache, or scope cache by user.
  - Logging outermost = logs every request including 401. Innermost = only logs ones that reached the handler.
- **Immutability of `HttpResponse.withHeader`** — don't mutate the existing response, return a new one. Treats responses as values, makes caching safe.

---

## Decorator vs Things It Gets Confused With

Be ready for any of these probes:

| vs | Distinction |
|---|---|
| **Decorator vs Inheritance** | If `LoggedAuthHandler extends UserListHandler`, you must subclass for every combination — exponential explosion. With Decorator, you compose at runtime: `Logging(Auth(handler))` vs `Auth(Logging(handler))` are different stacks of the same parts. *Composition over inheritance, made concrete.* |
| **Decorator vs Chain of Responsibility** | CoR: each handler chooses to handle or pass — typically only ONE handler does the work. Decorator: each wrapper *always* runs (before/after) and *always* delegates (or short-circuits with full result). Middleware sits at the boundary; "Decorator" is the right answer for this pipeline. |
| **Decorator vs Proxy** | Both wrap the same interface. Proxy controls *access* (lazy-load, remote, security). Decorator adds *behavior*. The line is fuzzy — `CachingMiddleware` could be called a Proxy too. Acceptable to mention. |
| **Decorator vs Adapter** | Adapter changes the *interface* (square peg → round hole). Decorator preserves the interface and adds behavior. |

---

## How to Attempt This Cold

Suggested order — write small, run often:

1. `HttpRequest.java` — record (one-liner in Java 21).
2. `HttpResponse.java` — class with constructor, getters, static factories `ok` / `unauthorized`, and `withHeader` that returns a **new** response.
3. `HttpHandler.java` — interface, one method.
4. `UserListHandler.java` — base concrete handler.
5. ✅ **Run the demo with just the base handler** — verify wiring before adding middleware.
6. `LoggingMiddleware.java` — easiest decorator: no short-circuit, no state.
7. `AuthMiddleware.java` — adds the short-circuit feature.
8. `CachingMiddleware.java` — adds an internal `Map<String, HttpResponse>` and post-processing.

A pitfall to watch:
- **`HttpResponse.withHeader` MUST return a new response.** If you mutate, your cache will get poisoned: subsequent hits will append `X-Cache: HIT` to an already-mutated object. Treat responses as immutable.
- **Cache key is METHOD + PATH** — including the auth header would make every user a unique cache entry, defeating the cache.

---

## Files in this Exercise

| File | Role |
|------|------|
| `HttpRequest.java`           | Value object (record) |
| `HttpResponse.java`          | Value object — immutable, with `withHeader` |
| `HttpHandler.java`           | The Component interface |
| `UserListHandler.java`       | Concrete Component (base, innermost) |
| `LoggingMiddleware.java`     | Decorator — pass-through with timing |
| `AuthMiddleware.java`        | Decorator — short-circuit on 401 |
| `CachingMiddleware.java`     | Decorator — short-circuit on cache hit |
| `WebFrameworkDemo.java`      | Client / `main` — **provided as the test contract** |
