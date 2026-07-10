## 1. Why exceptions exist

Replaces C-style return-code error handling. Problems with return codes:
- Different conventions per function (NULL, 0, -1, errno)
- Sentinel collision (atoi returns 0 on failure, but 0 is also valid)
- Caller burden — every call needs `if (err)` check
- Manual propagation through every layer
- Cleanup code duplicated at every error site

Exceptions give: **separation of detection from handling, automatic propagation, type-safe error objects, structured cleanup.**

## 2. The `throw` mechanism

- Each thread has a call stack of frames (locals, params, return address)
- Normal return: pop one frame, jump to return address
- `throw`: **stack unwinding** — pop frames repeatedly until a matching catch is found
- Each method has an **exception table** in its bytecode: `(from, to, target, type)` mapping bytecode ranges to handlers
- Match is type-aware (`instanceof`-style) — `catch (Exception)` matches `IOException`, etc.

**Why exceptions are "expensive":**
1. Stack walk on unwind — O(depth)
2. **Stack trace capture happens at `new Exception()`** — walks the entire stack to fill `getStackTrace()`. This is the big cost.
3. Defeats JIT branch prediction and inlining

Throw+catch is ~100–1000× slower than a return. Fine for actual errors, **never use for control flow.**

**Gotcha:** Stack unwinding picks the closest **matching** catch, not the closest catch. Non-matching catches in intermediate frames are invisible.

## 3. The `Throwable` hierarchy

```
Throwable
├── Error              ← JVM catastrophes (OOM, StackOverflow). DON'T catch.
└── Exception
    ├── (checked)      ← External world failures (IO, SQL). Compiler enforces handling.
    └── RuntimeException ← Programmer bugs (NPE, IAE). Unchecked.
```

| Category | Cause | Recoverable? | Preventable? | Compiler enforced? |
|---|---|---|---|---|
| `Error` | JVM | No | No | No (don't catch) |
| Checked `Exception` | External | Yes | No | **Yes** |
| `RuntimeException` | Programmer | Maybe | Yes | No |

## 4. Checked vs unchecked — Java's controversial choice

**Original idea:** compiler forces you to acknowledge every recoverable failure → failure modes become part of the type signature.

**Why modern Java leans unchecked:**
1. **Throws clause pollution** — every layer in the call chain repeats `throws X`
2. **Signature breakage propagation** — adding one checked exception at the bottom breaks every ancestor signature
3. **Lambda hostility** — `Runnable.run()` declares no throws; checked exceptions force ugly try/catch wrapping inside lambdas
4. **Catch-and-swallow temptation** — empty catch blocks become path of least resistance

**Modern consensus:**
- Default to `RuntimeException` for new code
- Wrap library checked exceptions into domain unchecked at boundaries (Spring's `DataAccessException` over `SQLException`)
- `InterruptedException` is the one checked exception worth handling carefully (thread coordination signal)

**Custom exception decision rule:** *Will every caller need to do something specific about this failure as part of normal flow?*
- Yes → checked
- No → unchecked

| Failure type | Example | Choice |
|---|---|---|
| Programmer bug | NPE, bad arg | Unchecked |
| Business rule violation | Insufficient funds, out of stock | **Checked** (every caller has a real decision) |
| External failure | DB down, network timeout | Checked, often wrapped to unchecked at boundary |
| Catastrophic | OOM | `Error`, don't catch |

## 5. try / catch / finally / try-with-resources

**`finally` runs when:**
- Try completes normally ✓
- Try throws (caught or uncaught) ✓
- Try contains `return` — **runs before** the return ✓
- **Does NOT run on `System.exit()`** or `kill -9`

**Antipatterns in `finally`:**
- `return` in finally **overrides** the try's return value
- `throw` in finally **swallows** any in-flight exception (silent loss — worst bug)
- Rule: finally should only contain cleanup, never affect control flow

**Pre-Java-7 bug `try-with-resources` fixed:**
```java
try { f.read(); }                 // throws "read failed" (interesting)
finally { f.close(); }            // throws "close failed" (boring)
// Caller sees only "close failed". Real exception lost.
```

**`try-with-resources` (Java 7+):**
```java
try (FileReader f = new FileReader(path)) {
    return f.readLine();
}  // f.close() automatic; primary exception preserved
```
- Auto-close any `AutoCloseable`
- **Exception suppression** — try's exception wins; close's exception attached via `addSuppressed()`, retrievable with `getSuppressed()`
- Multiple resources close in **reverse declaration order**

## 6. Production patterns to know

**Never swallow:**
```java
catch (Exception e) { log.error("failed"); }        // ← stack trace lost
catch (Exception e) { log.error("failed", e); throw e; }  // ← preserved
```
Always log the exception object itself (`e`, not just a message).

**Exception chaining** — preserve the cause:
```java
catch (SQLException e) {
    throw new UserLookupException("could not load user", e);  // ← cause attached
}
```
Visible in stack trace as "Caused by:".

**Catch the narrowest type first:**
```java
catch (FileNotFoundException e) { ... }   // specific first
catch (IOException e) { ... }             // general after
```

**Don't catch `Throwable` or `Error`** unless you're a framework boundary that genuinely needs to log everything before dying.

## Common interview questions

- Difference between `Error` / checked `Exception` / `RuntimeException`?
- What does `throw` do at the JVM level? (stack unwinding + exception table)
- Why are exceptions expensive? (stack trace capture)
- What's wrong with `return` or `throw` in `finally`? (overrides/swallows)
- Why was `try-with-resources` added? (close-in-finally swallowing primary exception)
- When would you create a checked vs unchecked custom exception?
- Why is `IOException` checked but `NullPointerException` unchecked? (external vs programmer)
- What is exception suppression? (`addSuppressed`, `getSuppressed`)
