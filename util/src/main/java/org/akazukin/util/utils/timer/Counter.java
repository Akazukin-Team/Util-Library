package org.akazukin.util.utils.timer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.akazukin.util.ext.Resettable;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Counter implements Resettable {
    @Getter
    @Setter
    int count = 0;

    public void count() {
        this.count++;
    }

    @Override
    public void reset() {
        this.count = 0;
    }
}
