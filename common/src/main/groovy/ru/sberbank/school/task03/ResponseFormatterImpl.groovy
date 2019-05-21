package ru.sberbank.school.task03

import ru.sberbank.school.task02.util.FxResponse

class ResponseFormatterImpl implements ResponseFormatter {
    /**
     * Выводит отформатированный по шаблону список ответов.
     *
     * @param responses список ответов, валютного сервиса
     * @return отформатированную строку по шаблону
     */

    @Override
    String format(List<FxResponse> responses) {
        Objects.requireNonNull(responses, "Переданный список операций не инициализирован")
        if (responses.isEmpty()) {
            return "Список запросов пуст"
        }
        FormatterTemplate template = new FormatterTemplate(responses)
        template.getReport()
    }
}