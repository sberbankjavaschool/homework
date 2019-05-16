package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse

class ResponseConverter {

    Set<String> symbolList
    List<FxResponse> responses

    ResponseConverter(List<FxResponse> responses) {
        this.responses = responses
        symbolList = new HashSet<>()
        responses.each {
            symbolList.add(it.getSymbol())
        }
    }

    Map<String, InfoAboutOperations> getSymbolsInfo() {
        Map<String, InfoAboutOperations> symbolsInfo = new HashMap<>()

        for (String symbol : symbolList) {
            InfoAboutOperations infoAboutOperations = new InfoAboutOperations()
            for (FxResponse response : responses) {
                if (response.getSymbol() == symbol) {
                    String date = response.getDate()
                    BigDecimal amount = new BigDecimal(response.getAmount())
                    String direction = response.getDirection()
                    BigDecimal price = new BigDecimal(response.getPrice())
                    infoAboutOperations.addOperation("| ${date} | ${amount} | ${direction} | ${price}")

                    if (response.getDirection() == "BUY") {
                        if (!infoAboutOperations.sumBuyPrice) {
                            infoAboutOperations.minBuyPrice = price
                            infoAboutOperations.maxBuyPrice = price
                            infoAboutOperations.bestBuyPriceAmount = amount
                            infoAboutOperations.worstBuyPriceAmount = amount
                        }
                        infoAboutOperations.sumBuyPrice += price
                        infoAboutOperations.incCountBuyOperation()
                        if (price < infoAboutOperations.minBuyPrice) {
                            infoAboutOperations.bestBuyPriceAmount = amount
                            infoAboutOperations.minBuyPrice = price
                        } else if (price > infoAboutOperations.maxBuyPrice) {
                            infoAboutOperations.worstBuyPriceAmount = amount
                            infoAboutOperations.maxBuyPrice = price
                        }
                    } else { //(response.getDirection() == "SELL")
                        if (!infoAboutOperations.sumSellPrice) {
                            infoAboutOperations.minSellPrice = price
                            infoAboutOperations.maxSellPrice = price
                            infoAboutOperations.bestSellPriceAmount = amount
                            infoAboutOperations.worstSellPriceAmount = amount
                        }
                        infoAboutOperations.sumSellPrice += price
                        infoAboutOperations.incCountSellOperation()
                        if (price < infoAboutOperations.minSellPrice) {
                            infoAboutOperations.bestSellPriceAmount = amount
                            infoAboutOperations.minSellPrice = price
                        } else if (price > infoAboutOperations.maxSellPrice) {
                            infoAboutOperations.worstSellPriceAmount = amount
                            infoAboutOperations.maxSellPrice = price
                        }
                    }
                }
            }
            symbolsInfo.put(symbol, infoAboutOperations)
        }
        symbolsInfo
    }

    def getMostRequesredSymbolPair() {
        int mostRequestedSymbolCount = 0
        String mostRequestedSymbol = null
        for (String symbol : symbolList) {
            int thisSymbolCount = 0
            for (FxResponse response : responses) {
                if (response.getSymbol() == symbol) {
                    if (++thisSymbolCount > mostRequestedSymbolCount) {
                        mostRequestedSymbolCount = thisSymbolCount
                        mostRequestedSymbol = symbol
                    }
                }
            }
        }
        def pair = [symbol: mostRequestedSymbol, count: mostRequestedSymbolCount]
    }

    int getResponsesCount() {
        responses.size()
    }

    static class InfoAboutOperations {

        int countBuyOperation = 0
        int countSellOperation = 0
        BigDecimal minBuyPrice = 0
        BigDecimal minSellPrice = 0
        BigDecimal maxBuyPrice = 0
        BigDecimal maxSellPrice = 0
        BigDecimal sumBuyPrice = 0
        BigDecimal sumSellPrice = 0
        BigDecimal bestBuyPriceAmount = 0
        BigDecimal bestSellPriceAmount = 0
        BigDecimal worstBuyPriceAmount = 0
        BigDecimal worstSellPriceAmount = 0
        List<String> operations = new ArrayList<>()

        int incCountBuyOperation() {
            countBuyOperation++
        }

        int incCountSellOperation() {
            countSellOperation++
        }

        BigDecimal getMeanBuyPrice(int scale) {
            (sumBuyPrice / countBuyOperation).setScale(scale, BigDecimal.ROUND_HALF_UP)
        }

        BigDecimal getMeanSellPrice(int scale) {
            (sumSellPrice / countSellOperation).setScale(scale, BigDecimal.ROUND_HALF_UP)
        }

        void addOperation(GString operation) {
            operations.add(operation)
        }
    }
}