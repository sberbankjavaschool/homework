import model.DocumentInfo
import model.VolumeRequestInfo
import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task03.ResponseFormatter

class ResponseFormatterImpl implements ResponseFormatter {
    DocumentInfo doc

    @Override
    String format(List<FxResponse> responses) {
        doc = ResponseController.getDocument(responses)

        """${header()}
${body()}
${footer()}"""
    }

    String header() {
"""====== Отчет об изменении котировок для валютных пар ${doc.headerSymbols} ======\n"""
    }

    String body() {
        String result = ""
        for (requestInfo in doc.symbolRequestInfos) {
            result += """====== Данные по инструменту: ${requestInfo.symbol} ======
${responsesTable(requestInfo.volumes)}
Данные по суммам (мин/сред/макс):
${requestInfo.min}/${requestInfo.average}/${requestInfo.max}
Самая выгодная для клиента цена покупки ${requestInfo.profitBuy.price} на объеме ${requestInfo.profitBuy.amount}
Самая выгодная для клиента цена продажи ${requestInfo.profitSell.price} на объеме ${requestInfo.profitSell.amount}
Самая невыгодная для клиента цена покупки ${requestInfo.lossBuy.price} на объеме ${requestInfo.lossBuy.amount}
Самая невыгодная для клиента цена продажи ${requestInfo.lossSell.price} на объеме ${requestInfo.lossSell.amount}\n"""
        }
        result
    }

    static String responsesTable(List<VolumeRequestInfo> requests) {
        String result = ""
        requests.each { result += "${it.date} | ${it.amount} | ${it.direction} | ${it.price}\n"}
        result
    }

    String footer() {
        """====== Общие данные запросов ======
Всего запросов сделано: ${doc.requestCounter}
Больше всего запросов по: ${doc.maxSymbolRequested} (${doc.maxSymbolRequestedCounter})"""
    }
}
