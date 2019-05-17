package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse

class Formatter implements ResponseFormatter {

    Report report

    @Override
    String format(List<FxResponse> responses) {

        report = new Report(responses)
        """
==== Отчет об изменении котировок для валют ${report.getSymbols().join(", ")} ====
${getBlock()}
=====================================
Всего запросов сделано: ${report.getCountRequests()}
Больше всего запросов по: ${report.getSymbolRequest()} (${report.getMaxCountRequests()})
"""
    }

    String getBlock() {

        String block = ""

        for (Map.Entry<String, ResponseGroup> entry in report.getResponseGroups()) {
            ResponseGroup group = entry.getValue()
            block += """
=== Данные по инструменту: ${entry.getKey()} ===
${getQuotes(group)}
Данные по суммам (макс/сред/мин) : ${group.getMaxAmount()} / ${group.getMediumAmount()} / ${group.getMinAmount()}
${getStatisticsBuy(group)}
${getStatisticsSell(group)}
"""
        }

        block

    }

    String getQuotes(ResponseGroup responseGroup) {

        String quote = ""

        for (FxResponse r in responseGroup.getResponses()) {

            quote += "| ${r.getDate()} | ${r.getAmount()} | ${r.getDirection()} | ${r.getPrice()} \n"
        }

        quote
    }

    String getStatisticsBuy(ResponseGroup responseGroup) {

        String statistics = ""

        if (responseGroup.getBestBuyPrice() == null) {
            statistics += "Запросов на покупку не было!"
        }
        else {
            statistics += """Самая выгодная для клиента цена при покупке ${responseGroup.getBestBuyPrice()} на объеме ${responseGroup.getBestBuyAmount()}
Самая невыгодная для клиента цена при покупке ${responseGroup.getWorstBuyPrice()} на объеме ${responseGroup.getWorstBuyAmount()}"""
        }

        statistics

    }

    String getStatisticsSell(ResponseGroup responseGroup) {

        String statistics = ""

        if (responseGroup.getBestSellPrice() == null) {
            statistics += "Запросов на продажу не было!"
        }
        else {
            statistics += """Самая выгодная для клиента цена при продаже ${responseGroup.getBestSellPrice()} на объеме ${responseGroup.getBestSellAmount()}
Самая невыгодная для клиента цена при продаже ${responseGroup.getWorstSellPrice()} на объеме ${responseGroup.getWorstSellAmount()}"""
        }

        statistics
    }

}