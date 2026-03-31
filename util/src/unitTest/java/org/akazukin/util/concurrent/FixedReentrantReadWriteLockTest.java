package org.akazukin.util.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class FixedReentrantReadWriteLockTest {
    @Test
    public void exclusive_reentrant_acquire_and_release() throws Exception {
        final FixedReentrantReadWriteLock locker = new FixedReentrantReadWriteLock();

        final CompletableFuture<Boolean> fut = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.exclusiveLock()) {
                assertEquals(1, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.EXCLUSIVE, locker.getLockType());

                try (final FixedReentrantReadWriteLock.ILock lock2 = locker.exclusiveLock()) {
                    assertEquals(2, locker.getLockCount());
                    assertEquals(FixedReentrantReadWriteLock.LockType.EXCLUSIVE, locker.getLockType());
                    return true;
                }
            }
        });

        assertTrue(fut.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void multiple_shared_acquire_concurrently() throws Exception {
        final FixedReentrantReadWriteLock locker = new FixedReentrantReadWriteLock();

        final CompletableFuture<Boolean> f = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.sharedLock()) {
                assertEquals(1, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.SHARED, locker.getLockType());

                try {
                    Thread.sleep(150);
                } catch (final InterruptedException ignored) {
                }
                return true;
            }
        });

        Thread.sleep(50);

        final CompletableFuture<Boolean> f2 = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.sharedLock()) {
                assertEquals(2, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.SHARED, locker.getLockType());
                return true;
            }
        });

        assertTrue(f.get(1, TimeUnit.SECONDS));
        assertTrue(f2.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void multiple_exclusive_acquire_concurrently() throws Exception {
        final FixedReentrantReadWriteLock locker = new FixedReentrantReadWriteLock();

        final CompletableFuture<Boolean> f = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.exclusiveLock()) {
                assertEquals(1, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.EXCLUSIVE, locker.getLockType());

                try {
                    Thread.sleep(150);
                } catch (final InterruptedException ignored) {
                }
                return true;
            }
        });

        Thread.sleep(50);

        final CompletableFuture<Boolean> f2 = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.exclusiveLock()) {
                assertEquals(1, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.EXCLUSIVE, locker.getLockType());
                return true;
            }
        });

        assertTrue(f.get(1, TimeUnit.SECONDS));
        assertTrue(f2.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void exclusive_blocks_shared_until_released() throws Exception {
        final FixedReentrantReadWriteLock locker = new FixedReentrantReadWriteLock();

        final CompletableFuture<Boolean> exclusiveFuture = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.exclusiveLock()) {
                assertEquals(1, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.EXCLUSIVE, locker.getLockType());

                try {
                    Thread.sleep(150);
                } catch (final InterruptedException ignored) {
                }
                return true;
            }
        });

        // give exclusive holder a moment to acquire
        Thread.sleep(50);
        assertEquals(1, locker.getLockCount());

        final CompletableFuture<Boolean> sharedFuture = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.sharedLock()) {
                assertEquals(1, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.SHARED, locker.getLockType());

                try {
                    Thread.sleep(150);
                } catch (final InterruptedException ignored) {
                }
                return true;
            }
        });

        // sharedAttempt should block while exclusive is held
        try {
            sharedFuture.get(150, TimeUnit.MILLISECONDS);
            fail("shared should have been blocked by exclusive lock");
        } catch (final Exception e) {
            // expected: timeout or similar
        }

        // after exclusive released, sharedAttempt should complete
        assertTrue(sharedFuture.get(1, TimeUnit.SECONDS));
        assertTrue(exclusiveFuture.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void lock_count() {
        final FixedReentrantReadWriteLock lock = new FixedReentrantReadWriteLock();
        assertEquals(0, lock.getLockCount());
        assertNull(lock.getLockType());
    }

    @Test
    public void promote_shared_to_exclusive_when_single_owner() throws Exception {
        final FixedReentrantReadWriteLock locker = new FixedReentrantReadWriteLock();

        final CompletableFuture<Boolean> fut = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.sharedLock()) {
                assertEquals(1, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.SHARED, locker.getLockType());

                try (final FixedReentrantReadWriteLock.ILock lock2 = locker.exclusiveLock()) {
                    assertEquals(2, locker.getLockCount());
                    assertEquals(FixedReentrantReadWriteLock.LockType.EXCLUSIVE, locker.getLockType());
                    return true;
                }
            }
        });

        assertTrue(fut.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void demote_exclusive_to_shared_in_same_thread() throws Exception {
        final FixedReentrantReadWriteLock locker = new FixedReentrantReadWriteLock();

        final CompletableFuture<Boolean> fut = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.exclusiveLock()) {
                assertEquals(1, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.EXCLUSIVE, locker.getLockType());

                try (final FixedReentrantReadWriteLock.ILock lock2 = locker.sharedLock()) {
                    assertEquals(2, locker.getLockCount());
                    assertEquals(FixedReentrantReadWriteLock.LockType.EXCLUSIVE, locker.getLockType());
                    return true;
                }
            }
        });

        assertTrue(fut.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void exclusive_single_acquire_release() throws Exception {
        final FixedReentrantReadWriteLock locker = new FixedReentrantReadWriteLock();

        final CompletableFuture<Boolean> fut = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.exclusiveLock()) {
                assertEquals(1, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.EXCLUSIVE, locker.getLockType());
                return true;
            }
        });

        assertTrue(fut.get(1, TimeUnit.SECONDS));
        assertEquals(0, locker.getLockCount());
    }

    @Test
    public void shared_single_acquire_release() throws Exception {
        final FixedReentrantReadWriteLock locker = new FixedReentrantReadWriteLock();

        final CompletableFuture<Boolean> fut = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.sharedLock()) {
                assertEquals(1, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.SHARED, locker.getLockType());
                return true;
            }
        });

        assertTrue(fut.get(1, TimeUnit.SECONDS));
        assertEquals(0, locker.getLockCount());
    }
}
