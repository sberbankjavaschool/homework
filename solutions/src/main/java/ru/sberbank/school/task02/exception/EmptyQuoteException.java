package ru.sberbank.school.task02.exception;

/**
 * Исключение для пустокого списка котировок.
 */
public class EmptyQuoteException extends FxConversionException {
    public EmptyQuoteException(String message) {
        super(message);
    }
}
