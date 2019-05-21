package ru.sberbank.school.task03;

import ru.sberbank.school.task02.util.FxResponse;

import java.util.List;

public interface ResponseFormatter {
    /**
     * Выводит отформатированный по шаблону список ответов.
     *
     * @param responses список ответов, валютного сервиса
     * @return отформатированную строку по шаблону
     */
    String format(List<FxResponse> responses);
}
