package org.akazukin.util.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A utility class providing methods for lambda related
 */
@UtilityClass
public class LambdaUtils {
    /**
     * Converts a collection of elements into a collection of {@link Callable} tasks.
     * Each task applies the provided {@link Consumer} to an element from the input collection
     * and returns null upon completion.
     *
     * @param <T>        the type of elements in the input collection
     * @param collection the input collection containing elements to process
     * @param lambda     the {@link Consumer} to apply to each element in the collection
     * @return a collection of {@link Callable} tasks, where each task processes an element from the input collection
     */
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

    /**
     * Converts a collection of elements into a collection of {@link Callable} tasks.
     * Each task processes an element from the input collection using the provided {@link Function}
     * and returns the result of applying the function.
     *
     * @param <T>        the type of elements in the input collection
     * @param <R>        the type of the results produced by the callables
     * @param collection the input collection containing elements to process
     * @param lambda     the {@link Function} to apply to each element in the collection
     * @return a collection of {@link Callable} tasks, where each task processes an element from
     * the input collection and returns the corresponding result
     */
    public <T, R> Collection<Callable<R>> toCallable(final Collection<T> collection, final Function<T, R> lambda) {
        @SuppressWarnings("unchecked") final Callable<R>[] tasks = new Callable[collection.size()];
        int i = 0;
        for (final T t1 : collection) {
            tasks[i++] = () -> lambda.apply(t1);
        }
        return Arrays.asList(tasks);
    }
}
