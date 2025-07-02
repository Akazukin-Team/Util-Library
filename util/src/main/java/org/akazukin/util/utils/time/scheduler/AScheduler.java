package org.akazukin.util.utils.time.scheduler;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;
import org.akazukin.util.object.Pair;
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
    final Map<Long, Pair<Object, T>> tasks = new HashMap<>();
    @Nullable
    @Setter
    Consumer<Throwable> throwableConsumer;

    @Override
    public boolean isScheduled(final long id) {
        return this.tasks.containsKey(id);
    }

    /**
     * Cancels all currently scheduled tasks in the scheduler.
     */
    @Override
    public synchronized void cancelAllTasks() {
        this.tasks.values()
                .forEach(p -> this.cancelInternal(p.getValue()));
        this.tasks.clear();
    }

    @Override
    public boolean scheduleTask(final long id, @NotNull final Runnable task, final TimeHolder delay) {
        return this.scheduleTask(id, task, delay, true);
    }

    @Override
    public boolean scheduleTask(final long id, @NotNull final Runnable task, final TimeHolder delay, final boolean override) {
        final Object obj = new Object();
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
                if (this.tasks.get(id).getKey() == obj) {
                    this.tasks.remove(id);
                }
            }
        };

        synchronized (this) {
            if (override) {
                final Pair<Object, T> e = this.tasks.remove(id);
                if (e != null) {
                    this.cancelInternal(e.getValue());
                }
            } else {
                if (this.tasks.containsKey(id)) {
                    return false;
                }
            }

            this.tasks.put(id, this.createPairScheduleInternal(obj, timerTask, delay));
        }
        return true;
    }

    @Override
    public synchronized void cancelTask(final long id) {
        final Pair<Object, T> task = this.tasks.remove(id);
        if (task == null) {
            throw new IllegalArgumentException("Task not found: " + id);
        }
        this.cancelInternal(task.getValue());
    }

    @Override
    public boolean scheduleLoopingTask(final long id, @NotNull final Runnable task, final TimeHolder delay, final TimeHolder interval) {
        return this.scheduleLoopingTask(id, task, delay, interval, true);
    }

    @Override
    public boolean scheduleLoopingTask(final long id, @NotNull final Runnable task, final TimeHolder delay, final TimeHolder interval, final boolean override) {
        final Object obj = new Object();
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
            if (override) {
                final Pair<Object, T> e = this.tasks.remove(id);
                if (e != null) {
                    this.cancelInternal(e.getValue());
                }
            } else {
                if (this.tasks.containsKey(id)) {
                    return false;
                }
            }

            this.tasks.put(id, this.createPairScheduleLoopInternal(obj, timerTask, delay, interval));
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

    @NotNull
    private Pair<Object, T> createPairScheduleLoopInternal(final Object obj, final Runnable task, final TimeHolder delay, final TimeHolder interval) {
        return new Pair<>(obj, this.scheduleLoopInternal(task, delay, interval));
    }

    protected abstract T scheduleLoopInternal(Runnable task, TimeHolder delay, TimeHolder interval);

    @NotNull
    private Pair<Object, T> createPairScheduleInternal(final Object obj, final Runnable task, final TimeHolder delay) {
        return new Pair<>(obj, this.scheduleInternal(task, delay));
    }

    protected abstract T scheduleInternal(Runnable task, TimeHolder delay);

    protected abstract void cancelInternal(T task);
}
