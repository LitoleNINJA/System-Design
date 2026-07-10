package DesignPatterns.Decorator;

public class LoggingMiddleware implements HttpHandler {
    private final HttpHandler httpHandler;
    public LoggingMiddleware(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        long startTime = System.currentTimeMillis();
        System.out.printf("[LOG] -> %s %s%n", request.method(), request.url());

        HttpResponse response = httpHandler.handle(request);

        long totalTime = System.currentTimeMillis() - startTime;
        System.out.printf("[LOG] <- %d in %dms%n", response.getStatus(), totalTime);
        return response;
    }
}
