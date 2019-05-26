package ru.sberbank.school.task03

import ru.sberbank.school.task03.model.*

class Template {

    static String buildView(DocumentInfo document) {

        StringBuilder view = new StringBuilder()
        view << header(document.symbols) <<
                body(document.symbolRequestInfoList) <<
                footer(document.totalRequests,
                       document.maxRequests,
                       document.maxRequestsSymbol)

        view
    }

    private static String header(Set<String> symbols) {

        symbols.each { symbol ->
            "==== Отчет об изменении котировок для валют ${symbol} ====\n"
        }
    }

    private static String body(List<SymbolRequestInfo> requestList) {

        StringBuilder body = new StringBuilder()
        requestList.each { request ->

            body <<"""====== Данные по инструменту: ${request.symbol} ======\n
${responseBlock(request.volumeRequestInfoList)} 
Данные по суммам:
\tmin: \taverage: \tmax:
\t${request.min} \t${request.average} \t${request.max}
Самая выгодная для клиента цена покупки ${request.bestBuyingPrice} на объеме ${request.bestBuyingAmount}
Самая выгодная для клиента цена продажи ${request.bestSellingPrice} на объеме ${request.bestSellingAmount}
Самая невыгодная для клиента цена покупки ${request.worstBuyingPrice} на объеме ${request.worstBuyingAmount}
Самая невыгодная для клиента цена продажи ${request.worstSellingPrice} на объеме ${request.worstSellingAmount}\n"""
        }

        body
    }

    private static String footer(String totalRequests, String maxRequests, String maxRequestsSymbol) {

        """====== Общие данные запросов ======
Всего запросов сделано: ${totalRequests}
Больше всего запросов по: ${maxRequestsSymbol} (${maxRequests})\n"""
    }

    private static String responseBlock(List<VolumeRequestInfo> quoteList) {

        StringBuilder block = new StringBuilder()
        quoteList.each { quote ->
            block << "${quote.date} | ${quote.amount} | ${quote.direction} | ${quote.price} \n"
        }

        block
    }
}