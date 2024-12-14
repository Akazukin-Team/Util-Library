package org.akazukin.util.utils.animation;

import java.awt.Color;

public class ColorAnimation {

    private final Animation red;
    private final Animation green;
    private final Animation blue;
    private final Animation alpha;

    public ColorAnimation() {
        this(null, EnumEasing.SINE.getEasing());
    }

    public ColorAnimation(final Color color, final IEasing animation) {
        this.red = new Animation((color == null) ? 0.0f : color.getRed(), animation);
        this.green = new Animation((color == null) ? 0.0f : color.getGreen(), animation);
        this.blue = new Animation((color == null) ? 0.0f : color.getBlue(), animation);
        this.alpha = new Animation((color == null) ? 0.0f : color.getAlpha(), animation);
    }

    public boolean animateTo(final Color color, final float time) {
        final boolean r = this.red.animateTo(color.getRed(), time);
        final boolean g = this.green.animateTo(color.getGreen(), time);
        final boolean b = this.blue.animateTo(color.getBlue(), time);
        final boolean a = this.alpha.animateTo(color.getAlpha(), time);

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
}