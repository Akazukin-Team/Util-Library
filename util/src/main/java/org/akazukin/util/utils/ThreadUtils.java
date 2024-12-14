package org.akazukin.util.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ThreadUtils {
    public static void parallel(final Runnable parallelStream, final int threads) {
        final ExecutorService pool = Executors.newWorkStealingPool(threads);
        try {
            pool.submit(parallelStream).get();
            pool.shutdown();
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
        pool.shutdownNow();
    }
}
