package DesignPatterns.Singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SingletonLogger {
    public enum LogLevel {
        DEBUG, INFO, WARN, ERROR
    }

    private volatile LogLevel logLevel = LogLevel.INFO;  // sensible default
    private final Object lock = new Object();
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private SingletonLogger() {}

    private static class Holder {
        private static final SingletonLogger INSTANCE = new SingletonLogger();
    }

    public static SingletonLogger getInstance() {
        return Holder.INSTANCE;
    }

    public void setLogLevel(LogLevel level) {
        this.logLevel = level;  // volatile write — no sync needed
    }

    private void log(LogLevel level, String message) {
        LogLevel current = logLevel;  // single volatile read
        if (level.ordinal() < current.ordinal()) return;

        String timeStamp = LocalDateTime.now().format(formatter);
        String line = "[" + timeStamp + "] [" + level + "] " + message;

        synchronized (lock) {
            System.out.println(line);
        }
    }

    public void debug(String message) { log(LogLevel.DEBUG, message); }
    public void info(String message)  { log(LogLevel.INFO, message); }
    public void warn(String message)  { log(LogLevel.WARN, message); }
    public void error(String message) { log(LogLevel.ERROR, message); }
}