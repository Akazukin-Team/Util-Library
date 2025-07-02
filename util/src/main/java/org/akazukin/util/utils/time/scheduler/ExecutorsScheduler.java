package org.akazukin.util.utils.time.scheduler;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.util.object.TimeHolder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * ExecutorsScheduler is an implementation of the {@link IScheduler} interface
 * that uses a {@link ScheduledExecutorService} to schedule tasks.
 * It allows scheduling tasks with a delay or at fixed intervals
 * and supports configurable thread pool sizes and custom thread factories.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@ThreadSafe
public class ExecutorsScheduler extends AScheduler<ScheduledFuture<?>> {
    final ScheduledThreadPoolExecutor pool;

    /**
     * Constructs a default {@code Scheduler} instance with max number of pool sizes.
     * This scheduler uses a scheduled thread pool to manage task execution.
     */
    public ExecutorsScheduler() {
        this(Integer.MAX_VALUE);
    }

    /**
     * Constructs a {@code Scheduler} instance with a specified number of threads.
     * This scheduler uses a scheduled thread pool to manage task execution.
     *
     * @param poolSize the number of threads to keep scheduled in the thread pool.
     *                 Must be a positive integer.
     */
    public ExecutorsScheduler(final int poolSize) {
        this.pool = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(poolSize);
    }

    /**
     * Constructs a {@code Scheduler} instance using a default of one thread with a custom thread factory.
     * This scheduler uses a scheduled thread pool to manage task execution.
     *
     * @param threadFactory the {@link ThreadFactory} to use for creating new threads in the thread pool.
     *                      Must not be {@code null}.
     */
    public ExecutorsScheduler(final ThreadFactory threadFactory) {
        this(1, threadFactory);
    }

    /**
     * Constructs a {@code Scheduler} instance with a specified number of threads and a custom thread factory.
     * This scheduler uses a scheduled thread pool to manage task execution.
     *
     * @param poolSize      the number of threads to keep scheduled in the thread pool.
     *                      Must be a positive integer.
     * @param threadFactory the {@link ThreadFactory} to use for creating new threads in the thread pool.
     *                      Must not be {@code null}.
     */
    public ExecutorsScheduler(final int poolSize, final ThreadFactory threadFactory) {
        this.pool = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(poolSize, threadFactory);
    }

    @Override
    public synchronized boolean isScheduled(final long id) {
        return super.isScheduled(id)
                && !this.tasks.get(id).getValue().isDone();
    }

    @Override
    public synchronized void cancelAllTasks() {
        this.pool.purge();
        this.tasks.clear();
    }

    @Override
    protected ScheduledFuture<?> scheduleLoopInternal(final Runnable task, final TimeHolder delay, final TimeHolder interval) {
        return this.pool.scheduleAtFixedRate(task,
                delay.toConvert(TimeUnit.NANOSECONDS),
                interval.toConvert(TimeUnit.NANOSECONDS),
                TimeUnit.MILLISECONDS);
    }

    @Override
    protected ScheduledFuture<?> scheduleInternal(final Runnable task, final TimeHolder delay) {
        return this.pool.schedule(task,
                delay.toConvert(TimeUnit.NANOSECONDS),
                TimeUnit.MILLISECONDS);
    }

    @Override
    protected void cancelInternal(final ScheduledFuture<?> task) {
        task.cancel(true);
    }

    /**
     * Terminates the scheduler by canceling all scheduled tasks and releasing resources used by the internal timer.
     * This method should be called to gracefully shut down the Scheduler instance when it is no longer necessary.
     * Once this method is called, the scheduler is no longer operational, and no further tasks can be scheduled.
     */
    @Override
    @SneakyThrows
    public void close() {
        this.pool.shutdown();
        if (!this.pool.awaitTermination(10, TimeUnit.SECONDS)) {
            this.pool.shutdownNow();
        }
    }
}
