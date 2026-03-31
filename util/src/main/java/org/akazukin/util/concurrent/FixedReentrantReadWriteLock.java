package org.akazukin.util.concurrent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FixedReentrantReadWriteLock {
    private final ReentrantLock mutex = new ReentrantLock();
    private final Condition condition = this.mutex.newCondition();
    private final Collection<LockRequest> lockRequests = new LinkedHashSet<>();
    private final Collection<Lock> locks = new HashSet<>();

    public ILock exclusiveLock() {
        return this.lock(false);
    }

    private Lock lock(final boolean shared) {
        final Thread owner = Thread.currentThread();
        final LockRequest req = new LockRequest(owner, shared);
        this.lockRequests.add(req);

        this.mutex.lock();
        try {
            while (!this.canLock(req)) {
                this.condition.await();
            }

            final Lock lock = new Lock(this, req);
            this.locks.add(lock);
            this.lockRequests.remove(req);
            return lock;
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.mutex.unlock();
        }
    }

    private boolean canLock(final LockRequest req) {
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
            return hasSelfLock || this.locks.isEmpty();
        }
    }

    public LockType getLockType() {
        return this.getLockType(Thread.currentThread());
    }

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

    public ILock sharedLock() {
        return this.lock(true);
    }

    public int getLockCount() {
        return this.locks.size();
    }

    public void unlock(final ILock lock) {
        this.mutex.lock();
        try {
            this.locks.remove(lock);
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
    }

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    private static class LockRequest {
        Thread owner;
        boolean shared;

        LockRequest(final Thread owner, final boolean shared) {
            this.owner = owner;
            this.shared = shared;
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    private static class Lock implements ILock {
        FixedReentrantReadWriteLock lock;
        LockRequest request;

        public Lock(final FixedReentrantReadWriteLock lock, final LockRequest request) {
            this.lock = lock;
            this.request = request;
        }

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
