package org.akazukin.util.utils.http;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HttpConfig {
    int connectTimeout = 2500;
    int readTimeout = 5000;
    boolean doInput = true;
    boolean doOutput = true;
    boolean doUseCaches = true;
    boolean followRedirects = true;
}
