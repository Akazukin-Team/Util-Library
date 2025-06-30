package org.akazukin.util.utils.time.scheduler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.util.object.TimeHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Scheduler is a thread-safe utility class for managing and scheduling tasks
 * with support for one-time and recurring task execution.
 * Tasks are uniquely identified by an ID and can be managed through
 * various scheduling and cancellation methods. It provides mechanisms
 * to handle exceptions during task execution via a configurable consumer.
 * The Scheduler also supports graceful shutdown to manage lifecycle effectively.
 * <p>
 * The class is thread-safe and can be used concurrently
 * by multiple threads without the need for synchronization.
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@ThreadSafe
public abstract class AScheduler<T> implements IScheduler {
    final Map<Long, T> tasks = new HashMap<>();
    @Nullable
    Consumer<Throwable> throwableConsumer;

    @Override
    public void setThrowableConsumer(final @Nullable Consumer<Throwable> throwableConsumer) {
        this.throwableConsumer = throwableConsumer;
    }

    @Override
    public boolean isScheduled(final long id) {
        return this.tasks.containsKey(id);
    }

    /**
     * Cancels all currently scheduled tasks in the scheduler.
     */
    @Override
    public synchronized void cancelAllTasks() {
        this.tasks.values().forEach(this::cancelInternal);
        this.tasks.clear();
    }

    @Override
    public boolean scheduleTask(final long id, @NotNull final Runnable task, final TimeHolder delay) {
        return this.scheduleTask(id, task, delay, true);
    }

    @Override
    public boolean scheduleTask(final long id, @NotNull final Runnable task, final TimeHolder delay, final boolean override) {
        final Runnable timerTask = () -> {
            try {
                task.run();
            } catch (final Throwable e) {
                synchronized (this) {
                    if (this.throwableConsumer != null) {
                        this.throwableConsumer.accept(e);
                    }
                }
            }
            synchronized (this) {
                if (this.tasks.get(id) == this) {
                    this.tasks.remove(id);
                }
            }
        };

        synchronized (this) {
            if (this.tasks.containsKey(id)) {
                if (!override) {
                    if (this.isScheduled(id)) {
                        return false;
                    }
                }
                this.cancelTask(id);
            }

            this.tasks.put(id, this.scheduleInternal(timerTask, delay));
        }
        return true;
    }

    @Override
    public synchronized void cancelTask(final long id) {
        final T task = this.tasks.remove(id);
        if (task == null) {
            throw new IllegalArgumentException("Task not found: " + id);
        }
        this.cancelInternal(task);
    }

    @Override
    public boolean scheduleLoopingTask(final long id, @NotNull final Runnable task, final TimeHolder delay, final TimeHolder interval) {
        return this.scheduleLoopingTask(id, task, delay, interval, true);
    }

    @Override
    public boolean scheduleLoopingTask(final long id, @NotNull final Runnable task, final TimeHolder delay, final TimeHolder interval, final boolean override) {
        final Runnable timerTask = () -> {
            try {
                task.run();
            } catch (final Throwable e) {
                synchronized (this) {
                    if (this.throwableConsumer != null) {
                        this.throwableConsumer.accept(e);
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

            this.tasks.put(id, this.scheduleLoopInternal(timerTask, delay, interval));
        }
        return true;
    }

    @Override
    public synchronized long[] getAllScheduledTasks() {
        return this.tasks.keySet().stream()
                .mapToLong(id -> id)
                .filter(this::isScheduled)
                .toArray();
    }

    protected abstract T scheduleLoopInternal(Runnable task, TimeHolder delay, TimeHolder interval);

    protected abstract T scheduleInternal(Runnable task, TimeHolder delay);

    protected abstract void cancelInternal(T task);
}
