package DesignPatterns.Decorator;

import java.util.Map;

public record HttpRequest(String method, String url, Map<String, String> headers, String body) {
}