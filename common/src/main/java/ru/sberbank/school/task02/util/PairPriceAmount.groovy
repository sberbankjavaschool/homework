package ru.sberbank.school.task02.util

class PairPriceAmount {
    BigDecimal price
    BigDecimal amount

    PairPriceAmount() {
        this.price = BigDecimal.ZERO
        this.amount = BigDecimal.ZERO
    }

    PairPriceAmount(BigDecimal price, BigDecimal amount) {
        this.price = price
        this.amount = amount
    }
}
