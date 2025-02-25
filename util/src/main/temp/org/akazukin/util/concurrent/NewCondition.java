package org.akazukin.util.concurrent;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewCondition extends AbstractQueuedSynchronizer.ConditionObject {
    @Serial
    private static final long serialVersionUID = -2916167129316198251L;
    Condition condition;
    boolean active;

    @Override
    public void signal() {
        this.condition.signal();
    }

    @Override
    public void signalAll() {
        this.condition.signalAll();
    }

    @Override
    public void awaitUninterruptibly() {
        this.condition.awaitUninterruptibly();
    }

    @Override
    public void await() throws InterruptedException {
        this.condition.await();
    }

    @Override
    public long awaitNanos(final long nanosTimeout) throws InterruptedException {
        return this.awaitNanos(nanosTimeout);
    }

    @Override
    public boolean awaitUntil(@NotNull final Date deadline) throws InterruptedException {
        return this.awaitUntil(deadline);
    }

    @Override
    public boolean await(final long time, final TimeUnit unit) throws InterruptedException {
        return this.await(time, unit);
    }

    public void tryAwait() {
    }
}
