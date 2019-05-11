package ru.sberbank.school.task03.util

import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task03.ResponseFormatter

class ResponseFormatterImpl implements ResponseFormatter {
    /**
     * Выводит отформатированный по шаблону список ответов.
     *
     * @param responses список ответов, валютного сервиса
     * @return отформатированную строку по шаблону
     */
    @Override
    String format(List<FxResponse> responses) {

        int maxResponsesCount
        String maxResponses

        List<String> symbols = new ArrayList<>()

        responses*.getSymbol().unique().each { symbol -> symbols.add(symbol) }

        String gString = "====== Отчет об изменении котировок для валют ${responses*.getSymbol().unique()}======"

        for (String symbol : symbols) {

            gString += "\n====== Блок, повторяющийся для каждой валютной пары: ${symbol} ======" +
                    "\nДанные по инструменту:\n SYMBOL | RESPONSE DATE-TIME | AMOUNT | OPERATION | PRICE | IS NOT FOUND"

            BigDecimal maxAmount
            BigDecimal allAmount = BigDecimal.ZERO
            BigDecimal profitableAmount
            BigDecimal unprofitableAmount
            int symbolResponseCount
            double minAmount = Double.POSITIVE_INFINITY
            double profitablePrice
            double unprofitablePrice = Double.POSITIVE_INFINITY

            for (FxResponse response : responses) {
                if (symbol.equals(response.getSymbol())) {
                    BigDecimal responseAmount = new BigDecimal(response.getAmount())
                    allAmount += responseAmount
                    if (maxAmount <= responseAmount) maxAmount = responseAmount
                    if (minAmount >= responseAmount) minAmount = responseAmount
                    symbolResponseCount++
                    if (maxResponsesCount < symbolResponseCount) {
                        maxResponsesCount = symbolResponseCount
                        maxResponses = symbol
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
                    gString += "\n${symbolResponseCount}. ${symbol} | ${response.getDate()} | ${responseAmount}" +
                            " | ${response.getDirection()} | ${responsePrice} | ${response.isNotFound()}"
                }
            }
            gString += "\nДанные по суммам: мин ${minAmount}/сред ${allAmount / symbolResponseCount}/макс ${maxAmount}" +
                    "\nСамая выгодная для клиента цена ${profitablePrice} на объеме ${profitableAmount}" +
                    "\nСамая невыгодная для клиента цена ${unprofitablePrice} на объеме ${unprofitableAmount}"
        }
        gString += "\n====== Результирующая документ строка ======\nВсего запросов сделано: ${responses.size()} " +
                "\nБольше всего запросов по: ${maxResponses} (${maxResponsesCount})"

        return gString
    }
}