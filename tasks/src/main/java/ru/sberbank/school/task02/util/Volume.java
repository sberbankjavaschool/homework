package ru.sberbank.school.task02.util;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class Volume {
    public static final Volume INFINITY = new Volume(-1);
    private final BigDecimal volume;
    private final boolean isInfinity;

    private Volume(long vol) {
        isInfinity = vol < 0;
        this.volume = BigDecimal.valueOf(vol);
    }

    private Volume(BigDecimal volume) {
        isInfinity = false;
        this.volume = volume;
    }

    public static Volume from(long vol) {
        if (vol < 0) {
            return INFINITY;
        }
        return new Volume(vol);
    }

    @Override
    public String toString() {
        return isInfinity ? "Infinity" : volume.toString();
    }
}
