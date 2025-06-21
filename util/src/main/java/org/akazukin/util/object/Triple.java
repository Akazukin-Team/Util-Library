package org.akazukin.util.object;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.NonThreadSafe;

/**
 * Represents a generic pair of three elements.
 * <p>
 * This class is a simple container object that holds three related objects.
 * It is designed to be immutable and provides
 * convenience methods for object comparison and string representation.
 *
 * @param <F> the type of the first element.
 * @param <S> the type of the second element.
 * @param <T> the type of the third element.
 */
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@ToString
@EqualsAndHashCode
@NonThreadSafe
public final class Triple<F, S, T> {
    F first;
    S second;
    T third;
}
