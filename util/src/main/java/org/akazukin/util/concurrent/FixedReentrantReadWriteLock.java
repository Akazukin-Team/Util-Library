package org.akazukin.util.concurrent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FixedReentrantReadWriteLock {
    ReentrantLock mutex = new ReentrantLock();
    Condition condition = this.mutex.newCondition();
    Collection<LockRequest> lockRequests = new CopyOnWriteArrayList<>();
    Collection<Lock> locks = new HashSet<>();

    @NotNull
    public ILock exclusiveLock() {
        return this.lock(false);
    }

    @NotNull
    private Lock lock(final boolean shared) {
        final LockRequest req = new LockRequest(Thread.currentThread(), shared);
        this.lockRequests.add(req);

        this.mutex.lock();
        try {
            while (!this.canLock(req)) {
                this.condition.await();
            }

            final Lock lock = new Lock(this, req);
            this.locks.add(lock);
            this.lockRequests.remove(req);

            if (!shared) {
                final Collection<Lock> toBorrow = new HashSet<>();
                for (final Lock existingLock : this.locks) {
                    if (existingLock.isShared() && existingLock.isLendable()) {
                        lock.addLend(existingLock);
                        toBorrow.add(existingLock);
                    }
                }
                this.locks.removeAll(toBorrow);
            }

            return lock;
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.mutex.unlock();
        }
    }

    private boolean canLock(@NotNull final LockRequest req) {
        for (final LockRequest r : this.lockRequests) {
            if (r == req) {
                break;
            }
            if (this.locks.stream()
                    .noneMatch(lock -> lock.getOwner() == r.getOwner())) {
                return false;
            }
        }

        if (req.isShared()) {
            for (final Lock lock : this.locks) {
                if (!lock.isShared() && lock.getOwner() != req.getOwner()) {
                    return false;
                }
            }
            return true;
        } else {
            final boolean hasSelfLock = this.locks.stream()
                    .anyMatch(lock -> lock.getOwner() == req.getOwner());
            if (hasSelfLock) {
                return true;
            }
            if (!this.locks.isEmpty()) {
                for (final Lock lock : this.locks) {
                    if (!lock.isShared() || !lock.isLendable()) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    @Nullable
    public LockType getLockType() {
        return this.getLockType(Thread.currentThread());
    }

    @Nullable
    public LockType getLockType(final Thread thread) {
        LockType type = null;
        for (final Lock lock : this.locks) {
            if (lock.getOwner() == thread) {
                if (!lock.isShared()) {
                    return LockType.EXCLUSIVE;
                }
                type = LockType.SHARED;
            }
        }

        return type;
    }

    @NotNull
    public ILock sharedLock() {
        return this.lock(true);
    }

    public int getLockCount() {
        return this.locks.size();
    }

    public void unlock(@NotNull final ILock ilock) {
        this.mutex.lock();
        try {
            this.locks.remove(ilock);
            if (ilock instanceof final Lock lock) {
                final Lock[] lentLocks = lock.clearLends();
                Collections.addAll(this.locks, lentLocks);
            }
            this.condition.signalAll();
        } finally {
            this.mutex.unlock();
        }
    }

    public enum LockType {
        EXCLUSIVE, SHARED
    }

    public interface ILock extends AutoCloseable {
        @Override
        void close();

        void setLendable(boolean lendable);
    }

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    private static class LockRequest {
        @NotNull
        Thread owner;
        boolean shared;

        LockRequest(@NotNull final Thread owner, final boolean shared) {
            this.owner = owner;
            this.shared = shared;
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class Lock implements ILock {
        private static final Lock[] EMPTY_ARR = new Lock[0];

        final FixedReentrantReadWriteLock lock;
        final LockRequest request;
        final Set<Lock> lends = new HashSet<>();
        @Getter
        @Setter
        boolean lendable;

        private Lock(@NotNull final FixedReentrantReadWriteLock lock, @NotNull final LockRequest request) {
            this.lock = lock;
            this.request = request;
        }

        public synchronized void addLend(@NotNull final Lock lock) {
            this.lends.add(lock);
        }

        @NotNull
        public synchronized Lock[] clearLends() {
            final Lock[] res = this.lends.toArray(EMPTY_ARR);
            this.lends.clear();
            return res;
        }

        @NotNull
        public Thread getOwner() {
            return this.request.getOwner();
        }

        public boolean isShared() {
            return this.request.isShared();
        }

        @Override
        public void close() {
            this.lock.unlock(this);
        }
    }
}
