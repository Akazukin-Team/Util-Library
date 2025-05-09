package org.akazukin.util.utils.animation;

@FunctionalInterface
@Deprecated
public interface Easing {
    Easing LINEAR = value -> value;
    Easing SINE = value -> Math.sin(value * Math.PI / 2);
    Easing QUAD = value -> 1 - (1 - value) * (1 - value);
    Easing CUBIC = value -> 1 - Math.pow(1 - value, 3);
    Easing QUART = value -> 1 - Math.pow(1 - value, 4);
    Easing QUINT = value -> 1 - Math.pow(1 - value, 5);
    Easing EXPO = value -> value == 1 ? 1 : 1 - Math.pow(2, -10 * value);
    Easing CIRC = value -> Math.sqrt(1 - Math.min(Math.pow(value - 1, 2), 1));
    Easing BACK = value -> {
        final double c1 = 1.70158;
        final double c3 = c1 + 1;
        return 1 + c3 * Math.pow(value - 1, 3) + c1 * Math.pow(value - 1, 2);
    };
    Easing ELASTIC = value -> {
        final double c4 = 2 * Math.PI / 3;

        return value == 0 ? 0 : value == 1 ? 1 : Math.pow(2, -10 * value) * Math.sin((value * 10 - 0.75) * c4) + 1;
    };
    Easing BOUNCE = value -> {
        final float n1 = 7.5625F;
        final float d1 = 2.75F;

        if (value < 1 / d1) {
            return n1 * value * value;
        } else if (value < 2 / d1) {
            return n1 * (value -= 1.5F / d1) * value + 0.75;
        } else if (value < 2.5 / d1) {
            return n1 * (value -= 2.25F / d1) * value + 0.9375;
        } else {
            return n1 * (value -= 2.625F / d1) * value + 0.984375;
        }
    };

    double ease(float value);
}