package org.akazukin.util.utils.http;

import lombok.Getter;

/**
 * Enum representing HTTP methods used in HTTP requests.
 * <p>
 * This enum provides a predefined set of common HTTP methods such as GET and POST.
 * Each enum constant is associated with a string representation of the HTTP method,
 * which can be used to configure HTTP requests.
 * <p>
 * Features:
 * - Predefined HTTP method constants.
 * - Provides the string representation for each HTTP method.
 * <p>
 * Example usages often include HTTP utilities,
 * where the enum constants are used to set or specify the desired request method.
 */
@Getter
public enum HttpMethod {
    GET("GET"),
    POST("POST");

    private final String method;

    HttpMethod(final String method) {
        this.method = method;
    }
}
