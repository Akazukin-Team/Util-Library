package org.akazukin.util.utils;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Scheduler implements Closeable {
    final Timer timer;

    final Map<Long, TimerTask> tasks = new ConcurrentHashMap<>();

    @Setter
    Consumer<Throwable> throwableConsumer;

    public Scheduler() {
        this(false);
    }

    public Scheduler(final boolean daemon) {
        this.timer = new Timer(daemon);
    }

    public boolean isScheduled(final long id) {
        return this.tasks.containsKey(id);
    }

    public void cancelAllTasks() {
        synchronized (this.tasks) {
            this.tasks.keySet().forEach(this::cancel);
            this.tasks.clear();
        }
    }

    public void cancel(final long id) {
        synchronized (this.tasks) {
            final TimerTask task = this.tasks.get(id);
            if (task == null) {
                throw new IllegalArgumentException("Task not found: " + id);
            }
            task.cancel();
            this.tasks.remove(id);
        }
    }

    public void scheduleTask(final long id, @NotNull final Runnable task, final long delay) {
        final TimerTask timerTask = this.createTimerTask(task);
        this.timer.schedule(timerTask, delay);

        synchronized (this.tasks) {
            if (this.tasks.containsKey(id)) {
                this.cancel(id);
            }
            this.tasks.put(id, timerTask);
        }
    }

    private TimerTask createTimerTask(@NotNull final Runnable task) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (final Throwable e) {
                    if (Scheduler.this.throwableConsumer != null) {
                        Scheduler.this.throwableConsumer.accept(e);
                    }
                }
            }
        };
    }

    public void scheduleLoopingTask(final long id, @NotNull final Runnable task, final long delay, final long interval) {
        final TimerTask timerTask = this.createTimerTask(task);
        this.timer.schedule(timerTask, delay, interval);

        synchronized (this.tasks) {
            if (this.tasks.containsKey(id)) {
                this.cancel(id);
            }
            this.tasks.put(id, timerTask);
        }
    }

    @Override
    public void close() {
        this.timer.cancel();
    }


    public long[] getAllTasks() {
        return this.tasks.keySet().stream().mapToLong(id -> id).toArray();
    }
}
