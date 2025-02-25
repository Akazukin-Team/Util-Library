package org.akazukin.util.concurrent;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class CountLatch {
    final Sync sync;

    public CountLatch(final int var1) {
        if (var1 < 0) {
            throw new IllegalArgumentException("count < 0");
        } else {
            this.sync = new Sync(var1);
        }
    }

    public void await() throws InterruptedException {
        this.sync.acquireSharedInterruptibly(1);
    }

    public boolean await(final long var1, final TimeUnit var3) throws InterruptedException {
        return this.sync.tryAcquireSharedNanos(1, var3.toNanos(var1));
    }

    public void release() {
        this.sync.releaseShared(1);
    }

    public long getCount() {
        return this.sync.getCount();
    }

    @Override
    public String toString() {
        return super.toString() + "[Count = " + this.sync.getCount() + "]";
    }

    public static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4982264981922014374L;

        Sync() {
            this(0);
        }

        Sync(final int var1) {
            this.setState(var1);
        }

        int getCount() {
            return this.getState();
        }

        @Override
        protected int tryAcquireShared(final int var1) {
            return this.getState() == 0 ? 1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(final int var1) {
            int var2;
            int var3;
            do {
                var2 = this.getState();
                if (var2 == 0) {
                    return false;
                }

                var3 = var2 - 1;
            } while (!this.compareAndSetState(var2, var3));

            return var3 == 0;
        }
    }
}