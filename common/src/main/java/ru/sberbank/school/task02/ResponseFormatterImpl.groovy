package ru.sberbank.school.task02


import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task03.ResponseFormatter

class ResponseFormatterImpl implements ResponseFormatter{

    private final Map<String,BlockData> mapSymbols = new HashMap<>()
    private final def separator = "="*40

    @Override
    String format(List<FxResponse> responses) {

        BlockData.buildData(responses, mapSymbols)
        def doc = "${getHead()}${getBlocksText()}${getResultingString(responses.size())}"
    }

    private String getHead() {
        "=======Отчет об изменении котировок для валют ${mapSymbols.keySet()}=======\n"
    }

    private String getBlocksText() {
        def result = ""
        for (String symbol : mapSymbols.keySet()) {
            BlockData symbolData = mapSymbols[symbol]
            result += """
=======Данные по инструменту: ${symbol}=======
Дата и время запроса | amount | operation(direction) | price
${getSymbolsData(symbolData)}
Данные по суммам (мин/сред/макс)\n($symbolData.min/$symbolData.avg/$symbolData.max)
$separator
${symbolData.buyCount > 0 ? getPriceOnBuy(symbolData) : "Покупок не было"}
$separator
${symbolData.sellCount > 0 ? getPriceOnSell(symbolData) : "Продаж не было"}
$separator
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

    private String getResultingString(int countRequests) {
        String symbol = BlockData.symbolMaxCount
        String result = "0 запросов.\n"
        if (countRequests > 0) {
            result = "Всего запросов сделано: ${countRequests}. Больше всего запросов по: ${symbol} (${mapSymbols[symbol].getRequestsCount()})"
        }
    }

}