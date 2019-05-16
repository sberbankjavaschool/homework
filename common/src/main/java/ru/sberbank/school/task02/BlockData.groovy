package ru.sberbank.school.task02

import ru.sberbank.school.task02.util.ClientOperation
import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task02.util.PairPriceAmount
import ru.sberbank.school.task02.util.RequestParser

import static java.math.RoundingMode.HALF_UP
import static ru.sberbank.school.task02.util.RequestParser.SCALE

class BlockData {

    int buyCount = 0
    int sellCount = 0

    private List<FxResponse> responsesData = new ArrayList<>()
    private BigDecimal min = Double.MAX_VALUE
    private BigDecimal avg = BigDecimal.ZERO
    private BigDecimal max = BigDecimal.ZERO

    PairPriceAmount bestBuyPrice = new PairPriceAmount()
    PairPriceAmount worstBuyPrice = new PairPriceAmount()

    PairPriceAmount bestSellPrice = new PairPriceAmount()
    PairPriceAmount worstSellPrice = new PairPriceAmount()

    BigDecimal getMin() {
        return min.setScale(SCALE, HALF_UP)
    }

    BigDecimal getAvg() {
        return avg.setScale(SCALE, HALF_UP)
    }

    BigDecimal getMax() {
        return max.setScale(SCALE, HALF_UP)
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

        BigDecimal price = new BigDecimal(response.price).setScale(SCALE, HALF_UP)

        ClientOperation operation = RequestParser.getClientOperation(response.direction)
        if (operation == ClientOperation.BUY) {
            if (buyCount == 0 || price < bestBuyPrice.price) {
                bestBuyPrice = new PairPriceAmount(price, amount)
            }
            if (buyCount == 0 || price > worstBuyPrice.price) {
                worstBuyPrice = new PairPriceAmount(price, amount)
            }
            buyCount++
        } else {
            if (sellCount == 0 || price > bestSellPrice.price) {
                bestSellPrice = new PairPriceAmount(price, amount)
            }
            if (sellCount == 0 || price < worstSellPrice.price) {
                worstSellPrice = new PairPriceAmount(price, amount)
            }
            sellCount++
        }

    }

    static Map<String,BlockData> buildData(List<FxResponse> responses) {
        HashMap<String, BlockData> mapSymbols = [:]
        for (FxResponse response : responses) {
            String symbol = response.symbol
            if (!mapSymbols.containsKey(symbol)) {
                mapSymbols[symbol] = new BlockData()
            }

            mapSymbols[symbol].addResponseData(response)
        }
        mapSymbols
    }

    static String getSymbolMostCount(Map<String,BlockData> mapSymbols) {
        int maxRequestOfSymbol = 0
        String symbolMostCount = ""
        for (String symbol : mapSymbols.keySet()) {
            int count = mapSymbols[symbol].getRequestsCount()
            if (count > maxRequestOfSymbol) {
                symbolMostCount = symbol
                maxRequestOfSymbol = count
            }
        }
        symbolMostCount
    }
}
