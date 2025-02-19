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


    public HttpResponse request(@NonNull final String url,
                                @Nullable final Properties header, @Nullable final String body,
                                @NonNull final HttpMethod method)
            throws IOException, URISyntaxException {
        return request(url, header, body, method, DEFAULT_CONFIG);
    }

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
