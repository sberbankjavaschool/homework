package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse

class Formatter implements ResponseFormatter {
    @Override
    String format(List<FxResponse> responses) {
        Objects.requireNonNull(responses, "Список ответов равен null")

        if (responses.isEmpty()) {
            throw new IllegalArgumentException("Список ответов пуст")
        }

        ReportGenerator reportGenerator = new ReportGenerator()
        Report report = reportGenerator.makeReport(responses)

        """${getHeader(report)}

${getBody(report)}

${getConclusion(report)}"""
    }

    private String getHeader(Report report) {
        def result = "Отчет об изменении котировок для валют "
        report.symbols.each { s -> result += "${s}, " }
        result = result.substring(0, result.length() - 2)
    }

    private String getBody(Report report) {
        def result = ""
        report.symbolReport.each { r ->
            def current = r.getValue()

            result += getSymbolsInfo(report, r.getKey())

            result += """
Данные по суммам (мин/сред/макс): ${current.min} / ${current.mid} / ${current.max}
Самая выгодная для клиента цена при покупке ${current.buy.goodPrice} на объеме ${current.buy.goodVol}
Самая выгодная для клиента цена при продаже ${current.sell.goodPrice} на объеме ${current.sell.goodVol}
Самая невыгодная для клиента цена при покупке ${current.buy.badPrice} на объеме ${current.buy.badVol}
Самая невыгодная для клиента цена при продаже ${current.sell.badPrice} на объеме ${current.sell.badVol}"""
        }
        result
    }

    private String getConclusion(Report report) {
        """Всего запросов сделано: ${report.countRequests}
Больше всего запросов по: ${report.nameMostRequests} (${report.countMostRequests})"""
    }

    private String getSymbolsInfo(Report report, def symbol) {
        def result = "Данные по инструменту: ${symbol}\n"
        report.symbolResponses[symbol].each { el ->
            result += "| ${el.date} | ${el.amount} | ${el.direction} | ${el.price}\n"
        }
            result
    }
}
