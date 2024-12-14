package org.akazukin.util.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class ListUtils {
    @SuppressWarnings("unchecked")
    public static <T> List<T> cast(@NonNull final List<?> list) {
        return list.stream().map(o -> (T) o).collect(Collectors.toList());
    }

    public static <T> List<List<T>> split(@NonNull final List<T> list, final int size) {
        return new ArrayList<>(
                IntStream.range(0, list.size())
                        .boxed()
                        .collect(
                                Collectors.groupingBy(
                                        index -> index / size,
                                        Collectors.mapping(list::get, Collectors.toList())
                                ))
                        .values());
    }

    @NonNull
    public static String join(@Nullable final String character, @NonNull final Collection<?> list) {
        return ListUtils.join(character, list.toArray());
    }

    @NonNull
    public static String join(@Nullable final String character, @NonNull final Object[] list) {
        return Arrays.stream(list)
                .map(String::valueOf)
                .collect(Collectors.joining(character != null ? character : ""));
    }

    @Nullable
    public static <T> T getByRandom(@NonNull final T[] list) {
        return list[((Double) (Math.random() * list.length)).intValue()];
    }

    @Nullable
    public static <T> T getByRandom(@NonNull final List<T> list) {
        return list.get(((Double) (Math.random() * list.size())).intValue());
    }

    public static <T> List<T> copy(@NonNull final List<T> array, final int index, final int length) {
        final List<T> list = new ArrayList<>();
        for (int i = index; list.size() <= length && i < array.size(); i++) {
            list.add(array.get(i));
        }
        return list;
    }
}
