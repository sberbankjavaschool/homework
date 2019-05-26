package ru.sberbank.school.task03.util

import ru.sberbank.school.task02.util.FxResponse

class Volume {
    String value

    BigDecimal bestPriceBuy = BigDecimal.ZERO
    BigDecimal worstPriceBuy = BigDecimal.ZERO
    BigDecimal bestPriceSell = BigDecimal.ZERO
    BigDecimal worstPriceSell = BigDecimal.ZERO


    Volume(String value) {
        this.value = value
    }

    void updatePrices(FxResponse response) {
        BigDecimal price = new BigDecimal(response.getPrice())
        if (response.getDirection().equals("BUY")) {
            if (bestPriceBuy.compareTo(BigDecimal.ZERO) <= 0 || bestPriceBuy.compareTo(price) > 0) {
                bestPriceBuy = price
            }
            if (worstPriceBuy.compareTo(BigDecimal.ZERO) <= 0 || worstPriceBuy.compareTo(price) < 0) {
                worstPriceBuy = price
            }
        } else {
            if (bestPriceSell.compareTo(BigDecimal.ZERO) <= 0 || bestPriceSell.compareTo(price) < 0) {
                bestPriceSell = price
            }
            if (worstPriceSell.compareTo(BigDecimal.ZERO) <= 0 || worstPriceSell.compareTo(price) > 0) {
                worstPriceSell = price
            }
        }
    }

    @Override
    String toString() {
        GString result = "Самая выгодная цена по операции BUY и объему ${value}: ${bestPriceBuy} \t " +
                         "Самая невыгодная цена по операции BUY и объему ${value} : ${worstPriceBuy} \n"

        result += "Самая выгодная цена по операции SELL и объему ${value}: ${bestPriceSell} \t " +
                  "Самая невыгодная цена по операции SELL и объему ${value} : ${worstPriceSell} \n"

        return result.toString()
    }
}
