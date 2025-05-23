package org.akazukin.util.concurrent;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.ThreadSafe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * A synchronization utility that allows one or more threads to
 * wait until a specified count reaches zero.
 * Similar to {@link java.util.concurrent.CountDownLatch},
 * this class can be used to block threads until certain operations are completed.
 *
 * <p>
 * This class is built using an internal synchronization mechanism (`Sync`),
 * which extends {@link AbstractQueuedSynchronizer} to manage the state of the latch.
 * The latch is initialized with a non-negative count,
 * which can be decremented using the {@link CountLatch#release()} method.
 * Threads can block until the count reaches zero by calling {@link CountLatch#await()}.
 *
 * <p>
 * Once the count reaches zero, the latch does not reset and all later invocations of
 * {@link CountLatch#await()} return immediately.
 */
@ThreadSafe
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class CountLatch {
    final Sync sync;

    /**
     * Constructs a CountLatch with the given initial count.
     * The count must be a non-negative value.
     * Threads can use the latch to wait until the count reaches zero.
     *
     * @param count the initial count for the latch.
     *              Must be greater than or equal to zero.
     *              If the count is less than zero, an {@link IllegalArgumentException} is thrown.
     * @throws IllegalArgumentException if the count is less than zero.
     */
    public CountLatch(final int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count < 0");
        } else {
            this.sync = new Sync(count);
        }
    }

    /**
     * Causes the calling thread to wait until the latch count reaches zero,
     * unless the thread is interrupted.
     * The thread will remain blocked while the count is greater than zero.
     * If the count is already zero, this method returns immediately.
     * <p>
     * This method is interruptible.
     * If the calling thread is interrupted while waiting,
     * an {@link InterruptedException} is thrown, and the thread's interrupted status is cleared.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public void await() throws InterruptedException {
        this.sync.acquireSharedInterruptibly(1);
    }

    /**
     * Causes the calling thread to wait until the latch count reaches zero or the specified
     * waiting time elapses, unless the thread is interrupted.
     * The thread will remain blocked while the count is greater than zero
     * and the specified waiting time has not elapsed.
     * <p>
     * If the count reaches zero before the timeout elapses, this method returns {@code true}.
     * If the timeout elapses while waiting, this method returns {@code false}.
     * In either case, if the thread is interrupted while waiting,
     * an {@code InterruptedException} is thrown, and the thread's interrupted status is cleared.
     *
     * @param timeout the maximum time to wait, in the given time unit
     * @param unit    the time unit of the {@code timeout} parameter
     * @return {@code true} if the count reached zero within the waiting time;
     * {@code false} if the waiting time elapsed before the count reached zero
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public boolean await(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    /**
     * Decrements the count of the latch by releasing one shared permit.
     * <p>
     * If the count reaches zero as a result of this call, any threads
     * waiting on the latch are released.
     * If the count is already zero, later calls to this method have no effect.
     */
    public void release() {
        this.sync.releaseShared(1);
    }

    /**
     * Returns the current count of the latch.
     * <p>
     * The count represents the number of decrements required for the latch
     * to reach zero, at which point any threads waiting on the latch are released.
     *
     * @return the current count of the latch
     */
    public long getCount() {
        return this.sync.getCount();
    }

    @Override
    public String toString() {
        return super.toString() + "[Count = " + this.sync.getCount() + "]";
    }

    /**
     * A synchronization utility class used internally to manage the state of a CountLatch.
     * This class extends {@link AbstractQueuedSynchronizer} to implement a custom latch mechanism.
     * The primary purpose of this class is to manage a shared count and control thread access
     * based on this count.
     */
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1671835981426064175L;

        /**
         * Default constructor for the {@link Sync} class.
         * Initializes the latch with a count of zero.
         * <p>
         * This constructor delegates to the parameterized constructor,
         * setting the initial count to zero by default.
         */
        Sync() {
            this(0);
        }

        /**
         * Constructs a new instance of the Sync class with the specified initial count.
         * Sets the state to the provided count value,
         * which represents the initially shared count for the latch.
         * This count determines the number of decrements required to release waiting threads.
         *
         * @param count the initial count to set. This value should be greater than or equal to zero.
         *              If the count is less than zero, the behavior is undefined.
         */
        Sync(final int count) {
            this.setState(count);
        }

        /**
         * Retrieves the current internal state value representing the count.
         *
         * @return the current state value, which typically represents the count of the latch
         */
        int getCount() {
            return this.getState();
        }

        /**
         * Attempts to acquire the shared lock for the given argument.
         * The shared acquisition is successful only if the current state is zero.
         *
         * @param state the argument passed to the acquisition method (unused in this implementation)
         * @return 1 if the shared acquisition is successful (state is zero),
         * -1 if the acquisition fails (state is not zero)
         */
        @Override
        protected int tryAcquireShared(final int state) {
            return this.getState() == 0 ? 1 : -1;
        }

        /**
         * Attempts to release a shared resource by decrementing the current state value.
         * If the decrement operation sets the state to zero, the shared resource is considered fully released.
         * <p>
         * This method is used in scenarios where a shared lock or latch needs to manage
         * decrements to its counter and determine if the resource can be fully released.
         *
         * @param var1 the decrement amount (unused in this implementation but required by the method signature)
         * @return {@code true} if the state reaches zero after the decrement,
         * indicating the shared resource has been fully released; {@code false} otherwise
         */
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