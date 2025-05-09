package org.akazukin.util.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class TimeUtils {
    public static final long NANO_MS = 1000000L;
    public static final long MS_S = 1000L;
    public static final long MIN_SEC = 60L;
    public static final long HOUR_MIN = 60L;
    public static final long DAY_HOUR = 24L;
    public static final long MS = 1000L;
    private static final Pattern TIME_PATTERN = Pattern.compile("^([0-9]+)(:([0-9]+))?(:([0-9]+))?(:([0-9]+))?(:" +
            "([0-9]+))?$");
    private static final Pattern TIME_PATTERN2 = Pattern.compile("^(([0-9]+)(?i)(y|years?|年間?))?(([0-9]+)(M|(?i)" +
            "months?|月間?))?(([0-9]+)(?i)(w|weeks?|週間?))?(([0-9]+)(?i)(d|days?|日間?))?(([0-9]+)(?i)(h|hours?|時間?))?(" +
            "([0-9]+)(m|(?i)minutes?|分間?))?(([0-9]+)(?i)(s|seconds?|秒間?))?$");

    public static String durationFormat(final long ms) {
        final long mS = ms % TimeUtils.MS_S;
        final long sec = (ms / TimeUtils.MS_S) % TimeUtils.MIN_SEC;
        final long min = (ms / (TimeUtils.MS_S * TimeUtils.MIN_SEC)) % TimeUtils.HOUR_MIN;
        final long h = (ms / (TimeUtils.MS_S * TimeUtils.MIN_SEC * TimeUtils.HOUR_MIN)) % TimeUtils.DAY_HOUR;
        final long d = (ms / (TimeUtils.MS_S * TimeUtils.MIN_SEC * TimeUtils.HOUR_MIN * TimeUtils.DAY_HOUR));

        String str = String.format("%02d:%02d", min, sec);
        if (mS != 0) {
            str += String.format(".%03d", mS);
        }

        if (d != 0 && h != 0) {
            str += String.format("%02d:", d) + str;
        }
        if (h != 0) {
            str += String.format("%02d:", h) + str;
        }

        return str;
    }

    public static long parseTime(final String timeStr) {
        long years = 0;
        long months = 0;
        long weeks = 0;
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;

        final Matcher m = TIME_PATTERN.matcher(timeStr);
        if (m.matches()) {
            int i = 0;
            for (int i2 = m.groupCount(); i2 > 0; i2--) {
                if (i2 % 2 == 0) {
                    continue;
                }
                final String g = m.group(i2);
                if (g == null) {
                    continue;
                }
                try {
                    final int parsed = Integer.parseInt(g);
                    if (i == 0) {
                        seconds += parsed;
                    } else if (i == 1) {
                        minutes += parsed;
                    } else if (i == 2) {
                        hours += parsed;
                    } else if (i == 3) {
                        days += parsed;
                    } else if (i == 4) {
                        years += parsed;
                    }
                } catch (final NumberFormatException ignored) {
                }
                i++;
            }
        }

        final Matcher m2 = TIME_PATTERN2.matcher(timeStr.replaceAll("(and| " +
                "|\t|,)", ""));
        if (m2.matches()) {
            for (int i = 0; i < m2.groupCount(); i++) {
                try {
                    final int parsed = Integer.parseInt(m2.group(i));
                    if (i == 2) {
                        years += parsed;
                    } else if (i == 5) {
                        months += parsed;
                    } else if (i == 8) {
                        weeks += parsed;
                    } else if (i == 11) {
                        days += parsed;
                    } else if (i == 14) {
                        hours += parsed;
                    } else if (i == 17) {
                        minutes += parsed;
                    } else if (i == 20) {
                        seconds += parsed;
                    }
                } catch (final NumberFormatException ignored) {
                }
            }
        }

        return (long) ((((((((years * 365.25) + (months * 30.4375) + (weeks * 7) + days) * 24) + hours) * 60) + minutes) * 60) + seconds);
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
