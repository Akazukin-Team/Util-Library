package org.akazukin.util.concurrent;

import java.io.Serial;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class NewCondition2 extends AbstractQueuedSynchronizer {
    @Serial
    private static final long serialVersionUID = -3265553307344212106L;

    public class Sync extends AbstractQueuedSynchronizer.ConditionObject {
        @Serial
        private static final long serialVersionUID = -264157820506115442L;
    }
}
