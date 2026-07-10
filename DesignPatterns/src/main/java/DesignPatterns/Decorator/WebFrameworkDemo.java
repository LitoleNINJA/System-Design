package DesignPatterns.Decorator;

import java.util.Map;

/**
 * Test driver / demo for the HTTP Middleware Chain exercise.
 *
 * --------------------------------------------------------------------------
 * THIS FILE IS THE SPEC. Implement the supporting classes in this same
 * folder/package so this file compiles and runs:
 *
 *   1. HttpRequest.java       — record(method, path, headers, body)
 *   2. HttpResponse.java      — immutable value object with:
 *                                 - ctor(int statusCode, Map<String,String> headers, String body)
 *                                 - getStatusCode(), getHeaders(), getBody()
 *                                 - static ok(body), unauthorized()
 *                                 - withHeader(k, v) → returns a NEW response (immutability)
 *   3. HttpHandler.java       — interface: HttpResponse handle(HttpRequest req)
 *   4. UserListHandler.java   — base handler, returns 200 with a fixed JSON body
 *   5. LoggingMiddleware.java — ctor(inner)
 *   6. AuthMiddleware.java    — ctor(inner, expectedToken)
 *   7. CachingMiddleware.java — ctor(inner)
 *
 * Until all seven exist you'll get "cannot find symbol" errors — each error
 * tells you what to build next.
 * --------------------------------------------------------------------------
 *
 * The defining demonstration of Decorator: every layer in the stack
 * implements the SAME interface as the thing it wraps. The same
 * `HttpHandler` reference can point to a bare handler or a 3-deep stack of
 * middleware — the call site doesn't know or care.
 *
 * Stack assembled:
 *
 *   Caching ◀── outermost (sees request first, response last)
 *     Logging
 *       Auth
 *         UserListHandler ◀── innermost (the actual business logic)
 *
 * Why this order:
 *   - Cache OUTERMOST: a cache hit short-circuits everything below — we
 *     don't bother logging or authenticating because we're returning a
 *     stored response immediately. Big perf win.
 *   - Logging OUTSIDE Auth: so 401 responses still get logged. If Logging
 *     were inside Auth, Auth's short-circuit would never reach Logging and
 *     401s would silently disappear from logs — bad observability.
 *
 * Read the wrapping outside-in. A request flows IN through Caching first,
 * then Logging (always passes through), then Auth (which may short-circuit
 * with 401), then the handler. A response flows OUT in reverse order.
 */
public class WebFrameworkDemo {

    public static void main(String[] args) {
        HttpHandler handler = new UserListHandler();

        // Compose the stack. Innermost is the base handler; we wrap outward.
        // Logging sits OUTSIDE Auth so 401 responses still get logged.
        HttpHandler stack =
            new CachingMiddleware(
                new LoggingMiddleware(
                    new AuthMiddleware(handler, "secret-123")));

        System.out.println("=== Stack: Cache -> Logging -> Auth(token=secret-123) -> UserListHandler ===");

        System.out.println("\n--- Req 1: GET /api/users (no auth header) ---");
        send(stack, new HttpRequest("GET", "/api/users", Map.of(), ""));

        System.out.println("\n--- Req 2: GET /api/users (Authorization: Bearer secret-123) ---");
        send(stack, new HttpRequest("GET", "/api/users",
                Map.of("Authorization", "Bearer secret-123"), ""));

        System.out.println("\n--- Req 3: GET /api/users (same auth, should hit cache) ---");
        send(stack, new HttpRequest("GET", "/api/users",
                Map.of("Authorization", "Bearer secret-123"), ""));

        System.out.println("\n--- Req 4: GET /api/users?page=2 (different path, bypasses cache) ---");
        send(stack, new HttpRequest("GET", "/api/users?page=2",
                Map.of("Authorization", "Bearer secret-123"), ""));
    }

    private static void send(HttpHandler stack, HttpRequest req) {
        HttpResponse resp = stack.handle(req);
        System.out.printf("Response: %d headers=%s body=%s%n",
                resp.getStatus(), resp.getHeaders(), resp.getBody());
    }
}
