package org.akazukin.util.utils.timer;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.util.interfaces.Resettable;
import org.akazukin.util.object.TimeHolder;

import java.util.concurrent.TimeUnit;

/**
 * A utility class that provides functionality for tracking elapsed time with support for pausing,
 * resuming, and checking the time passed in various units.
 * <p>
 * This class is thread-safe for operations involving pausing and resuming the timer.
 * It implements the {@link Resettable} interface, allowing the timer to be reset
 * to its initial state.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Timer implements Resettable {
    public static final long NS_MS = 1000000L;
    public static final long MS_S = 1000L;
    public static final long NS_S = NS_MS * MS_S;

    /**
     * The Time.
     */
    long startedTime = -1;
    long pausedAtTime = -1;

    public Timer() {
        this.reset();
    }

    /**
     * Resets the timer to its initial state.
     * <p>
     * This method updates the internal state of the timer,
     * setting the start time to the current system time in nanoseconds,
     * and marking the paused time as inactive.
     */
    @Override
    public void reset() {
        this.startedTime = System.nanoTime();
        this.pausedAtTime = -1;
    }

    /**
     * Checks if the specified time, encapsulated within a {@link TimeHolder}, has passed.
     * The check compares the remaining time, computed in nanoseconds, against zero.
     *
     * @param holder the {@link TimeHolder} containing the time to check.
     *               Must not be {@code null}.
     * @return {@code true} if the remaining time is less than or equal to zero,
     * otherwise {@code false}.
     */
    public boolean hasPassedTime(final TimeHolder holder) {
        return this.getLeftTime(holder).toConvert(TimeUnit.NANOSECONDS) <= 0;
    }

    /**
     * Calculates the remaining time encapsulated in a {@link TimeHolder},
     * by subtracting the elapsed time from the initial time provided.
     *
     * @param holder the {@link TimeHolder} containing the total time to compare against.
     *               Must not be {@code null}.
     * @return a {@link TimeHolder} representing the remaining time.
     * The resulting time is calculated in nanoseconds.
     */
    public TimeHolder getLeftTime(final TimeHolder holder) {
        final long passedNs = this.getPassedTime().toConvert(TimeUnit.NANOSECONDS);
        final long reqNs = holder.toConvert(TimeUnit.NANOSECONDS);

        final TimeHolder res = new TimeHolder();
        res.addTime(reqNs - passedNs, TimeUnit.NANOSECONDS);
        return res;
    }

    /**
     * Calculates the elapsed time since the {@link Timer} was started or last reset,
     * subtracting any paused duration, and wraps it in a {@link TimeHolder}.
     * The time is measured in nanoseconds.
     *
     * @return the elapsed time encapsulated within a {@link TimeHolder}.
     */
    public TimeHolder getPassedTime() {
        final long passedNs = System.nanoTime() - this.startedTime -
                (this.pausedAtTime != -1 ? System.nanoTime() - this.pausedAtTime : 0);

        final TimeHolder holder = new TimeHolder();
        holder.addTime(passedNs, TimeUnit.NANOSECONDS);
        return holder;
    }

    /**
     * Pauses the timer.
     * If the timer is already paused, calling this method has no effect.
     * <p>
     * This method ensures thread safety by synchronizing access.
     */
    public synchronized void pause() {
        if (this.pausedAtTime == -1) {
            this.pausedAtTime = System.nanoTime();
        }
    }

    /**
     * Resumes the timer if it is currently paused.
     * <p>
     * This method adjusts the timer's internal state to exclude the paused duration
     * from the total elapsed time. If the timer is not paused, this method returns immediately.
     */
    public synchronized void resume() {
        if (this.pausedAtTime == -1) {
            return;
        }

        this.startedTime += System.nanoTime() - this.pausedAtTime;
        this.pausedAtTime = -1;
    }

    /**
     * Checks if the timer is currently paused.
     *
     * @return true if the timer is paused, otherwise false
     */
    public boolean isPaused() {
        return this.pausedAtTime != -1;
    }
}
