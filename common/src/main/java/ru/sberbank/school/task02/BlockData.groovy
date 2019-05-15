package ru.sberbank.school.task02

import ru.sberbank.school.task02.util.ClientOperation
import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task02.util.RequestParser

import java.math.RoundingMode

class BlockData {

    int buyCount = 0
    int sellCount = 0

    static String symbolMaxCount

    private List<FxResponse> responsesData = new ArrayList<>()
    BigDecimal min = Double.MAX_VALUE
    private BigDecimal avg = BigDecimal.ZERO
    BigDecimal max = BigDecimal.ZERO

    PriceAmount bestBuyPrice = new PriceAmount()
    PriceAmount worstBuyPrice = new PriceAmount()

    PriceAmount bestSellPrice = new PriceAmount()
    PriceAmount worstSellPrice = new PriceAmount()

    BigDecimal getAvg() {
        return avg.setScale(0, RoundingMode.HALF_UP)
    }

    List<FxResponse> getResponses() {
        return responsesData
    }

    int getRequestsCount() {
        return responsesData.size()
    }

    void addResponseData(FxResponse response) {

        int count = getRequestsCount()
        responsesData.add(response)

        BigDecimal amount = RequestParser.getAmount(response.amount)
        if (amount > max) {
            max = amount
        }

        if (amount < min) {
            min = amount
        }

        avg = avg * (count / (count + 1)) + amount / (count + 1)

        BigDecimal price = RequestParser.getPrice(response.price)

        ClientOperation operation = RequestParser.getClientOperation(response.direction)
        if (operation == ClientOperation.BUY) {
            if (buyCount == 0 || price < bestBuyPrice.price) {
                bestBuyPrice.setData(price, amount)
            }
            if (buyCount == 0 || price > worstBuyPrice.price) {
                worstBuyPrice.setData(price, amount)
            }
            buyCount++
        }
        else {
            if (sellCount == 0 || price > bestSellPrice.price) {
                bestSellPrice.setData(price, amount)
            }
            if (sellCount == 0 || price < worstSellPrice.price) {
                worstSellPrice.setData(price, amount)
            }
            sellCount++
        }

    }

    static Map<String,BlockData> buildData(List<FxResponse> responses, Map<String,BlockData> mapSymbols) {
        for (FxResponse response : responses) {
            String symbol = response.symbol
            if (!mapSymbols.containsKey(symbol)) {
                mapSymbols[symbol] = new BlockData()
            }

            mapSymbols[symbol].addResponseData(response)
        }

        int maxRequestOfSymbol = 0

        for (String symbol : mapSymbols.keySet()) {
            int count = mapSymbols[symbol].getRequestsCount()
            if (count > maxRequestOfSymbol) {
                BlockData.symbolMaxCount = symbol
                maxRequestOfSymbol = count
            }
        }

        mapSymbols
    }
}
