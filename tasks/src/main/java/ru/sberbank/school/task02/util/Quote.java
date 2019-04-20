package ru.sberbank.school.task02.util;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode(of = "id")
public class Quote {
    private final String id = UUID.randomUUID().toString();

    @Getter
    private final Symbol symbol;
    /**
     * Верхняя граница объема.
     */
    @Getter
    private final Volume volume;
    private final BigDecimal bid;
    private final BigDecimal offer;

    @Builder
    public Quote(@NonNull Symbol symbol,
                 @NonNull Volume volume,
                 @NonNull BigDecimal bid,
                 @NonNull BigDecimal offer) {
        this.symbol = symbol;
        this.volume = volume;
        this.bid = bid;
        this.offer = offer;
    }

    public boolean isInfinity() {
        return volume.isInfinity();
    }

    public BigDecimal getVolumeSize() {
        return volume.getVolume();
    }

    public Optional<BigDecimal> getBid() {
        return Optional.ofNullable(bid);
    }

    public Optional<BigDecimal> getOffer() {
        return Optional.ofNullable(offer);
    }
}
