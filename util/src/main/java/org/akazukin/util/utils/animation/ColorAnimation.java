package org.akazukin.util.utils.animation;

import org.jetbrains.annotations.Nullable;

import java.awt.Color;

@Deprecated
public class ColorAnimation {

    private final Animation red;
    private final Animation green;
    private final Animation blue;
    private final Animation alpha;

    public ColorAnimation(final Easing animation) {
        this(null, animation);
    }

    public ColorAnimation(@Nullable final Color color, final Easing animation) {
        this.red = new Animation((color == null) ? 0.0f : color.getRed(), animation);
        this.green = new Animation((color == null) ? 0.0f : color.getGreen(), animation);
        this.blue = new Animation((color == null) ? 0.0f : color.getBlue(), animation);
        this.alpha = new Animation((color == null) ? 0.0f : color.getAlpha(), animation);
    }

    public boolean animateTo(final Color color, final long durationMS) {
        final boolean r = this.red.animateTo(color.getRed(), durationMS).isChanged();
        final boolean g = this.green.animateTo(color.getGreen(), durationMS).isChanged();
        final boolean b = this.blue.animateTo(color.getBlue(), durationMS).isChanged();
        final boolean a = this.alpha.animateTo(color.getAlpha(), durationMS).isChanged();

        return r || g || b || a;
    }

    public Color getValue() {
        return new Color(
                Math.max(Math.min((int) this.red.getValue(), 255), 0),
                Math.max(Math.min((int) this.green.getValue(), 255), 0),
                Math.max(Math.min((int) this.blue.getValue(), 255), 0),
                Math.max(Math.min((int) this.alpha.getValue(), 255), 0)
        );
    }

    public void setValue(final Color color) {
        this.red.setValue(color.getRed());
        this.green.setValue(color.getGreen());
        this.blue.setValue(color.getBlue());
        this.alpha.setValue(color.getAlpha());
    }

    public void reset() {
        this.red.reset();
        this.green.reset();
        this.blue.reset();
        this.alpha.reset();
    }
}
