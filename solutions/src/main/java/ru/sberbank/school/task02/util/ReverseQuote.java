package ru.sberbank.school.task02.util;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class ReverseQuote {

    private Volume volumeFrom;
    private Volume volumeTo;
    private BigDecimal price;

    @Builder
    public ReverseQuote(@NonNull Volume volumeFrom,
                 @NonNull Volume volumeTo,
                 @NonNull BigDecimal price) {
        this.volumeFrom = volumeFrom;
        this.volumeTo = volumeTo;
        this.price = price;
    }

    public BigDecimal getVolumeFromSize() {
        return volumeFrom.getVolume();
    }

    public BigDecimal getVolumeToSize() {
        return volumeTo.getVolume();
    }

}
