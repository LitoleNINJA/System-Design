package DesignPatterns.Decorator;

public class AuthMiddleware implements HttpHandler {
    private final HttpHandler httpHandler;
    private final String secret;

    public AuthMiddleware(HttpHandler httpHandler, String secret) {
        this.httpHandler = httpHandler;
        this.secret = secret;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {

        String value = request.headers().get("Authorization");
        if(value == null || !value.startsWith("Bearer ")) {
            return HttpResponse.unauthorized();
        }

        String token = value.split(" ")[1];
        if(!token.equals(secret)) {
            return HttpResponse.unauthorized();
        }

        httpHandler.handle(request);
        return httpHandler.handle(request);
    }
}
