package org.akazukin.util.utils.time.scheduler;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.util.object.TimeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * TimerScheduler is an implementation of the {@link IScheduler} interface
 * that uses a {@link Timer} to schedule tasks.
 * It allows scheduling tasks with a delay or at fixed intervals
 * and supports both daemon and non-daemon timer threads.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ThreadSafe
public final class TimerScheduler extends AScheduler<TimerTask> {
    Timer timer;

    /**
     * Constructs a default {@code TimerScheduler} instance with a non-daemon timer thread.
     * This scheduler uses a single background thread to manage task execution.
     */
    public TimerScheduler() {
        this(false);
    }

    /**
     * Constructs a {@code TimerScheduler} instance with a specified daemon mode.
     * This scheduler uses a single background thread to manage task execution.
     *
     * @param daemon {@code true} if the timer thread should run as a daemon thread,
     *               {@code false} if the timer thread should run as a user thread.
     */
    public TimerScheduler(final boolean daemon) {
        this.timer = new Timer(daemon);
    }

    @Override
    @SneakyThrows
    public void close() {
        this.timer.cancel();
    }

    @Override
    public synchronized void cancelAllTasks() {
        this.timer.purge();
        this.tasks.clear();
    }

    @Override
    protected TimerTask scheduleLoopInternal(final Runnable task, final TimeHolder delay, final TimeHolder interval) {
        final TimerTask timerTask = toTimerTask(task);
        this.timer.scheduleAtFixedRate(timerTask, delay.toConvert(TimeUnit.MILLISECONDS), interval.toConvert(TimeUnit.MILLISECONDS));
        return timerTask;
    }

    @Override
    protected TimerTask scheduleInternal(final Runnable task, final TimeHolder delay) {
        final TimerTask timerTask = toTimerTask(task);
        this.timer.schedule(timerTask, delay.toConvert(TimeUnit.MILLISECONDS));
        return timerTask;
    }

    @Override
    protected void cancelInternal(@NotNull final TimerTask task) {
        task.cancel();
    }

    private static TimerTask toTimerTask(final Runnable task) {
        return new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        };
    }
}
