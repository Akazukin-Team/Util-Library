package org.akazukin.util.object;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.akazukin.annotation.marker.NonThreadSafe;

/**
 * Represents a generic pair of key and value.
 * <p>
 * This class is a simple container object that holds two related objects:
 * a key and a value. It is designed to be immutable and provides
 * convenience methods for object comparison and string representation.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode
@NonThreadSafe
public class Pair<K, V> {
    K key;
    V value;
}
