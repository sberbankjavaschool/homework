package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;

/**
 * Исключение для пустого списка котировок.
 */
public class EmptyQuoteException extends FxConversionException {
    public EmptyQuoteException(String message) {
        super(message);
    }
}
