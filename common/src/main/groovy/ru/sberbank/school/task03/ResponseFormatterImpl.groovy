package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.ClientOperation
import ru.sberbank.school.task02.util.FxResponse

class ResponseFormatterImpl implements ResponseFormatter {

    @Override
    String format(List<FxResponse> responses) {
        Objects.requireNonNull(responses, "передан null")
        String result = "${header(responses)}\n"
        result
    }

    private String header(List<FxResponse> responses) {
        Map<String, List<FxResponse>> map = responses.groupBy { it.symbol }
        def gString = "Отчет об изменении котировок для валют ${map.keySet().join(", ")}\n\n"
        map.each{ k, v -> gString += getInstruments(k, v) }
        gString += endInfo(map)
        gString
    }

    private String getInstruments(String instruments, List<FxResponse> responses) {
        def gString = """Данные по инструменту: ${instruments}
                        || Дата и время запроса | amount | operation(direction) | price\n""".stripMargin()
        for(FxResponse response : responses) {
            gString += "| " + response.date + " | " + response.amount + " | " + response.direction +
                    " | " + response.price + "\n"
        }
        gString += getAdditionalInfo(responses) + "\n"
        gString
    }

    private String getAdditionalInfo(List<FxResponse> responses) {
        def (responsesBuy, responsesSell) = responses.split {
            it.direction.equals(ClientOperation.BUY.toString())}
        def string = "${buyInfo(responsesBuy)}${sellInfo(responsesSell)}"
        string
    }

    private String buyInfo(List<FxResponse> responsesBuy) {
        def gString = ""
        if (responsesBuy.size() > 0) {
            gString += "Отчет по операциям покупки:\n${report(responsesBuy)}"
        }
        gString
    }

    private String sellInfo(List<FxResponse> responsesSell) {
        def gString = ""
        if (responsesSell.size() > 0) {
            gString = "Отчет по операциям продажи:\n${report(responsesSell)}"
        }
        gString
    }

    private String report(List<FxResponse> responses) {
        SolutionFormatter.calculateBodyValue(responses)
        def gString = """Максимальная цена: ${SolutionFormatter.getMaxPrice()}
                        |Минимальная цена: ${SolutionFormatter.getMinPrice()}
                        |Cредняя цена: ${SolutionFormatter.getAveragePrice()}\n""".stripMargin()
        gString += "Самая выгодная для клиента цена ${SolutionFormatter.getMaxPrice()} " +
                   "на объеме ${SolutionFormatter.getProfitAmount()}\n" +
                   "Самая невыгодная для клиента цена ${SolutionFormatter.getMinPrice()} " +
                   "на объеме ${SolutionFormatter.getUnprofitAmount()}\n"
        gString
    }

    private String endInfo(Map<String, List<FxResponse>> map) {
        SolutionFormatter.calculateEndValue(map)
        def string = "Всего запросов сделано: ${SolutionFormatter.getAllCount()} \n" +
                "Больше всего запросов по: ${SolutionFormatter.getMaxCountName()}" +
                "(${SolutionFormatter.getMaxCount()})"
        string
    }
}