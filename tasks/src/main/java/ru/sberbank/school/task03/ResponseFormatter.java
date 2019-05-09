package ru.sberbank.school.task03;

import java.util.List;

import ru.sberbank.school.task02.util.FxResponse;

public interface ResponseFormatter {
    /**
     * Выводит отформатированный по шаблону список ответов.
     *
     * @param responses список ответов, валютного сервиса
     * @return отформатированную строку по шаблону
     */
    String format(List<FxResponse> responses);
}
