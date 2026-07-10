package DesignPatterns.Decorator;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final int status;
    private final Map<String, String> headers;
    private final String body;

    public HttpResponse(int status, Map<String, String> headers, String body) {
        this.status  = status;
        this.headers = (headers == null) ? Map.of() : Map.copyOf(headers);
        this.body    = body;
    }

    public int                 getStatus()  { return status; }
    public Map<String, String> getHeaders() { return headers; }
    public String              getBody()    { return body; }

    public static HttpResponse ok(String body) {
        return new HttpResponse(200, Map.of(), body);
    }

    public static HttpResponse unauthorized() {
        return new HttpResponse(401, Map.of(), "Unauthorized");
    }

    /** Returns a NEW response with the existing fields plus one extra header. */
    public HttpResponse withHeader(String key, String value) {
        Map<String, String> next = new HashMap<>(headers);
        next.put(key, value);
        return new HttpResponse(status, next, body);
    }
}
