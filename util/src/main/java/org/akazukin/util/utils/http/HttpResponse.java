package org.akazukin.util.utils.http;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

/**
 * Represents an HTTP response received from executing an HTTP request.
 * <p>
 * This class encapsulates various details about the HTTP response, including the response code,
 * headers, content length, and response payload.
 * It also provides access to any error payload that may have been received.
 * Instances of this class are immutable and are typically returned
 * by HTTP utility methods, such as those in the `HttpUtils` class.
 * <p>
 * Key Details:
 * - `responseCode`: The HTTP response status code. Common values include 200 for success or 404 for not found.
 * - `response`: The raw byte array representing the response body returned by the server.
 * - `contentLength`: The length of the response content in bytes, as specified by the server.
 * - `error`: The raw byte array representing the error body, if the server returned an error response.
 * - `responseHeader`: A map containing the HTTP response headers, where the key represents the header name,
 * and the value is a list of values for that header.
 */
@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Deprecated
public final class HttpResponse {
    int responseCode;
    byte[] response;
    long contentLength;
    byte[] error;
    Map<String, List<String>> responseHeader;
}
