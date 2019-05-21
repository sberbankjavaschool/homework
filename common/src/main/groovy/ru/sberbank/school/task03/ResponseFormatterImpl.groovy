package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task03.util.DataFromResponse
import ru.sberbank.school.task03.util.MergedResponses


class ResponseFormatterImpl implements ResponseFormatter {
    @Override
    String format(List<FxResponse> responses) {
        DataFromResponse data = new DataFromResponse(responses)

        """Отчет об изменении котировок для валют ${data.getSymbols()}
        
${createAllBody(data.getResponsesMap())}

Всего запросов сделано: ${data.getCount()}
Больше всего запросов по: ${data.getMostPopular()} (${data.getMostPopularCount()})"""
    }

    private String createAllBody(Map<String, MergedResponses> map){
        StringBuilder result = new StringBuilder()

        map.each {k, v ->
            String str = """Данные по инструменту: ${k}
${getTableForSymbol(v)}
Данные по суммам (мин = ${v.getMinAmount()} / сред = ${v.getAvgAmount()} / макс = ${v.getMaxAmount()})
${v.getBestPriceSell() ? """При продаже:
- cамая выгодная для клиента цена ${v.getBestPriceSell()} на объеме ${v.getBestPriceSellAmount()}
- cамая невыгодная для клиента цена ${v.getWorstPriceSell()} на объеме ${v.getWorstPriceSellAmount()}"""
                    : 'Не было ни одной операции по продаже'}
${v.getBestPriceBuy() ? """При покупке:
- cамая выгодная для клиента цена ${v.getBestPriceBuy()} на объеме ${v.getBestPriceBuyAmount()}
- cамая невыгодная для клиента цена ${v.getWorstPriceBuy()} на объеме ${v.getWorstPriceBuyAmount()}
""" : 'Не было ни одной операции по покупке'}"""

            result.append(str).append("\n")
        }

        result.toString()
    }

    private String getTableForSymbol(MergedResponses mr) {
        StringBuilder result = new StringBuilder()

        for (def i = 0; i < mr.getCount(); i++) {
            String str = "| ${mr.getDates().get(i)} | ${mr.getAmounts().get(i)} | ${mr.getDirections().get(i)} \
 | ${mr.getPrices().get(i)} |"

            result.append(str).append("\n")
        }

        result.toString()
    }
}