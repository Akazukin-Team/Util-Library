package org.akazukin.util.utils.time.scheduler;

import org.akazukin.util.object.TimeHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.util.function.Consumer;

public interface IScheduler extends Closeable {
    /**
     * Sets a consumer to handle {@link Throwable} instances encountered during scheduled task execution.
     * If a consumer is provided, it will be invoked whenever a {@link Throwable} is thrown by a task.
     *
     * @param throwableConsumer a {@link Consumer} to process {@link Throwable} instances. It may be {@code null},
     *                          in which case no actions will be taken when a {@link Throwable} is encountered.
     */
    void setThrowableConsumer(@Nullable Consumer<Throwable> throwableConsumer);

    /**
     * Checks if a task with the specified identifier is currently scheduled.
     *
     * @param id the unique identifier of the task to check.
     * @return {@code true} if a task with the specified identifier is scheduled,
     * {@code false} otherwise.
     */
    boolean isScheduled(long id);

    void cancelAllTasks();

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
    boolean scheduleTask(long id, @NotNull Runnable task, TimeHolder delay);

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
    boolean scheduleTask(long id, @NotNull Runnable task, TimeHolder delay, boolean override);

    /**
     * Cancels a scheduled task with the specified unique identifier.
     * If the task is not found, an {@link IllegalArgumentException} is thrown.
     *
     * @param id the unique identifier of the task to be canceled.
     *           It must match the identifier of a previously scheduled task.
     * @throws IllegalArgumentException if a task with the specified identifier is not found.
     */
    void cancelTask(long id);

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
    boolean scheduleLoopingTask(long id, @NotNull Runnable task, TimeHolder delay, TimeHolder interval);

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
    boolean scheduleLoopingTask(long id, @NotNull Runnable task, TimeHolder delay, TimeHolder interval, boolean override);

    /**
     * Cancels all currently scheduled tasks in the scheduler.
     * Closes the scheduler and releases any resources it holds.
     */
    @Override
    void close();

    /**
     * Retrieves an array of unique identifiers for all currently scheduled tasks.
     *
     * @return an array of task identifiers, where each entry represents the unique ID
     * of a task managed by the scheduler. If no tasks are scheduled, an empty
     * array is returned.
     */
    long[] getAllScheduledTasks();
}
