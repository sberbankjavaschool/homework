package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task03.util.BodyData
import ru.sberbank.school.task03.util.MergedResponses

import java.math.RoundingMode

class ResponseFormatterImpl implements ResponseFormatter {
    @Override
    String format(List<FxResponse> responses) {
        Map<String, MergedResponses> responsesMap = createMap(responses)
        BodyData bodyData = getBodyData(responsesMap)
        """${createHeader(responses)}          
${bodyData.textOfBody}
${createResume(bodyData)}"""
    }

    private String createHeader(List<FxResponse> responses) {
        Set<String> set = new TreeSet<>()
        def result = "Отчет об изменении котировок для валют"

        responses.each { FxResponse response ->
            set.add(response.getSymbol().substring(0, 3));
            set.add(response.getSymbol().substring(4))
        }

        for (def i = 0; i < set.size(); i++) {
            result += " ${set[i]}"

            if (i != set.size() - 1){
                result+=","
            }
        }

        result
    }

    private Map<String, MergedResponses> createMap(List<FxResponse> responses) {
        Map map = [:].withDefault { key -> return [] } as LinkedHashMap<String, MergedResponses>

        responses.each { FxResponse response ->
            def symbol = response.getSymbol()

            if (!map.containsKey(symbol)) {
                map.put(symbol, new MergedResponses(symbol, response))
            } else {
                MergedResponses mr = map.getAt(symbol)
                mr.addNewItem(response)
                map[symbol] = mr
            }
        }

         map
    }

    private BodyData getBodyData(Map<String, MergedResponses> map) {
        def result = ""
        def mostPopular = ""
        def mostPopularCount = 0
        def reqCount = 0

        map.each { k, v ->
            def sum
            def max
            def min
            def minPriceSell
            def minPriceAmountSell
            def maxPriceSell
            def maxPriceAmountSell
            def minPriceBuy
            def minPriceAmountBuy
            def maxPriceBuy
            def maxPriceAmountBuy
            def count
            String operation

            result += "\nДанные по инструменту: ${k}\n"

            for (def i = 0; i < v.getCount(); i++) {
                String srtAmount = v.getAmounts().get(i)
                BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(srtAmount))
                BigDecimal price = BigDecimal.valueOf(Double.parseDouble(v.getPrices().get(i)))
                operation = v.getDirections().get(i)

                if (i == 0) {
                    sum = amount
                    max = amount
                    min = amount

                    if (operation == "SELL") {
                        minPriceSell = price
                        maxPriceSell = price
                        maxPriceAmountSell = srtAmount
                        minPriceAmountSell = srtAmount
                    } else {
                        minPriceBuy = price
                        maxPriceBuy = price
                        maxPriceAmountBuy = srtAmount
                        minPriceAmountBuy = srtAmount
                    }
                } else {
                    sum = sum + amount

                    if (max < amount) {
                        max = amount
                    }

                    if (min > amount) {
                        min = amount
                    }

                    if (operation =="SELL") {
                        if (minPriceSell == null || minPriceSell  > price) {
                            minPriceSell = price
                            minPriceAmountSell = srtAmount
                        }

                        if (minPriceSell == null || maxPriceSell < price) {
                            maxPriceSell = price
                            maxPriceAmountSell = srtAmount
                        }
                    } else {
                        if (minPriceBuy == null || minPriceBuy > price) {
                            minPriceBuy = price
                            minPriceAmountBuy = srtAmount
                        }

                        if (maxPriceBuy == null || maxPriceBuy < price) {
                            maxPriceBuy = price
                            maxPriceAmountBuy = srtAmount

                        }
                    }
                }
                def date = v.getDates().get(i)
                result += "| ${date} | ${amount.setScale(2, RoundingMode.HALF_UP)} | ${operation} " +
                        "| ${price.setScale(2, RoundingMode.HALF_UP)} |\n"

                count = v.getCount()

                if (mostPopularCount < count) {
                    mostPopularCount = count
                    mostPopular = k
                }
            }

            reqCount += count

            def avg = sum / 2

            result += "Данные по суммам (мин = ${min.setScale(2, RoundingMode.HALF_UP)} / " +
                    "сред = ${avg} / макс = ${max.setScale(2, RoundingMode.HALF_UP)})\n"

            if (minPriceAmountSell != null) {
                result += """При продаже:
- cамая выгодная для клиента цена ${maxPriceSell} на объеме ${maxPriceAmountSell}
- cамая невыгодная для клиента цена ${minPriceSell} на объеме ${minPriceAmountSell}
"""
            }

            if (minPriceAmountBuy != null) {
                result += """При покупке:
- cамая выгодная для клиента цена ${minPriceBuy} на объеме ${minPriceAmountBuy}
- cамая невыгодная для клиента цена ${maxPriceBuy} на объеме ${maxPriceAmountBuy}
"""
            }
        }

        return new BodyData(result, mostPopular, mostPopularCount, reqCount)
    }

    private String createResume(BodyData bodyData) {
        """Всего запросов сделано: ${bodyData.count}
Больше всего запросов по: ${bodyData.mostPopular} (${bodyData.mostPopularCount})
"""

    }
}