package org.akazukin.util.concurrent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
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

            if (!shared) {
                final Collection<Lock> toBorrow = new HashSet<>();
                for (final Lock existingLock : this.locks) {
                    if (existingLock != lock && existingLock.isShared() && existingLock.isLendable()) {
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

    public void unlock(final ILock ilock) {
        this.mutex.lock();
        try {
            this.locks.remove(ilock);
            if (ilock instanceof Lock) {
                final Lock lock = (Lock) ilock;
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
        Thread owner;
        boolean shared;

        LockRequest(final Thread owner, final boolean shared) {
            this.owner = owner;
            this.shared = shared;
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class Lock implements ILock {
        private static final Lock[] EMPTY_ARR = new Lock[0];

        final FixedReentrantReadWriteLock lock;
        final LockRequest request;
        final Set<Lock> lends = new HashSet<>();
        @Getter
        @Setter
        boolean lendable;

        public Lock(final FixedReentrantReadWriteLock lock, final LockRequest request) {
            this.lock = lock;
            this.request = request;
        }

        public synchronized void addLend(final Lock lock) {
            this.lends.add(lock);
        }

        public synchronized void setLends(final Lock lock) {
            this.lends.clear();
            this.lends.add(lock);
        }

        public synchronized Lock[] clearLends() {
            final Lock[] res = this.lends.toArray(EMPTY_ARR);
            this.lends.clear();
            return res;
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
