package org.akazukin.util.utils;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.util.object.TimeHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Scheduler is a thread-safe utility class for managing and scheduling tasks
 * with support for one-time and recurring task execution.
 * Tasks are uniquely identified by an ID and can be managed through
 * various scheduling and cancellation methods. It provides mechanisms
 * to handle exceptions during task execution via a configurable consumer.
 * The Scheduler also supports graceful shutdown to manage lifecycle effectively.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@ThreadSafe
public class Scheduler implements Closeable {
    final ScheduledExecutorService timer;

    final Map<Long, ScheduledFuture<?>> tasks = new HashMap<>();

    @Nullable
    Consumer<Throwable> throwableConsumer;

    /**
     * Constructs a default {@code Scheduler} instance with a single thread.
     * This scheduler uses a scheduled thread pool to manage task execution.
     */
    public Scheduler() {
        this(1);
    }

    /**
     * Constructs a {@code Scheduler} instance with a specified number of threads.
     * This scheduler uses a scheduled thread pool to manage task execution.
     *
     * @param threads the number of threads to be used in the scheduled thread pool.
     *                Must be a positive integer.
     */
    public Scheduler(final int threads) {
        this.timer = Executors.newScheduledThreadPool(threads);
    }

    /**
     * Constructs a {@code Scheduler} instance using a default of one thread with a custom thread factory.
     * This scheduler uses a scheduled thread pool to manage task execution.
     *
     * @param threadFactory the {@link ThreadFactory} to use for creating new threads in the thread pool.
     *                      Must not be {@code null}.
     */
    public Scheduler(final ThreadFactory threadFactory) {
        this(1, threadFactory);
    }

    /**
     * Constructs a {@code Scheduler} instance with a specified number of threads and a custom thread factory.
     * This scheduler uses a scheduled thread pool to manage task execution.
     *
     * @param threads       the number of threads to be used in the scheduled thread pool.
     *                      Must be a positive integer.
     * @param threadFactory the {@link ThreadFactory} to use for creating new threads in the thread pool.
     *                      Must not be {@code null}.
     */
    public Scheduler(final int threads, final ThreadFactory threadFactory) {
        this.timer = Executors.newScheduledThreadPool(threads, threadFactory);
    }

    /**
     * Sets a consumer to handle {@link Throwable} instances encountered during scheduled task execution.
     * If a consumer is provided, it will be invoked whenever a {@link Throwable} is thrown by a task.
     *
     * @param throwableConsumer a {@link Consumer} to process {@link Throwable} instances. It may be {@code null},
     *                          in which case no actions will be taken when a {@link Throwable} is encountered.
     */
    public synchronized void setThrowableConsumer(final @Nullable Consumer<Throwable> throwableConsumer) {
        this.throwableConsumer = throwableConsumer;
    }

    /**
     * Checks if a task with the specified identifier is currently scheduled.
     *
     * @param id the unique identifier of the task to check.
     * @return {@code true} if a task with the specified identifier is scheduled,
     * {@code false} otherwise.
     */
    public boolean isScheduled(final long id) {
        return this.tasks.containsKey(id);
    }

    /**
     * Cancels all currently scheduled tasks in the scheduler.
     * This method iterates through all the identifiers of scheduled tasks
     * and invokes {@link #cancelTask(long)} for each task, ensuring all tasks
     * are properly canceled.
     * <p>
     * This method is thread-safe and synchronized to prevent concurrent modifications
     * to the underlying task storage.
     */
    public synchronized void cancelAllTasks() {
        this.tasks.keySet().forEach(this::cancelTask);
    }

    /**
     * Cancels a scheduled task with the specified unique identifier.
     * If the task is not found, an {@link IllegalArgumentException} is thrown.
     *
     * @param id the unique identifier of the task to be canceled.
     *           It must match the identifier of a previously scheduled task.
     * @throws IllegalArgumentException if a task with the specified identifier is not found.
     */
    public synchronized void cancelTask(final long id) {
        final ScheduledFuture<?> task = this.tasks.remove(id);
        if (task == null) {
            throw new IllegalArgumentException("Task not found: " + id);
        }
        task.cancel(true);
    }

    /**
     * Schedules a one-time task to be executed after a specified delay.
     *
     * @param id    the unique identifier for the task. If a task with the same id already exists,
     *              the method will attempt to override it based on internal logic.
     * @param task  the {@link Runnable} task to be executed. Must not be {@code null}.
     * @param delay the delay in times before the task is executed.
     * @return {@code true} if the task was successfully scheduled.
     * Returns {@code false} if a task with the same id exists and overriding is not permitted.
     */
    public boolean scheduleTask(final long id, @NotNull final Runnable task, final TimeHolder delay) {
        return this.scheduleTask(id, task, delay, true);
    }

    /**
     * Schedules a one-time task to be executed after a specified delay.
     *
     * @param id       the unique identifier for the task. If a task with the same id already exists
     *                 and {@code override} is {@code false}, the method will return {@code false}.
     *                 If {@code override} is {@code true}, the existing task will be canceled and replaced.
     * @param task     the {@link Runnable} task to be executed. Must not be {@code null}.
     * @param delay    the delay in times before the task is executed.
     * @param override indicates whether an existing task with the same id should be overridden.
     * @return {@code true} if the task was successfully scheduled, or if an existing task was overridden.
     * Returns {@code false} if a task with the same id exists and {@code override} is {@code false}.
     */
    public boolean scheduleTask(final long id, @NotNull final Runnable task, final TimeHolder delay, final boolean override) {
        final Runnable timerTask = () -> {
            try {
                task.run();
            } catch (final Throwable e) {
                synchronized (Scheduler.this) {
                    if (Scheduler.this.throwableConsumer != null) {
                        Scheduler.this.throwableConsumer.accept(e);
                    }
                }
            }
        };

        synchronized (this) {
            if (this.tasks.containsKey(id)) {
                if (!override) {
                    if (!this.tasks.get(id).isDone() && !this.tasks.get(id).isCancelled()) {
                        return false;
                    }
                }
                this.cancelTask(id);
            }

            this.tasks.put(id,
                    this.timer.schedule(timerTask, delay.toConvert(TimeUnit.NANOSECONDS), TimeUnit.NANOSECONDS));
        }
        return true;
    }

    /**
     * Schedules a recurring task to be executed at a fixed interval after an initial delay.
     * The task will continue execution at the specified interval unless explicitly canceled.
     * If a task with the same identifier already exists,
     * the method may override it based on internal logic.
     *
     * @param id       the unique identifier for the task.
     *                 If a task with the same id already exists,
     *                 the behavior depends on the internal logic for handling task conflicts.
     * @param task     the {@link Runnable} task to be executed. Must not be {@code null}.
     * @param delay    the delay in times before the task is first executed.
     * @param interval the interval in times between successive executions of the task.
     * @return {@code true} if the task was successfully scheduled.
     * Returns {@code false} if scheduling was unsuccessful,
     * such as when internal conditions for task replacement were not met.
     */
    public boolean scheduleLoopingTask(final long id, @NotNull final Runnable task, final TimeHolder delay, final TimeHolder interval) {
        return this.scheduleLoopingTask(id, task, delay, interval, true);
    }

    /**
     * Schedules a recurring task to be executed at a fixed interval after an initial delay.
     * If a task with the specified id already exists, the behavior depends on the value of the {@code override} parameter.
     *
     * @param id       the unique identifier for the task.
     *                 If a task with the same id already exists
     *                 and {@code override} is {@code false}, the method will return {@code false}.
     *                 If {@code override} is {@code true}, the existing task will be canceled and replaced.
     * @param task     the {@link Runnable} task to be executed. Must not be {@code null}.
     * @param delay    the delay in times before the task is first executed.
     * @param interval the interval in times between successive executions of the task.
     * @param override indicates whether an existing task with the same id should be overridden.
     * @return {@code true} if the task was successfully scheduled, or if an existing task was overridden.
     * Returns {@code false} if a task with the same id exists and {@code override} is {@code false}.
     */
    public boolean scheduleLoopingTask(final long id, @NotNull final Runnable task, final TimeHolder delay, final TimeHolder interval, final boolean override) {
        final Runnable timerTask = () -> {
            try {
                task.run();
            } catch (final Throwable e) {
                synchronized (Scheduler.this) {
                    if (Scheduler.this.throwableConsumer != null) {
                        Scheduler.this.throwableConsumer.accept(e);
                    }
                }
            }
        };


        synchronized (this) {
            if (this.tasks.containsKey(id)) {
                if (!override) {
                    return false;
                }
                this.cancelTask(id);
            }

            this.tasks.put(id,
                    this.timer.scheduleAtFixedRate(timerTask,
                            delay.toConvert(TimeUnit.NANOSECONDS), interval.toConvert(TimeUnit.NANOSECONDS), TimeUnit.NANOSECONDS));
        }
        return true;
    }

    /**
     * Terminates the scheduler by canceling all scheduled tasks and releasing resources used by the internal timer.
     * This method should be called to gracefully shut down the Scheduler instance when it is no longer necessary.
     * Once this method is called, the scheduler is no longer operational, and no further tasks can be scheduled.
     */
    @Override
    @SneakyThrows
    public void close() {
        this.timer.shutdown();
        if (this.timer.awaitTermination(10, TimeUnit.SECONDS)) {
            this.timer.shutdownNow();
        }
    }


    /**
     * Retrieves an array of unique identifiers for all currently scheduled tasks.
     *
     * @return an array of task identifiers, where each entry represents the unique ID
     * of a task managed by the scheduler. If no tasks are scheduled, an empty
     * array is returned.
     */
    public long[] getAllTasks() {
        return this.tasks.keySet().stream().mapToLong(id -> id).toArray();
    }
}
