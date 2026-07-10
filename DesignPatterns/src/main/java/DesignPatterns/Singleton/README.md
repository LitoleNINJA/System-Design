# Singleton Pattern — Application Logger

## Problem

Design an application-wide logger that can be used from anywhere in the codebase. Every component — a web controller, a database layer, a background job — should write to the **same** logger instance. If one component changes the log level (e.g., enabling DEBUG during a support call), that change must be immediately visible to all other components.

## Why Singleton here

A logger is the canonical Singleton use case because:

- **Shared state is the point.** Log level, output target, and formatting rules must be consistent across the whole app. Two separate logger instances could produce split, inconsistent output.
- **Expensive to construct.** Real loggers open file handles or network sockets. You want exactly one.
- **Global access is intentional.** Every layer of the app legitimately needs it — this is one of the rare cases where a global is the right design.

## Requirements

1. Only one `SingletonLogger` instance can exist per JVM. Calling `getInstance()` from any thread must return the exact same object (`==` identity, not just `equals`).
2. Supports four log levels: `DEBUG < INFO < WARN < ERROR`. Messages below the current level are silently suppressed.
3. `setLogLevel(level)` changes the level globally — all callers immediately see the new threshold.
4. Each log line is prefixed with a timestamp: `[yyyy-MM-dd HH:mm:ss] [LEVEL] message`.
5. Thread-safe: concurrent calls from multiple threads must not interleave partial log lines.

## Class to implement

```
SingletonLogger
  - getInstance() : SingletonLogger          ← static; always returns the same object
  - setLogLevel(LogLevel level) : void
  - debug(String message) : void
  - info(String message)  : void
  - warn(String message)  : void
  - error(String message) : void

  LogLevel (nested enum)
    DEBUG, INFO, WARN, ERROR
```

## Implementation hint — the Holder pattern

The cleanest Java Singleton avoids `synchronized` on the hot path entirely:

```java
private static class Holder {
    static final SingletonLogger INSTANCE = new SingletonLogger();
}

public static SingletonLogger getInstance() {
    return Holder.INSTANCE;   // JVM guarantees Holder is loaded once, lazily
}
```

The JVM class loader guarantees `Holder` is initialized exactly once, on the first call to `getInstance()`. No `synchronized`, no `volatile` on the instance itself, no double-checked locking.

`setLogLevel` still needs `volatile` (or a lock) because it's a separate write that racing threads could miss.

## Expected output

```
=== Same instance? true ===

=== Default log level: INFO ===
[2026-05-19 10:00:00] [INFO]  App started
[2026-05-19 10:00:00] [WARN]  Low memory warning
[2026-05-19 10:00:00] [ERROR] Disk full

=== Change level to DEBUG via a second reference ===
[2026-05-19 10:00:00] [DEBUG] Now visible — level changed through a different variable
[2026-05-19 10:00:00] [INFO]  Still visible

=== Three threads, one logger ===
[2026-05-19 10:00:00] [INFO] Thread-A — same instance: true
[2026-05-19 10:00:00] [INFO] Thread-B — same instance: true
[2026-05-19 10:00:00] [INFO] Thread-C — same instance: true
```

*(thread lines may appear in any order — that's expected)*

## The three things the demo proves

| Test | What it proves |
|---|---|
| `logger1 == logger2` is `true` | Only one instance exists — identity, not just equality |
| Level set via `logger2` affects `logger1` | They share the same state because they ARE the same object |
| All threads report `same instance: true` | Singleton is thread-safe across concurrent `getInstance()` calls |
