package ru.sberbank.school.task03.model

class ResponseReportModel {
    int responsesQuantity
    int maxResponsesBySymbol
    String maxResponsesSymbol

    List<String> modelSymbols = new ArrayList<>();
    Map<String, ReportMiddleBlock> modelBlocks = new HashMap<>()

    class ReportMiddleBlock {
        String symbol
        BigDecimal maxAmount
        BigDecimal avgAmount
        BigDecimal profitableAmount
        BigDecimal unprofitableAmount
        int symbolResponseCount
        double minAmount
        double profitablePrice
        double unprofitablePrice

        List<ReportResponse> modelResponses = new ArrayList<>()
    }

    class ReportResponse {
        String date
        String direction
        BigDecimal amount
        int number
        double price
        boolean isNotFound
    }
}