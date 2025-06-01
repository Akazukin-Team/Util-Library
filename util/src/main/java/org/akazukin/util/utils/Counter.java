package org.akazukin.util.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.akazukin.util.interfaces.Resettable;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Counter implements Resettable {
    @Getter
    @Setter
    int count;

    public void count() {
        this.count++;
    }

    @Override
    public void reset() {
        this.count = 0;
    }
}
