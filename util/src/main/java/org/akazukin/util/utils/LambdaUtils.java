package org.akazukin.util.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

@UtilityClass
public class LambdaUtils {
    public <T> Collection<Callable<Void>> toCallable(final Collection<T> collection, final Consumer<T> lambda) {
        @SuppressWarnings("unchecked") final Callable<Void>[] tasks = new Callable[collection.size()];
        int i = 0;
        for (final T t1 : collection) {
            tasks[i++] = () -> {
                lambda.accept(t1);
                return null;
            };
        }
        return Arrays.asList(tasks);
    }

    public <T, R> Collection<Callable<R>> toCallable(final Collection<T> collection, final Function<T, R> lambda) {
        @SuppressWarnings("unchecked") final Callable<R>[] tasks = new Callable[collection.size()];
        int i = 0;
        for (final T t1 : collection) {
            tasks[i++] = () -> lambda.apply(t1);
        }
        return Arrays.asList(tasks);
    }
}
