package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse

import java.math.RoundingMode


class ResponseGroup {

    String symbol
    String maxAmount
    String mediumAmount
    String minAmount
    int countRequest
    List<FxResponse> responses
    BigDecimal bestBuyPrice
    BigDecimal bestBuyAmount
    BigDecimal worstBuyPrice
    BigDecimal worstBuyAmount
    BigDecimal bestSellPrice
    BigDecimal bestSellAmount
    BigDecimal worstSellPrice
    BigDecimal worstSellAmount


    ResponseGroup(List<FxResponse> responses, String symbol) {
        this.symbol = symbol
        this.responses = responses
        countRequest = this.responses.size()
        calcAmounts()
        calcBuy()
        calcSell()
    }

    void calcAmounts() {

        BigDecimal max = new BigDecimal(responses.get(0).getAmount())
        BigDecimal min = new BigDecimal(responses.get(0).getAmount())
        BigDecimal sum = 0
        BigDecimal current

        for (FxResponse r in responses) {
            current = new BigDecimal(r.getAmount())
            if (max < current) {
                max = current
            }
            if (min > current) {
                min = current
            }
            sum += new BigDecimal(r.getAmount())
                    }

        maxAmount = max.toString()
        mediumAmount = sum.divide(BigDecimal.valueOf(responses.size()), 2, RoundingMode.HALF_UP).toString()
        minAmount = min.toString()
    }


    void calcBuy() {

        for (FxResponse r in responses) {
            BigDecimal price = new BigDecimal(r.getPrice())
            if (r.getDirection() == "BUY") {
                if (bestBuyPrice == null || price < bestBuyPrice) {
                    bestBuyPrice = price
                    bestBuyAmount = new BigDecimal(r.getAmount())
                }

                if (worstBuyPrice == null || price > worstBuyPrice) {
                    worstBuyPrice = price
                    worstBuyAmount = new BigDecimal(r.getAmount())
                }
            }
        }
    }

    void calcSell() {

        for (FxResponse r in responses) {
            BigDecimal price = new BigDecimal(r.getPrice())
            if (r.getDirection() == "SELL") {
                if (bestSellPrice == null || price > bestSellPrice) {
                    bestSellPrice = price
                    bestSellAmount = new BigDecimal(r.getAmount())
                }

                if (worstSellPrice == null || price < worstSellPrice) {
                    worstSellPrice = price
                    worstSellAmount = new BigDecimal(r.getAmount())
                }
            }
        }
    }
}
