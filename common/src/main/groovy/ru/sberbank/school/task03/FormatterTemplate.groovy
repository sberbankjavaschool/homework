package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse

class FormatterTemplate {

    ResponseConverter converter

    FormatterTemplate(List<FxResponse> responses) {
        converter = new ResponseConverter(responses)
    }

    String getReport() {
        getHeader() + getBody() + getFooter()
    }

    String getHeader() {
        String result = "Отчет об изменении котировок для валют "
        Set<String> symbolList = converter.symbolList
        symbolList.each {
            if (it == symbolList[0]) {
                result += it
            } else {
                result += ", " + it
            }
        }
        result
    }

    String getFooter() {
        def mostRequestedSymbolPair = converter.getMostRequesredSymbolPair()
        int responsesCount = converter.getResponsesCount()
        String result = """\nВсего запросов сделано: ${responsesCount}
Больше всего запросов по: ${mostRequestedSymbolPair.symbol} (${mostRequestedSymbolPair.count})"""
    }

    String getBody() {
        Map<String, ResponseConverter.InfoAboutOperations> symbolsInfo = converter.getSymbolsInfo()
        String result = "\n"
        symbolsInfo.each { key, value ->
            result += getSymbolTopic(key) + getOperations(value) + getBuyReport(value) + getSellReport(value)
        }
        result
    }

    String getSymbolTopic(String symbol) {
        """\nДанные по инструменту: ${symbol}
| Дата и время запроса | amount | operation(direction) | price\n"""
    }

    String getOperations(ResponseConverter.InfoAboutOperations info) {
        String result = ""
        info.operations.each { result += it + "\n" }
        result
    }

    String getBuyReport(ResponseConverter.InfoAboutOperations info) {
        info.countBuyOperation != 0 ?
                """Отчет по операциям покупки:
    минимальная цена: $info.minBuyPrice
    максимальная цена: $info.maxBuyPrice
    средняя цена: ${info.getMeanBuyPrice(2)}
    самая выгодная для клиента цена $info.minBuyPrice на объеме $info.bestBuyPriceAmount
    самая невыгодная для клиента цена $info.maxBuyPrice на объеме $info.worstBuyPriceAmount\n"""
                : "Операций покупки не было\n"
    }

    String getSellReport(ResponseConverter.InfoAboutOperations info) {
        info.countSellOperation != 0 ?
                """Отчет по операциям продажи:
    минимальная цена: $info.minSellPrice
    максимальная цена: $info.maxSellPrice
    средняя цена: ${info.getMeanSellPrice(2)}
    самая выгодная для клиента цена $info.minSellPrice на объеме $info.bestSellPriceAmount
    самая невыгодная для клиента цена $info.maxSellPrice на объеме $info.worstSellPriceAmount\n"""
                : "Операций продажи не было\n"
    }
}