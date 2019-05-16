package ru.sberbank.school.task02


import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task03.ResponseFormatter

class ResponseFormatterImpl implements ResponseFormatter{

    private Map<String,BlockData> mapSymbols = new HashMap<>()

    @Override
    String format(List<FxResponse> responses) {
        mapSymbols = BlockData.buildData(responses)
        def doc = "${getHead()}${getBlocksText()}${getResultingString(responses)}"
    }

    private String getHead() {
        "=======Отчет об изменении котировок для валют ${mapSymbols.keySet()}=======\n"
    }

    private String getBlocksText() {
        def result = ""
        for (Map.Entry<String, BlockData> symbol : mapSymbols.entrySet()) {
            BlockData value = symbol.value
            result += """
=======Данные по инструменту: ${symbol}=======
Дата и время запроса | amount | operation(direction) | price
${getSymbolsData(value)}
Данные по суммам (мин/сред/макс)
($value.min/$value.avg/$value.max)
=============================================
${value.buyCount > 0 ? getPriceOnBuy(value) : "Покупок не было"}
=============================================
${value.sellCount > 0 ? getPriceOnSell(value) : "Продаж не было"}
=============================================
"""
        }

        result != "" ? result : "Данных по инструментам нет.\n"
    }

    private static String getPriceOnBuy(BlockData symbolData) {
        def result = """\
Самая выгодная для клиента цена при покупке: \
$symbolData.bestBuyPrice.price на объеме $symbolData.bestBuyPrice.amount 
Самая невыгодная для клиента цена при покупке: $symbolData.worstBuyPrice.price на объеме \
${symbolData.worstBuyPrice.amount}"""
    }

    private static String getPriceOnSell(BlockData symbolData) {
        def result = """\
Самая выгодная для клиента цена при продаже: \
$symbolData.bestSellPrice.price на объеме $symbolData.bestSellPrice.amount 
Самая невыгодная для клиента цена при продаже: $symbolData.worstSellPrice.price на объеме \
${symbolData.worstSellPrice.amount}"""
    }

    private static String getSymbolsData(BlockData symbolData) {
        def result = ""
        for (FxResponse response : symbolData.getResponses()) {
            result += "$response.date | $response.amount | $response.direction | $response.price\n"
        }

        result
    }

    private String getResultingString(List<FxResponse> responses) {
        int countRequests = responses.size()
        String result = "0 запросов.\n"
        if (countRequests > 0) {
            String symbol = BlockData.getSymbolMostCount(mapSymbols)
            result = "Всего запросов сделано: ${countRequests}. Больше всего запросов по: ${symbol} (${mapSymbols[symbol].getRequestsCount()})"
        }
    }

}