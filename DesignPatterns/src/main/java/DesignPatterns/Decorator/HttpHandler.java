package DesignPatterns.Decorator;

public interface HttpHandler {
    HttpResponse handle(HttpRequest request);
}
