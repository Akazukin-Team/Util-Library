package org.akazukin.util.utils.http.deprecated;

import lombok.experimental.UtilityClass;
import org.akazukin.util.utils.IOUtils;
import org.akazukin.util.utils.http.HttpMethod;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Properties;

@UtilityClass
@Deprecated
public class HttpUtils {
    public static byte[] request(final String url, final Properties header, final HttpMethod method) throws IOException, URISyntaxException {
        return HttpUtils.request(url, header, null, method);
    }

    public static byte[] request(final String url, final Properties header, final String body,
                                 final HttpMethod method) throws IOException, URISyntaxException {
        HttpURLConnection con = null;
        try {
            if (url.startsWith("http://")) {
                con = (HttpURLConnection) new URI(url).toURL().openConnection();
            } else {
                con = (HttpsURLConnection) new URI(url).toURL().openConnection();
            }

            if (header != null && !header.isEmpty()) {
                for (final Map.Entry<Object, Object> entry : header.entrySet()) {
                    con.setRequestProperty(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                }
            }

            con.setRequestMethod(method.getMethod());
            con.setConnectTimeout(2500);
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

            try (final InputStream is = (con.getErrorStream() == null ? con.getInputStream() : con.getErrorStream())) {
                return IOUtils.readAllBytes(is);
            }
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    public static byte[] request(final String url, final String body, final HttpMethod method) throws IOException,
            URISyntaxException {
        return HttpUtils.request(url, null, body, method);
    }

    public static byte[] request(final String url, final HttpMethod method) throws IOException, URISyntaxException {
        return HttpUtils.request(url, null, null, method);
    }
}
