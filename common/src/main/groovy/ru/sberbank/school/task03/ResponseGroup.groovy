package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse

import java.math.RoundingMode


class ResponseGroup {

    String symbol
    String maxAmount
    String mediumAmount
    String minAmount
    int size
    List<FxResponse> responses
    BigDecimal bestBuyPrice
    BigDecimal bestBuyAmount
    BigDecimal worstBuyPrice
    BigDecimal worstBuyAmount
    BigDecimal bestSellPrice
    BigDecimal bestSellAmount
    BigDecimal worstSellPrice
    BigDecimal worstSellAmount


    ResponseGroup(List<FxResponse> allResponses, String symbol) {
        this.symbol = symbol

        responses = new ArrayList<>()

        for (FxResponse r in allResponses) {

            if (r.getSymbol() == symbol)
                responses.add(r)
        }

        maxAmount = calcMaxAmount().toString()
        mediumAmount = calcMediumAmount().toString()
        minAmount = calcMinAmount().toString()
        size = responses.size()
        calcBuy()
        calcSell()
    }

    BigDecimal calcMaxAmount() {

        BigDecimal max = new BigDecimal(responses.get(0).getAmount())
        BigDecimal current

        for (FxResponse r in responses) {
            current = new BigDecimal(r.getAmount())
            if (max < current) {
                max = current
            }
        }

        max
    }

    BigDecimal calcMinAmount() {

        BigDecimal min = new BigDecimal(responses.get(0).getAmount())
        BigDecimal current

        for (FxResponse r in responses) {
            current = new BigDecimal(r.getAmount())
            if (min > current) {
                min = current
            }
        }

        min
    }

    BigDecimal calcMediumAmount() {

        BigDecimal medium = 0

        for (FxResponse r in responses) {

            medium = medium.add(new BigDecimal(r.getAmount()))
        }

        medium.divide(BigDecimal.valueOf(responses.size()), 2, RoundingMode.HALF_UP)
    }

    BigDecimal calcBuy() {

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

    BigDecimal calcSell() {

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
