package ru.sberbank.school.task02.exeption;

import ru.sberbank.school.task02.exception.FxConversionException;

/**
 * Ошибка при возвращении пустого листа котировок
 * Created by Gregory Melnikov at 28.04.2019
 */
public class EmptyQuoteList extends FxConversionException {

    public EmptyQuoteList(String message) {
        super(message);
    }

}