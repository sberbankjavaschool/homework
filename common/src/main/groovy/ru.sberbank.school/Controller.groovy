package ru.sberbank.school

import ru.sberbank.school.model.*
import ru.sberbank.school.task02.util.FxResponse

import static java.math.BigDecimal.ZERO
import static java.math.BigDecimal.valueOf

class Controller {

    static DocumentInfo buildDocument(List<FxResponse> responses) {

        String maxRequestsSymbol
        Set<String> symbols = []
        List<SymbolRequestInfo> infoList = []

        int totalRequests = 0
        int maxRequests = 0

        Map<String, List<FxResponse>> mappedResponses = responses.groupBy{ response -> response.getSymbol() }

        symbols = mappedResponses.keySet()
        symbols.each { key ->

            def currentSymbolList = mappedResponses[key]
            int currentSize = currentSymbolList.size()
            totalRequests += currentSize

            if (currentSize > maxRequests) {

                maxRequests = currentSize
                maxRequestsSymbol = key
            }

            infoList << buildSymbolBlock(currentSymbolList, key)
        }

        DocumentInfo document = new DocumentInfo()
        document.with {
            it.totalRequests = totalRequests.toString()
            it.maxRequests = maxRequests.toString()
            it.maxRequestsSymbol = maxRequestsSymbol
            it.symbols.addAll symbols
            it.symbolRequestInfoList = infoList
        }

        document
    }

    private static SymbolRequestInfo buildSymbolBlock(List<FxResponse> list, String symbol) {

        def directionMap = list.groupBy{ response -> response.getDirection() }

        BigDecimal minAmount = new BigDecimal(list.head().getAmount())
        BigDecimal maxAmount = valueOf(minAmount)
        BigDecimal sum = ZERO
        BigDecimal bestSellingPrice = new BigDecimal(directionMap["SELL"].head().getPrice())
        BigDecimal bestBuyingPrice =  new BigDecimal(directionMap["BUY"].head().getPrice())
        BigDecimal worstSellingPrice = valueOf(bestSellingPrice)
        BigDecimal worstBuyingPrice = valueOf(bestBuyingPrice)
        BigDecimal bestSellingAmount
        BigDecimal bestBuyingAmount
        BigDecimal worstSellingAmount
        BigDecimal worstBuyingAmount
        List<VolumeRequestInfo> volumeRequestInfoList = []

        directionMap["SELL"].each{ response ->

            BigDecimal price = new BigDecimal(response.getPrice())
            BigDecimal amount = new BigDecimal(response.getAmount())
            sum += amount

            if (amount < minAmount) { minAmount = amount }
            if (amount > maxAmount) { maxAmount = amount }
            if (price > bestSellingPrice) {

                bestSellingPrice = price
                bestSellingAmount = amount
            } else if (price < worstSellingPrice) {

                worstSellingPrice = price
                worstSellingAmount = amount
            }

            volumeRequestInfoList.add(new VolumeRequestInfo(response))
        }

        directionMap["BUY"].each{ response ->

            BigDecimal price = new BigDecimal(response.getPrice())
            BigDecimal amount = new BigDecimal(response.getAmount())
            sum += amount

            if (amount < minAmount) { minAmount = amount }
            if (amount > maxAmount) { maxAmount = amount }
            if (price > bestBuyingPrice) {

                bestBuyingPrice = price
                bestBuyingAmount = amount
            } else if (price < worstBuyingPrice) {

                worstBuyingPrice = price
                worstBuyingAmount = amount
            }

            volumeRequestInfoList.add(new VolumeRequestInfo(response))
        }

        String average = sum.divide(valueOf(list.size()), 2, BigDecimal.ROUND_HALF_UP).toString()

        SymbolRequestInfo info = new SymbolRequestInfo()
        info.with{
            it.min = minAmount.toString()
            it.max = maxAmount.toString()
            it.symbol = symbol
            it.average = average
            it.bestSellingPrice = bestSellingPrice.toString()
            it.bestBuyingPrice = bestSellingPrice.toString()
            it.bestSellingAmount = bestSellingAmount.toString()
            it.bestBuyingAmount = bestBuyingAmount.toString()
            it.worstSellingPrice = worstSellingPrice.toString()
            it.worstBuyingPrice = worstBuyingPrice.toString()
            it.worstSellingAmount = worstSellingAmount.toString()
            it.worstBuyingAmount = worstBuyingAmount.toString()
            it.volumeRequestInfoList = volumeRequestInfoList
        }

        info
    }                             

}

