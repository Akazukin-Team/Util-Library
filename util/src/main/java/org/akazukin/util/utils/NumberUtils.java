package org.akazukin.util.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberUtils {
    public static final long NANO_MS = 1000000L;
    public static final long MS_S = 1000L;
    public static final long MIN_SEC = 60L;
    public static final long HOUR_MIN = 60L;
    public static final long DAY_HOUR = 24L;
    public static final long MS = 1000L;

    public static String durationFormat(final long ms) {
        final long mS = ms % NumberUtils.MS_S;
        final long sec = (ms / NumberUtils.MS_S) % NumberUtils.MIN_SEC;
        final long min = (ms / (NumberUtils.MS_S * NumberUtils.MIN_SEC)) % NumberUtils.HOUR_MIN;
        final long h = (ms / (NumberUtils.MS_S * NumberUtils.MIN_SEC * NumberUtils.HOUR_MIN)) % NumberUtils.DAY_HOUR;
        final long d = (ms / (NumberUtils.MS_S * NumberUtils.MIN_SEC * NumberUtils.HOUR_MIN * NumberUtils.DAY_HOUR));

        String str = String.format("%02d:%02d", min, sec);
        if (mS != 0) str += String.format(".%03d", mS);

        if (d != 0 && h != 0) str += String.format("%02d:", d) + str;
        if (h != 0) str += String.format("%02d:", h) + str;

        return str;
    }

    public static String durationFormat2(final long ms) {
        final int days = (int) (ms / 1000 / 60 / 60 / 24);
        final int hours = (int) (ms / 1000 / 60 / 60) % 24;
        final int minutes = (int) (ms / 1000 / 60) % 60;
        final int seconds = (int) (ms / 1000) % 60;

        String formated = "";
        if (days != 0) {
            formated += days;
        }
        if (hours != 0 || !formated.isEmpty()) {
            if (!formated.isEmpty()) {
                formated += ":";
                if (hours < 10) {
                    formated += "0";
                }
            }
            formated += hours;
        }
        if (minutes != 0 || !formated.isEmpty()) {
            if (!formated.isEmpty()) {
                formated += ":";
                if (minutes < 10) {
                    formated += "0";
                }
            }
            formated += minutes;
        }
        if (seconds != 0 || !formated.isEmpty()) {
            if (!formated.isEmpty()) {
                formated += ":";
                if (seconds < 10) {
                    formated += "0";
                }
            }
            formated += seconds;
        }

        return formated;
    }
}
