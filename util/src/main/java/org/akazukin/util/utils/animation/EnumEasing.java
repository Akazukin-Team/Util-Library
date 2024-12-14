package org.akazukin.util.utils.animation;

import lombok.Getter;

public enum EnumEasing {
    LINEAR(value -> value),
    SINE(value -> Math.sin(value * Math.PI / 2)),
    QUAD(value -> 1 - (1 - value) * (1 - value)),
    CUBIC(value -> 1 - Math.pow(1 - value, 3)),
    QUART(value -> 1 - Math.pow(1 - value, 4)),
    QUINT(value -> 1 - Math.pow(1 - value, 5)),
    EXPO(value -> value == 1 ? 1 : 1 - Math.pow(2, -10 * value)),
    CIRC(value -> Math.sqrt(1 - Math.min(Math.pow(value - 1, 2), 1))),
    BACK(value -> {
        double c1 = 1.70158;
        double c3 = c1 + 1;
        return 1 + c3 * Math.pow(value - 1, 3) + c1 * Math.pow(value - 1, 2);
    }),
    ELASTIC(value -> {
        double c4 = 2 * Math.PI / 3;

        return value == 0 ? 0 : value == 1 ? 1 : Math.pow(2, -10 * value) * Math.sin((value * 10 - 0.75) * c4) + 1;
    }),
    BOUNCE(value -> {
        float n1 = 7.5625F;
        float d1 = 2.75F;

        if (value < 1 / d1) {
            return n1 * value * value;
        } else if (value < 2 / d1) {
            return n1 * (value -= 1.5F / d1) * value + 0.75;
        } else if (value < 2.5 / d1) {
            return n1 * (value -= 2.25F / d1) * value + 0.9375;
        } else {
            return n1 * (value -= 2.625F / d1) * value + 0.984375;
        }
    });

    @Getter
    private final IEasing easing;

    EnumEasing(final IEasing easing) {
        this.easing = easing;
    }

    public double ease(final float value) {
        return this.easing.ease(value);
    }
}
