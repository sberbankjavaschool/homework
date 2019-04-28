package ru.sberbank.school.task02.exeption;

/**
 * Ошибка при возвращении пустого листа котировок
 * Created by Gregory Melnikov at 28.04.2019
 */
public class EmptyQuoteList extends RuntimeException {
    public EmptyQuoteList(String message) {
        super(message);
    }
}
