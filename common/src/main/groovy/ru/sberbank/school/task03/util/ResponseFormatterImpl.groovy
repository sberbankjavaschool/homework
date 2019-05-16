package ru.sberbank.school.task03.util

import ru.sberbank.school.task02.util.FxResponse
import ru.sberbank.school.task03.ResponseFormatter
import ru.sberbank.school.task03.model.ResponseReportModel
import ru.sberbank.school.task03.model.ResponseReportModel.ReportMiddleBlock
import ru.sberbank.school.task03.model.ResponseReportModel.ReportResponse

class ResponseFormatterImpl implements ResponseFormatter {

    private final ResponseModelConverter converter = new ResponseModelConverter()

    /**
     * Выводит отформатированный по шаблону список ответов.
     *
     * @param responses список ответов, валютного сервиса
     * @return отформатированную строку по шаблону
     */
    @Override
    String format(List<FxResponse> responses) {

        ResponseReportModel model = converter.convert(responses)

        List<String> symbols = model.getModelSymbols()
        Map<String, ReportMiddleBlock> blocks = model.getModelBlocks()

        String gString = "====== Отчет об изменении котировок для валют ${symbols}======"
        for (String symbol : symbols) {
            ReportMiddleBlock block = blocks.get(symbol)
            List<ReportResponse> modelResponses = block.getModelResponses()
            gString += """\n====== Блок, повторяющийся для каждой валютной пары: ${symbol} ======
Данные по инструменту:\n SYMBOL | RESPONSE DATE-TIME | AMOUNT | OPERATION | PRICE | IS NOT FOUND"""
            for (ReportResponse response : modelResponses) {
                gString += """\n${response.number}. ${symbol} | ${response.date} | ${response.amount} | ${response.direction} | ${response.price} | ${response.isNotFound}"""
            }
            gString += """\nДанные по суммам: мин ${block.minAmount}/сред ${block.avgAmount}/макс ${block.maxAmount}
Самая выгодная для клиента цена ${block.profitablePrice} на объеме ${block.profitableAmount}
Самая невыгодная для клиента цена ${block.unprofitablePrice} на объеме ${block.unprofitableAmount}"""
        }
        gString += """\n====== Результирующая документ строка ======\nВсего запросов сделано: ${model.responsesQuantity}
Больше всего запросов по: ${model.maxResponsesBySymbol} (${model.maxResponsesSymbol})"""

        return gString
    }
}