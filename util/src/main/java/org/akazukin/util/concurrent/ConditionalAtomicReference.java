package org.akazukin.util.concurrent;

import lombok.Getter;
import org.akazukin.util.annotation.ThreadSafe;

/**
 * A thread-safe reference holder that allows updating its value conditionally based on a
 * predicate. The update operation is synchronized to ensure atomicity and thread safety.
 *
 * @param <T> the type of the value held by the reference
 */
@ThreadSafe
public class ConditionalAtomicReference<T> {
    final ValuePredicate<T> supplier;
    @Getter
    T value;

    /**
     * Constructs a new ConditionalAtomicReference instance with a specified predicate supplier
     * and an initial value.
     * The provided predicate determines the conditions under which the value can be updated atomically.
     *
     * @param supplier the predicate used to evaluate whether the reference can be updated;
     *                 it takes the current value and a proposed new value as input parameters
     * @param value    the initial value of the reference
     */
    public ConditionalAtomicReference(final ValuePredicate<T> supplier, final T value) {
        this.supplier = supplier;
        this.value = value;
    }

    /**
     * Constructs a new ConditionalAtomicReference instance with a specified predicate supplier.
     * The provided predicate is used to determine the conditions under which the value can be updated atomically.
     *
     * @param supplier the predicate used to evaluate whether the reference can be updated;
     *                 it takes the current value and a proposed new value as input parameters
     */
    public ConditionalAtomicReference(final ValuePredicate<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Updates the stored value if the specified condition, defined by the predicate, is met.
     * The operation is thread-safe and synchronized to ensure atomicity during the update.
     *
     * @param value the new value to update the reference to if the predicate condition is satisfied
     * @return {@code true} if the value was successfully updated, {@code false} otherwise
     */
    public boolean update(final T value) {
        synchronized (this) {
            if (this.supplier.test(this.value, value)) {
                this.value = value;
                return true;
            }
        }
        return false;
    }

    /**
     * Functional interface representing a condition to test whether a value can be updated.
     * <p>
     * The condition is represented as a method that takes two arguments: the current value and a proposed new value.
     * Implementations of this interface can define the logic for determining if the transition from the old value
     * to the new value is valid.
     *
     * @param <T> the type of the values to be tested
     */
    @FunctionalInterface
    public interface ValuePredicate<T> {
        /**
         * Evaluates a condition with the old and new values to determine if the update operation
         * should be allowed.
         *
         * @param oldValue the current value prior to the update
         * @param newValue the proposed new value to be set
         * @return {@code true} if the condition is met and the update is allowed, {@code false} otherwise
         */
        boolean test(final T oldValue, T newValue);
    }
}