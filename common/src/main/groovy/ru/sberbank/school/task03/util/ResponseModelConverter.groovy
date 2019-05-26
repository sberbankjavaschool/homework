package ru.sberbank.school.task03.util

import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task03.model.ResponseReportModel
import ru.sberbank.school.task03.model.ResponseReportModel.ReportMiddleBlock
import ru.sberbank.school.task03.model.ResponseReportModel.ReportResponse

class ResponseModelConverter {

    ResponseReportModel convert(List<FxResponse> responses) {

        ResponseReportModel model = new ResponseReportModel()
        Map<String, ReportMiddleBlock> modelBlocks = model.getModelBlocks()

        int maxResponsesBySymbol
        String maxResponsesSymbol

        List<String> symbols = model.getModelSymbols()

        responses*.getSymbol().unique().each { symbol -> symbols.add(symbol) }

        for (String symbol : symbols) {
            BigDecimal maxAmount
            BigDecimal allAmount = BigDecimal.ZERO
            BigDecimal profitableAmount
            BigDecimal unprofitableAmount
            int symbolResponseCount
            double minAmount = Double.POSITIVE_INFINITY
            double profitablePrice
            double unprofitablePrice = Double.POSITIVE_INFINITY

            ReportMiddleBlock middleBlock = new ReportMiddleBlock()

            List<ReportResponse> modelResponses = middleBlock.getModelResponses()

            for (FxResponse response : responses) {
                if (symbol.equals(response.getSymbol())) {
                    BigDecimal responseAmount = new BigDecimal(response.getAmount())
                    allAmount += responseAmount
                    if (maxAmount <= responseAmount) maxAmount = responseAmount
                    if (minAmount >= responseAmount) minAmount = responseAmount
                    symbolResponseCount++
                    if (maxResponsesBySymbol < symbolResponseCount) {
                        maxResponsesBySymbol = symbolResponseCount
                        maxResponsesSymbol = symbol
                    }
                    double responsePrice
                    if (!response.getPrice().isBlank()) {
                        responsePrice = Double.valueOf(response.getPrice())
                    }
                    if (profitablePrice <= responsePrice) {
                        profitablePrice = responsePrice
                        profitableAmount = responseAmount
                    }
                    if (unprofitablePrice >= responsePrice) {
                        unprofitablePrice = responsePrice
                        unprofitableAmount = responseAmount
                    }
                    ReportResponse modelResponse = new ReportResponse()
                    modelResponse.setNumber(symbolResponseCount)
                    modelResponse.setDate(response.getDate())
                    modelResponse.setAmount(responseAmount)
                    modelResponse.setDirection(response.getDirection())
                    modelResponse.setPrice(responsePrice)
                    modelResponse.setIsNotFound(response.isNotFound())
                    modelResponses.add(modelResponse)
                }
            }
            middleBlock.setMinAmount(minAmount)
            middleBlock.setAvgAmount(allAmount / symbolResponseCount)
            middleBlock.setMaxAmount(maxAmount)
            middleBlock.setProfitablePrice(profitablePrice)
            middleBlock.setProfitableAmount(profitableAmount)
            middleBlock.setUnprofitablePrice(unprofitablePrice)
            middleBlock.setUnprofitableAmount(unprofitableAmount)
            middleBlock.setSymbolResponseCount(symbolResponseCount)
            modelBlocks.put(symbol, middleBlock)
        }
        model.setResponsesQuantity(responses.size())
        model.setMaxResponsesBySymbol(maxResponsesBySymbol)
        model.setMaxResponsesSymbol(maxResponsesSymbol)
        return model
    }
}

