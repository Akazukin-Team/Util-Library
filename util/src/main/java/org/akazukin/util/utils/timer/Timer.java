package org.akazukin.util.utils.timer;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.util.ext.Resettable;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Timer implements Resettable {
    public static final long NANO_MS = 1000000L;
    public static final long MS_S = 1000L;

    /**
     * The Time.
     */
    Long startedTime = null;
    Long pausedAtTime = null;

    public Timer() {
        this.reset();
    }

    /**
     * Reset.
     */
    @Override
    public void reset() {
        this.startedTime = System.nanoTime();
        this.pausedAtTime = null;
    }

    public long getPassedMSTime() {
        return this.getPassedNanoTime() / Timer.NANO_MS;
    }

    public long getPassedNanoTime() {
        return System.nanoTime() - this.startedTime -
                (this.pausedAtTime != null ? System.nanoTime() - this.pausedAtTime : 0);
    }

    /**
     * Has time passed boolean.
     *
     * @param ms the ms
     * @return the boolean
     */
    public boolean hasMSTimePassed(final long ms) {
        return this.hasNanoTimePassed(ms * Timer.NANO_MS);
    }

    /**
     * Has time passed boolean.
     *
     * @param nano the ms
     * @return the boolean
     */
    public boolean hasNanoTimePassed(final long nano) {
        return this.getNanoTimeLeft(nano) <= 0;
    }

    /**
     * Has time left long.
     *
     * @param nano the nanoseconds
     * @return the long
     */
    public long getNanoTimeLeft(final long nano) {
        return nano - this.getPassedNanoTime();
    }

    public long getLeftSecTime(final long ms) {
        return this.getMSTimeLeft(ms * Timer.MS_S) / Timer.MS_S;
    }

    /**
     * Has time left long.
     *
     * @param ms the milliseconds
     * @return the long
     */
    public long getMSTimeLeft(final long ms) {
        return this.getNanoTimeLeft(ms * Timer.NANO_MS) / Timer.NANO_MS;
    }


    /**
     * pause the timer
     */
    public void pause() {
        if (this.pausedAtTime == null) this.pausedAtTime = System.nanoTime();
    }

    /**
     * resume the timer
     */
    public void resume() {
        if (this.pausedAtTime == null) return;

        this.startedTime += System.nanoTime() - this.pausedAtTime;
        this.pausedAtTime = null;
    }

    public boolean isPaused() {
        return this.pausedAtTime != null;
    }
}
