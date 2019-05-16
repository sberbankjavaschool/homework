package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task03.model.DocumentInfo
import ru.sberbank.school.task03.model.SymbolRequestInfo
import ru.sberbank.school.task03.model.VolumeRequestInfo

class Formatter implements ResponseFormatter {
    DocumentInfo document

    @Override
    String format(List<FxResponse> responses) {
        ModelCalculation modelCalculation = new ModelCalculation(responses)
        document = modelCalculation.calculation()

        """${buildHeading()}
${buildBlock()}
${buildConclusion()}"""
    }

    String buildHeading() {
        "====== Отчет об изменении котировок для валютных пар ${document.headingSymbols} ======\n"
    }

    String buildConclusion() {
        """=========================================
Всего запросов сделано: ${document.numberRequest}
Больше всего запросов по: ${document.maxSymbol} (${document.maxNumberSymbolRequest})"""
    }

    String buildBlock() {
        String result = ""
        for (SymbolRequestInfo request : document.symbolRequest) {
            result += """====== Данные по инструменту: ${request.symbol} ======

${getDataResponses(request.volumes)}
Данные по суммам (макс/сред/мин) : (${request.max} /${request.medium} /${request.min})
${getPriceOnBuy(request) ?: "Запросов на покупку не было"}
${getPriceOnSelling(request) ?: "Запросов на продажу не было"}

"""
        }
        result
    }

    String getDataResponses(List<VolumeRequestInfo> request) {
        String result = ""
        request.each { result += "${it.date} | ${it.amount} | ${it.direction} | ${it.price}\n" }
        result
    }

    String getPriceOnBuy(SymbolRequestInfo request) {
        FxResponse profitBuy = request.profitBuy
        FxResponse unprofitBuy = request.unprofitBuy

        if (profitBuy == null || unprofitBuy == null) {
            return null
        }

        """Самая выгодная для клиента при покупке цена ${profitBuy.price} на объеме ${profitBuy.amount}
Cамая невыгодная для клиента при покупке цена ${unprofitBuy.price} на объеме ${unprofitBuy.amount}"""
    }

    String getPriceOnSelling(SymbolRequestInfo request) {
        FxResponse profitSell = request.profitSell
        FxResponse unprofitSell = request.unprofitSell

        if (profitSell == null || unprofitSell == null) {
            return null
        }

        """Самая выгодная для клиента при продаже цена ${profitSell.price} на объеме ${profitSell.amount}
Самая невыгодная для клиента при продаже цена ${unprofitSell.price} на объеме ${unprofitSell.amount}"""

    }
}
