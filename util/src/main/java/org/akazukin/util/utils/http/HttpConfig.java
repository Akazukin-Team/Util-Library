package org.akazukin.util.utils.http;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Configuration object for HTTP connections.
 * This class encapsulates various parameters that control HTTP connection behavior.
 * <p>
 * Fields provided in this class allow setting timeouts, input/output behaviors,
 * caching preferences, and redirect handling for HTTP connections.
 * <p>
 * This object is generally used to configure HTTP connection properties before initiating requests.
 * <p>
 * Features:
 * - Connect timeout: Defines the maximum time to establish a connection.
 * - Read timeout: Specifies the maximum time to wait for a response.
 * - Input handling: Determines whether the connection allows input.
 * - Output handling: Determines whether the connection allows output.
 * - Caching preferences: Controls if caching is enabled for the connection.
 * - Redirect handling: Specifies whether HTTP redirects are automatically followed.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Deprecated
public class HttpConfig {
    int connectTimeout = 2500;
    int readTimeout = 5000;
    boolean doInput = true;
    boolean doOutput = true;
    boolean doUseCaches = true;
    boolean followRedirects = true;
}
