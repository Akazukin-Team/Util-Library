package org.akazukin.util.utils.timer;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.util.interfaces.Resettable;

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
     * Calculates the total number of seconds that have passed since the timer started or was last reset,
     * excluding any paused duration.
     *
     * @return the elapsed time in seconds
     */
    public long getPassedSec() {
        return this.getPassedNs() / Timer.NS_S;
    }

    /**
     * Calculates the total number of nanoseconds that have passed since the timer started or was last reset,
     * excluding any paused duration.
     *
     * @return the elapsed time in nanoseconds
     */
    public long getPassedNs() {
        return System.nanoTime() - this.startedTime -
                (this.pausedAtTime != -1 ? System.nanoTime() - this.pausedAtTime : 0);
    }

    /**
     * Calculates the total number of milliseconds that have passed since the timer started or was last reset,
     * excluding any paused duration.
     *
     * @return the elapsed time in milliseconds
     */
    public long getPassedMs() {
        return this.getPassedNs() / Timer.NS_MS;
    }

    /**
     * Determines if the specified number of milliseconds has passed
     * since the timer started or was reset.
     *
     * @param ms the number of milliseconds to check
     * @return true if the specified milliseconds have passed, otherwise false
     */
    public boolean hasPassedMs(final long ms) {
        return this.hasPassedNs(ms * Timer.NS_MS);
    }

    /**
     * Determines if the specified number of nanoseconds has passed
     * since the timer started or was reset.
     *
     * @param ns the number of nanoseconds to check
     * @return true if the specified nanoseconds have passed, otherwise false
     */
    public boolean hasPassedNs(final long ns) {
        return this.getLeftNs(ns) <= 0;
    }

    /**
     * Calculates the number of nanoseconds remaining until the specified target time.
     * This method determines the difference between the provided time and the elapsed
     * nanoseconds since the timer was started or last reset.
     *
     * @param ns the target time in nanoseconds
     * @return the remaining nanoseconds until the target time
     */
    public long getLeftNs(final long ns) {
        return ns - this.getPassedNs();
    }

    /**
     * Determines if the specified number of seconds has passed
     * since the timer started or was reset.
     *
     * @param sec the number of seconds to check
     * @return true if the specified seconds have passed, otherwise false
     */
    public boolean hasPassedSec(final long sec) {
        return this.hasPassedNs(sec * Timer.NS_S);
    }

    /**
     * Calculates the number of milliseconds remaining until the specified target time.
     * This method determines the difference between the provided time and the elapsed
     * milliseconds since the timer was started or last reset.
     *
     * @param ms the target time in milliseconds
     * @return the remaining milliseconds until the target time
     */
    public long getLeftMs(final long ms) {
        return this.getLeftNs(ms * Timer.NS_MS) / Timer.NS_MS;
    }

    /**
     * Calculates the number of seconds remaining until the specified target time.
     * This method determines the difference between the provided time and the elapsed
     * seconds since the timer was started or last reset.
     *
     * @param sec the target time in seconds
     * @return the remaining seconds until the target time
     */
    public long getLeftSec(final long sec) {
        return this.getLeftNs(sec * Timer.NS_S) / Timer.NS_S;
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
