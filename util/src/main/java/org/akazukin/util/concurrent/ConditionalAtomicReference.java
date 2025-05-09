package org.akazukin.util.concurrent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class ConditionalAtomicReference<T> {
    final ValuePredicate<T> supplier;
    @Getter
    T value;

    public boolean update(final T value) {
        synchronized (this) {
            if (this.supplier.test(this.value, value)) {
                this.value = value;
                return true;
            }
        }
        return false;
    }

    @FunctionalInterface
    public interface ValuePredicate<T> {
        boolean test(final T oldValue, T newValue);
    }
}