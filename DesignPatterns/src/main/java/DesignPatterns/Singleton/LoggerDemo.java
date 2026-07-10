package DesignPatterns.Singleton;

/**
 * Demonstrates the three core Singleton properties:
 *   1. Same instance     — getInstance() always returns the identical object
 *   2. Shared state      — a level change through one reference affects all references
 *   3. Thread safety     — concurrent getInstance() calls across threads return the same object
 */
public class LoggerDemo {

    public static void main(String[] args) throws InterruptedException {

        // ── Test 1: Identity ──────────────────────────────────────────────────
        SingletonLogger logger1 = SingletonLogger.getInstance();
        SingletonLogger logger2 = SingletonLogger.getInstance();
        System.out.println("=== Same instance? " + (logger1 == logger2) + " ===");

        // ── Test 2: Basic log-level filtering ─────────────────────────────────
        System.out.println("\n=== Default log level: INFO ===");
        logger1.debug("This is suppressed — DEBUG < INFO");
        logger1.info("App started");
        logger1.warn("Low memory warning");
        logger1.error("Disk full");

        // ── Test 3: Level change is shared across all references ───────────────
        System.out.println("\n=== Change level to DEBUG via logger2 ===");
        logger2.setLogLevel(SingletonLogger.LogLevel.DEBUG);
        logger1.debug("Now visible - level changed through a different variable");
        logger1.info("Still visible");

        // ── Test 4: Multi-threaded — all threads see the same instance ─────────
        System.out.println("\n=== Three threads, one logger ===");
        Runnable task = () -> {
            SingletonLogger threadLocal = SingletonLogger.getInstance();
            threadLocal.info(Thread.currentThread().getName()
                    + " - same instance: " + (threadLocal == logger1));
        };

        Thread t1 = new Thread(task, "Thread-A");
        Thread t2 = new Thread(task, "Thread-B");
        Thread t3 = new Thread(task, "Thread-C");
        t1.start(); t2.start(); t3.start();
        t1.join();  t2.join();  t3.join();

        System.out.println("\n(Thread lines above may appear in any order - that's expected)");
    }
}
