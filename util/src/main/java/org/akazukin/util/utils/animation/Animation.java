package org.akazukin.util.utils.animation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Animation {
    private static final long MS_TO_NS = 1000000L;

    @Setter
    @Getter
    private float value;
    private long lastUpdateTime;
    @Setter
    private Easing easing;

    public Animation(final float value, final Easing easing) {
        this.value = value;
        this.lastUpdateTime = System.nanoTime();
        this.easing = easing;
    }

    public Result animateTo(final float target, final long durationMS) {
        final long currentTime = System.nanoTime();
        final long deltaTime = currentTime - this.lastUpdateTime;
        this.lastUpdateTime = currentTime;

        if (target == this.value) {
            return new Result(0, 0);
        }

        final float diff = target - this.value;

        if (durationMS == 0) {
            final Result r = new Result(1, diff);
            this.value = target;
            return r;
        }

        final float progress = (float) deltaTime / (durationMS * MS_TO_NS);
        final float easedProgress = (float) this.easing.ease(progress);


        if (easedProgress >= 1) {
            final Result r = new Result(1, diff);
            this.value = target;
            return r;
        }

        final float deltaValue = diff * easedProgress;
        this.value += deltaValue;

        return new Result(easedProgress, deltaValue);
    }

    public void reset() {
        this.lastUpdateTime = System.nanoTime();
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Result {
        float percent;
        float value;

        public boolean isChanged() {
            return this.percent != 0;
        }
    }
}