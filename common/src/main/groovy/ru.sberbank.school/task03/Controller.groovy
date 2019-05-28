package ru.sberbank.school.task03

import ru.sberbank.school.task03.model.*
import ru.sberbank.school.task02.util.FxResponse

import static java.math.BigDecimal.ZERO
import static java.math.BigDecimal.valueOf

class Controller {

    static DocumentInfo buildDocument(List<FxResponse> responses) {

        Set<String> symbols = []
        List<SymbolRequestInfo> infoList = []
        String maxRequestsSymbol = ""

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

        DocumentInfo document = new DocumentInfo(
                totalRequests : totalRequests.toString(),
                maxRequests : maxRequests.toString(),
                maxRequestsSymbol : maxRequestsSymbol,
                symbols : symbols,
                symbolRequestInfoList : infoList)

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
        BigDecimal price
        BigDecimal amount
        List<VolumeRequestInfo> volumeRequestInfoList = []

        directionMap["SELL"].each{ response ->

            price = new BigDecimal(response.getPrice())
            amount = new BigDecimal(response.getAmount())
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

            price = new BigDecimal(response.getPrice())
            amount = new BigDecimal(response.getAmount())
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

        SymbolRequestInfo info = new SymbolRequestInfo(
                min : minAmount.toString(),
                max : maxAmount.toString(),
                symbol : symbol,
                average : average,
                bestSellingPrice : bestSellingPrice.toString(),
                bestBuyingPrice : bestSellingPrice.toString(),
                bestSellingAmount : bestSellingAmount.toString(),
                bestBuyingAmount : bestBuyingAmount.toString(),
                worstSellingPrice : worstSellingPrice.toString(),
                worstBuyingPrice : worstBuyingPrice.toString(),
                worstSellingAmount : worstSellingAmount.toString(),
                worstBuyingAmount : worstBuyingAmount.toString(),
                volumeRequestInfoList : volumeRequestInfoList)

        info
    }                             

}

