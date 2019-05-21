package ru.sberbank.school.task03.util

import ru.sberbank.school.task02.util.FxResponse
import java.math.RoundingMode

class MergedResponses {
    String symbols
    List<String> dates
    List<BigDecimal> amounts
    List<String> directions
    List<BigDecimal> prices

    BigDecimal minAmount
    BigDecimal maxAmount
    BigDecimal avgAmount

    BigDecimal bestPriceSell
    BigDecimal bestPriceBuy
    BigDecimal worstPriceSell
    BigDecimal worstPriceBuy

    BigDecimal bestPriceSellAmount
    BigDecimal bestPriceBuyAmount
    BigDecimal worstPriceSellAmount
    BigDecimal worstPriceBuyAmount

    int count
    private BigDecimal summ

    MergedResponses(symbol, FxResponse response) {
        BigDecimal amount = parseNumber(response.getAmount())
        BigDecimal price = parseNumber(response.getPrice())
        String direction = response.getDirection()

        this.symbols = symbol
        this.dates = [response.getDate()] as ArrayList<String>
        this.amounts = [amount] as ArrayList<BigDecimal>
        this.directions = [direction] as ArrayList<String>
        this.prices = [price] as ArrayList<BigDecimal>

        this.minAmount = amount
        this.maxAmount = amount
        this.avgAmount = amount

        if (direction == "SELL") {
            this.bestPriceSell = price
            this.worstPriceSell = price

            this.bestPriceSellAmount = amount
            this.worstPriceSellAmount = amount
        } else {
            this.bestPriceBuy = price
            this.worstPriceBuy = price

            this.bestPriceBuyAmount = amount
            this.worstPriceBuyAmount = amount
        }

        this.count = 1
        this.summ = amount
    }

    void addNewItem(FxResponse response){
        BigDecimal amount = parseNumber(response.getAmount())
        BigDecimal price = parseNumber(response.getPrice())
        String direction = response.getDirection()

        dates.add(response.getDate())
        amounts.add(amount)
        directions.add(direction)
        prices.add(price)
        count++

        ifMinSet(amount)
        ifMaxSet(amount)
        recalculateAvg(amount)

        setAmountsForResume(price, amount, direction)
    }

    private BigDecimal parseNumber(String number) {
        BigDecimal result = BigDecimal.valueOf(Double.parseDouble(number))
        result.setScale(2, RoundingMode.HALF_UP)
    }

    private void ifMinSet(BigDecimal amount) {
        if (minAmount > amount) {
            minAmount = amount
        }
    }

    private void ifMaxSet(BigDecimal amount) {
        if (maxAmount < amount) {
            maxAmount = amount
        }
    }

    private void recalculateAvg(amount) {
        summ += amount
        avgAmount = (summ / count).setScale(2, RoundingMode.HALF_UP)
    }

    private void setAmountsForResume(BigDecimal price, BigDecimal amount, String direction) {
        if (direction == 'SELL') {
            if ( bestPriceSell == null || bestPriceSell < price) {
                bestPriceSell = price
                bestPriceSellAmount = amount
            }
            if (worstPriceSell == null || worstPriceSell > price) {
                worstPriceSell = price
                worstPriceSellAmount = amount
            }
        } else {
            if (bestPriceBuy == null || bestPriceBuy > price) {
                bestPriceBuy = price
                bestPriceBuyAmount = amount
            }
            if (worstPriceBuy == null || worstPriceBuy < price) {
                worstPriceBuy = price
                worstPriceBuyAmount = amount
            }
        }
    }
}


