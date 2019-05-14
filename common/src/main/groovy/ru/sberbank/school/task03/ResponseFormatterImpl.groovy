package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task02.util.Symbol

import java.lang.String
import java.lang.reflect.Array

class ResponseFormatterImpl implements ResponseFormatter {
    int maxcountResponses
    int countCommonResponses
    String maxcountResponsesCurrency
    /**
     * Выводит отформатированный по шаблону список ответов.
     *
     * @param responses список ответов, валютного сервиса
     * @return отформатированную строку по шаблону
     */

    @Override
    String format(List<FxResponse> responses) {
        if (responses.isEmpty()) {
            throw new IllegalArgumentException("List of responses is empty")
        }
        String formatResponse = createHeader(responses)
        List<String> symbols = new ArrayList<>();
        responses*.getSymbol().unique().each {
            symbol -> symbols.add(symbol)
        }
        for (String symbol : symbols) {
            formatResponse += createCurPairBlock(symbol, responses);
        }
//        symbols*.each {
//            symbol -> formatResponse += createCurPairBlock(symbol, responses);
//        }
        formatResponse += createConclusion()
        //println(formatResponse)
        return formatResponse
    }

    String createHeader(List<FxResponse> responses) {
        String currencies = responses*.getSymbol().unique();
        String result = "Отчет об изменении котировок для валют ${(currencies)}"
        result
    }

    String createConclusion() {
        String conclusion = """\nВсего запросов сделано: ${countCommonResponses}
Больше всего запросов по: ${maxcountResponsesCurrency} (${maxcountResponses})"""
        conclusion
    }

    String createCurPairBlock(String symbol, List<FxResponse> responses) {

        String result = "\nДанные по инструменту:${symbol}"
        BigDecimal totalAmount = BigDecimal.ZERO
        BigDecimal maxAmount
        BigDecimal minAmount = BigDecimal.ZERO
        int countResponses = 0
        BigDecimal profitPrice = BigDecimal.ZERO
        BigDecimal nonprofitPrice = BigDecimal.ZERO
        BigDecimal profitAmount
        BigDecimal nonprofitAmount

        for (FxResponse response : responses) {
            if (symbol.toString().equals(response.getSymbol())) {
                BigDecimal curAmount = new BigDecimal(response.getAmount())
                totalAmount += curAmount
                countResponses++
                if (maxAmount < curAmount) {
                    maxAmount = curAmount
                }
                if (minAmount > curAmount) {
                    minAmount = curAmount
                }
                if (minAmount.equals(BigDecimal.ZERO)) {
                    minAmount = curAmount
                }

                BigDecimal curPrice = new BigDecimal(response.getPrice())

//                if (response.getDirection().toString().equals("BUY")) {
                if (nonprofitPrice.equals(BigDecimal.ZERO)) {
                    nonprofitPrice = curPrice
                    nonprofitAmount = new BigDecimal(response.getAmount())
                }
                if (curPrice > profitPrice) {
                        profitPrice = curPrice
                        profitAmount = new BigDecimal(response.getAmount())
                }
                if (curPrice < nonprofitPrice) {
                        nonprofitPrice = curPrice
                        nonprofitAmount = new BigDecimal(response.getAmount())
                    }
//                } else {
//                    if (curPrice > profitPrice) {
//                        profitPrice = curPrice
//                        profitAmount = new BigDecimal(response.getAmount())
//                    } else if (curPrice < nonprofitPrice) {
//                        nonprofitPrice = curPrice
//                        nonprofitAmount = new BigDecimal(response.getAmount())
//                    }
//                }

                result += "\n| ${response.getDate()} | ${curAmount} | ${response.getDirection()} | ${curPrice}"

            }
        }
        countCommonResponses += countResponses
        if (countResponses > maxcountResponses) {
            maxcountResponses = countResponses
            maxcountResponsesCurrency = symbol
        }
        result += """\nДанные по суммам (${minAmount}/${(totalAmount.divide(countResponses))}/${maxAmount})
Самая выгодная для клиента цена ${profitPrice} на объеме ${profitAmount}
Самая невыгодная для клиента цена ${nonprofitPrice} на объеме ${nonprofitAmount}"""
        result
    }

}
