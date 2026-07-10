package DesignPatterns.Decorator;

public class UserListHandler implements HttpHandler {
    @Override
    public HttpResponse handle(HttpRequest request) {
        return new HttpResponse(200,
                null,
                "[{\"id\":1,\"name\":\"Alice\"},{\"id\":2,\"name\":\"Bob\"}]");
    }
}
