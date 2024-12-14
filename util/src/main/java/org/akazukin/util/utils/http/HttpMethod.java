package org.akazukin.util.utils.http;

import lombok.Getter;

@Getter
public enum HttpMethod {
    GET("GET"),
    POST("POST");

    private final String method;

    HttpMethod(final String method) {
        this.method = method;
    }
}
