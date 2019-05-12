package ru.sberbank.school.task03

import ru.sberbank.school.task02.exception.FxConversionException
import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task03.util.BodyData
import ru.sberbank.school.task03.util.MergedResponses

import java.math.RoundingMode

class ResponseFormatterImpl implements ResponseFormatter {
    @Override
    String format(List<FxResponse> responses) {
        Objects.requireNonNull(responses, "List of responses cant't be null")

        if (responses.isEmpty()) {
            throw new FxConversionException("List of responses can't be empty")
        }

        createHeader(responses) + getBody(responses)
    }

    private String createHeader(List<FxResponse> responses) {
        Set<String> set = new TreeSet<>()
        def result = "Отчет об изменении котировок для валют"

        responses.each { FxResponse response ->
            set.add(response.getSymbol().substring(0, 3));
            set.add(response.getSymbol().substring(4))
        }

        for (def i = 0; i < set.size(); i++) {
            result+=  " " + set.getAt(i)

            if (i != set.size() - 1){
                result+=","
            }
        }

        result + "\n"
    }

    private String getBody(List<FxResponse> responses) {
        Map instMap = [:].withDefault { key -> return [] } as LinkedHashMap<String, MergedResponses>

        responses.each { FxResponse response ->
            def symbol = response.getSymbol()

            if (!instMap.containsKey(symbol)) {
                instMap.put(symbol, new MergedResponses(symbol, response))
            } else {
                MergedResponses mr = instMap.getAt(symbol)
                mr.addNewItem(response)
                instMap.putAt(symbol, mr)
            }
        }

        BodyData resumeResp = getBodyData(instMap)
        def body = resumeResp.getTextOfBody()
        def count = resumeResp.getCount()
        def mostPopular = resumeResp.getMostPopular()
        def mostPopCount = resumeResp.getMostPopularCount()

        "${body}\nВсего запросов сделано: ${count}\nБольше всего запросов по: ${mostPopular} (${mostPopCount})\n"
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
            String minStr
            String maxStr

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
                    minStr = srtAmount
                    maxStr = srtAmount

                    if (operation.equalsIgnoreCase("sell")) {
                        minPriceAmountSell = srtAmount
                        maxPriceAmountSell = srtAmount
                        minPriceSell = price
                        maxPriceSell = price
                    } else {
                        minPriceAmountBuy = srtAmount
                        maxPriceAmountBuy = srtAmount
                        minPriceBuy = price
                        maxPriceBuy = price
                    }
                } else {
                    sum = sum.add(amount)

                    if (max.compareTo(amount) < 0) {
                        max = amount
                        maxStr = srtAmount
                    }

                    if (min.compareTo(amount) > 0) {
                        min = amount
                        minStr = srtAmount
                    }

                    if (operation.equalsIgnoreCase("sell")) {
                        if (minPriceAmountSell == null||minPriceSell.compareTo(price) > 0) {
                            minPriceSell = price
                            minPriceAmountSell = srtAmount
                        }

                        if (maxPriceSell == null||maxPriceSell.compareTo(price) < 0) {
                            maxPriceSell = price
                            maxPriceAmountSell = srtAmount
                        }
                    } else {
                        if (minPriceAmountBuy == null || minPriceBuy.compareTo(price) > 0) {
                            minPriceBuy = price
                            minPriceAmountBuy = srtAmount
                        }

                        if (maxPriceAmountBuy == null || maxPriceBuy.compareTo(price) < 0) {
                            maxPriceBuy = price
                            maxPriceAmountBuy = srtAmount
                        }
                    }
                }
                def date = v.getDates().get(i)
                result += "| ${date} | ${srtAmount} | ${operation} | ${price} |\n"

                count = v.getCount()

                if (mostPopularCount < count) {
                    mostPopularCount = count
                    mostPopular = k
                }
            }

            reqCount += count

            def avg = sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP)

            result += "Данные по суммам (мин = ${minStr} / сред = ${avg} / макс = ${maxStr})\n"

            if (minPriceAmountSell != null) {
                result += "При продаже:\n- cамая выгодная для клиента цена ${maxPriceSell}" +
                        " на объеме ${maxPriceAmountSell}\n"

                if (maxPriceSell.compareTo(minPriceSell) != 0) {
                    result += "- cамая невыгодная для клиента цена ${minPriceSell} на объеме ${minPriceAmountSell}\n"
                }
            }

            if (minPriceAmountBuy != null) {
                result += "При покупке:\n- cамая выгодная для клиента цена ${minPriceBuy}" +
                        " на объеме ${minPriceAmountBuy}\n"

                if (maxPriceBuy.compareTo(minPriceBuy) != 0) {
                    result += "- cамая невыгодная для клиента цена ${maxPriceBuy} на объеме ${maxPriceAmountBuy}\n"
                }
            }
        }

        return new BodyData(result, mostPopular, mostPopularCount, reqCount)
    }
}