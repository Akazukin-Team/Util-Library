package org.akazukin.util.utils.http;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.akazukin.util.utils.IOUtils;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Properties;

/**
 * Utility class providing static methods for making HTTP requests.
 * This class simplifies the process of configuring and executing HTTP and HTTPS requests,
 * with support for request headers, request bodies, custom HTTP methods, and configurable timeouts.
 * <p>
 * HttpUtils is designed as a utility class and cannot be instantiated.
 * It provides helper methods for performing HTTP operations and managing configurations.
 * <p>
 * Features:
 * - Default configuration for connection and read timeouts.
 * - Support for HTTP and HTTPS protocols.
 * - Configurable request headers and body.
 * - Customizable request configurations through {@code HttpConfig}.
 */
@UtilityClass
public class HttpUtils {
    private final static HttpConfig DEFAULT_CONFIG = new HttpConfig();

    private final static int CONNECTION_TIMEOUT = 2500;
    private final static int READ_TIMEOUT = 5000;

    static {
        DEFAULT_CONFIG.setConnectTimeout(CONNECTION_TIMEOUT);
        DEFAULT_CONFIG.setReadTimeout(READ_TIMEOUT);
        DEFAULT_CONFIG.setDoInput(true);
        DEFAULT_CONFIG.setDoOutput(true);
        DEFAULT_CONFIG.setDoUseCaches(false);
    }


    /**
     * Sends an HTTP request to the specified URL with optional headers, body, and the desired HTTP method.
     * <p>
     * This method leverages the default configuration for HTTP connection settings and supports both HTTP
     * and HTTPS protocols.
     * It processes the request based on the specified HTTP method, headers, and body.
     * The response, including status code, headers, body, and error (if any),
     * is encapsulated in an {@link HttpResponse} object and returned.
     *
     * @param url    the URL to send the HTTP request to. Must not be null.
     * @param header a {@link Properties} object containing request headers.
     *               Can be null or empty.
     * @param body   the request body to be sent.
     *               Can be null if the HTTP method does not require a body.
     * @param method the HTTP method to use for the request (e.g., GET, POST).
     *               Must not be null.
     * @return an {@link HttpResponse} object containing the HTTP response details, including status
     * code, body, headers, and any error content if applicable.
     * @throws IOException        if an I/O error occurs during request or response handling.
     * @throws URISyntaxException if the provided URL is malformed or uses an unsupported protocol.
     */
    public HttpResponse request(@NonNull final String url,
                                @Nullable final Properties header, @Nullable final String body,
                                @NonNull final HttpMethod method)
            throws IOException, URISyntaxException {
        return request(url, header, body, method, DEFAULT_CONFIG);
    }

    /**
     * Sends an HTTP request to the specified URL using the provided configurations, headers, and body.
     * <p>
     * This method supports both HTTP and HTTPS protocols, allowing customized configurations for connectivity,
     * such as timeouts, headers, and redirect behavior.
     * It constructs the request with the specified HTTP method and sends the request body if provided.
     * The response, including status code, headers, and body,
     * is returned encapsulated within a {@link HttpResponse} object.
     *
     * @param url    the URL to send the HTTP request to.
     *               Must not be null.
     * @param header a {@link Properties} object containing request headers.
     *               Can be null or empty.
     * @param body   the request body to be sent.
     *               Can be null if the method does not require a body.
     * @param method the HTTP method to use for the request (e.g., GET, POST).
     *               Must not be null.
     * @param config the HTTP configuration object specifying connection settings and behaviors.
     *               Must not be null.
     * @return an {@link HttpResponse} object containing the status code, response body, headers,
     * error body (if any), and content length.
     * @throws IOException        if an I/O exception occurs during the request or response handling.
     * @throws URISyntaxException if the provided URL is invalid or the protocol is not supported.
     */
    public HttpResponse request(@NonNull final String url,
                                @Nullable final Properties header, @Nullable final String body,
                                @NonNull final HttpMethod method,
                                @NonNull final HttpConfig config)
            throws IOException, URISyntaxException {
        HttpURLConnection con = null;
        try {
            final URL url_ = new URI(url).toURL();
            final URLConnection con_ = url_.openConnection();
            if (con_ instanceof HttpsURLConnection) {
                con = (HttpsURLConnection) con_;
            } else if (con_ instanceof HttpURLConnection) {
                con = (HttpURLConnection) con_;
            } else {
                throw new URISyntaxException(url, "Unsupported protocol");
            }

            if (header != null && !header.isEmpty()) {
                for (final Map.Entry<Object, Object> entry : header.entrySet()) {
                    con.setRequestProperty(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                }
            }

            con.setRequestMethod(method.getMethod());
            con.setConnectTimeout(config.getConnectTimeout());
            con.setReadTimeout(5000);
            con.setInstanceFollowRedirects(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(true);

            con.connect();

            if (body != null) {
                try (final OutputStream os = con.getOutputStream()) {
                    try (final OutputStreamWriter osw = new OutputStreamWriter(os)) {
                        osw.write(body);
                    }
                }
            }


            byte[] resIS = null;
            byte[] errorIS = null;

            try (final InputStream is = con.getInputStream()) {
                if (is != null) {
                    resIS = IOUtils.readAllBytes(is);
                }
            }

            try (final InputStream is = con.getErrorStream()) {
                if (is != null) {
                    errorIS = IOUtils.readAllBytes(is);
                }
            }

            return new HttpResponse(con.getResponseCode(), resIS, con.getContentLengthLong(),
                    errorIS,
                    con.getHeaderFields());
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
}
