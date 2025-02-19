package org.akazukin.util.utils.http;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class HttpResponse {
    int responseCode;
    byte[] response;
    long contentLength;
    byte[] error;
    Map<String, List<String>> responseHeader;
}
