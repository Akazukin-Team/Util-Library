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

    @Test
    public void lend_single_shared_lock_to_exclusive() throws Exception {
        final FixedReentrantReadWriteLock locker = new FixedReentrantReadWriteLock();

        // Thread 1: acquire shared lock and set lendable
        final CompletableFuture<Boolean> sharedFuture = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.sharedLock()) {
                assertEquals(1, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.SHARED, locker.getLockType());

                if (lock instanceof FixedReentrantReadWriteLock.Lock) {
                    final FixedReentrantReadWriteLock.Lock castLock =
                            (FixedReentrantReadWriteLock.Lock) lock;
                    castLock.setLendable(true);
                }

                try {
                    Thread.sleep(300);
                } catch (final InterruptedException ignored) {
                }
                return true;
            }
        });

        // Give shared lock holder a moment
        Thread.sleep(50);

        // Thread 2: try to acquire exclusive lock (should borrow the shared lock)
        final CompletableFuture<Boolean> exclusiveFuture = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.exclusiveLock()) {
                // While exclusive is held, lock count should decrease because shared was borrowed
                assertEquals(1, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.EXCLUSIVE, locker.getLockType());
                return true;
            }
        });

        assertTrue(exclusiveFuture.get(1, TimeUnit.SECONDS));
        assertTrue(sharedFuture.get(1, TimeUnit.SECONDS));
        assertEquals(0, locker.getLockCount());
    }

    @Test
    public void lend_multiple_shared_locks_to_exclusive() throws Exception {
        final FixedReentrantReadWriteLock locker = new FixedReentrantReadWriteLock();

        // Thread 1: acquire first shared lock and set lendable
        final CompletableFuture<Boolean> sharedFuture1 = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.sharedLock()) {
                if (lock instanceof FixedReentrantReadWriteLock.Lock) {
                    final FixedReentrantReadWriteLock.Lock castLock =
                            (FixedReentrantReadWriteLock.Lock) lock;
                    castLock.setLendable(true);
                }

                try {
                    Thread.sleep(300);
                } catch (final InterruptedException ignored) {
                }
                return true;
            }
        });

        Thread.sleep(50);

        // Thread 2: acquire second shared lock and set lendable
        final CompletableFuture<Boolean> sharedFuture2 = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.sharedLock()) {
                if (lock instanceof FixedReentrantReadWriteLock.Lock) {
                    final FixedReentrantReadWriteLock.Lock castLock =
                            (FixedReentrantReadWriteLock.Lock) lock;
                    castLock.setLendable(true);
                }

                try {
                    Thread.sleep(300);
                } catch (final InterruptedException ignored) {
                }
                return true;
            }
        });

        Thread.sleep(50);

        // Both shared locks should now be held
        assertEquals(2, locker.getLockCount());

        // Thread 3: acquire exclusive lock (should borrow both shared locks)
        final CompletableFuture<Boolean> exclusiveFuture = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.exclusiveLock()) {
                // Lock count should be 1 (only the exclusive lock)
                assertEquals(1, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.EXCLUSIVE, locker.getLockType());
                return true;
            }
        });

        assertTrue(exclusiveFuture.get(1, TimeUnit.SECONDS));
        assertTrue(sharedFuture1.get(1, TimeUnit.SECONDS));
        assertTrue(sharedFuture2.get(1, TimeUnit.SECONDS));
        assertEquals(0, locker.getLockCount());
    }

    @Test
    public void lend_single_shared_with_mix_lendable_and_non_lendable() throws Exception {
        final FixedReentrantReadWriteLock locker = new FixedReentrantReadWriteLock();

        // Thread 1: acquire non-lendable shared lock
        final CompletableFuture<Boolean> nonLendableFuture = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.sharedLock()) {
                // This lock is non-lendable by default
                try {
                    Thread.sleep(200);
                } catch (final InterruptedException ignored) {
                }
                return true;
            }
        });

        Thread.sleep(50);

        // Thread 2: try to acquire exclusive lock (should block because non-lendable shared exists)
        final CompletableFuture<Boolean> exclusiveFuture = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.exclusiveLock()) {
                assertEquals(1, locker.getLockCount());
                assertEquals(FixedReentrantReadWriteLock.LockType.EXCLUSIVE, locker.getLockType());
                return true;
            }
        });

        // Exclusive should be blocked initially
        try {
            exclusiveFuture.get(100, TimeUnit.MILLISECONDS);
            fail("exclusive should have been blocked by non-lendable shared lock");
        } catch (final Exception ignored) {
            // expected
        }

        // After non-lendable shared is released, exclusive should proceed
        assertTrue(nonLendableFuture.get(1, TimeUnit.SECONDS));
        assertTrue(exclusiveFuture.get(1, TimeUnit.SECONDS));
        assertEquals(0, locker.getLockCount());
    }

    @Test
    public void lent_locks_are_restored_after_exclusive_release() throws Exception {
        final FixedReentrantReadWriteLock locker = new FixedReentrantReadWriteLock();

        final java.util.concurrent.atomic.AtomicInteger lockCountAfterExclusiveRelease =
                new java.util.concurrent.atomic.AtomicInteger(-1);

        // Thread 1: acquire shared lock and set lendable
        final CompletableFuture<Boolean> sharedFuture = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.sharedLock()) {
                if (lock instanceof FixedReentrantReadWriteLock.Lock) {
                    final FixedReentrantReadWriteLock.Lock castLock =
                            (FixedReentrantReadWriteLock.Lock) lock;
                    castLock.setLendable(true);
                }

                try {
                    Thread.sleep(150);
                } catch (final InterruptedException ignored) {
                }

                // Check lock count after exclusive is released
                lockCountAfterExclusiveRelease.set(locker.getLockCount());
                return true;
            }
        });

        Thread.sleep(50);

        // Thread 2: acquire exclusive lock (should borrow the shared lock)
        final CompletableFuture<Boolean> exclusiveFuture = CompletableFuture.supplyAsync(() -> {
            try (final FixedReentrantReadWriteLock.ILock lock = locker.exclusiveLock()) {
                assertEquals(1, locker.getLockCount());
                return true;
            }
        });

        assertTrue(exclusiveFuture.get(1, TimeUnit.SECONDS));
        assertTrue(sharedFuture.get(1, TimeUnit.SECONDS));

        // The lock count after exclusive release should be 1 again (shared lock restored)
        assertEquals(1, lockCountAfterExclusiveRelease.get());
        assertEquals(0, locker.getLockCount());
    }
}
