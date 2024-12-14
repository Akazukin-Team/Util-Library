package org.akazukin.util.utils.animation;

import lombok.Getter;
import lombok.Setter;

public class Animation {
    @Setter
    @Getter
    private float value;
    private long lastUpdateTime;
    @Setter
    private IEasing easing;

    public Animation(final float value, final IEasing easing) {
        this.value = value;
        this.lastUpdateTime = System.nanoTime();
        this.easing = easing;
    }

    public boolean animateTo(final float target, final double duration) {
        if (duration == 0) {
            final boolean r = target != this.value;
            this.value = target;
            return r;
        }


        final long currentTime = System.nanoTime();
        final long deltaTime = currentTime - this.lastUpdateTime;
        this.lastUpdateTime = currentTime;

        final float progress = (float) ((double) (deltaTime / 1000L) / (Math.max(duration * 100d, 1)));
        final float easedProgress = (float) this.easing.ease(progress);

        final float deltaValue = (target - this.value) * easedProgress;
        this.value += deltaValue;

        return deltaValue != 0f;
    }
}