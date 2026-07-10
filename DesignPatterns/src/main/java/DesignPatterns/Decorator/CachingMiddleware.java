package DesignPatterns.Decorator;

import java.util.HashMap;
import java.util.Map;

public class CachingMiddleware implements HttpHandler {
    private final HttpHandler httpHandler;
    private final Map<String, HttpResponse> cache;

    public CachingMiddleware(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
        this.cache = new HashMap<>();
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        String key = request.url();
        if (cache.containsKey(key)) {
            return cache.get(key).withHeader("X-Cache", "HIT");
        }
        HttpResponse response = httpHandler.handle(request);
        if (response.getStatus() < 400) {       // only cache success responses
            cache.put(key, response);
        }
        return response.withHeader("X-Cache", "MISS");
    }
}
