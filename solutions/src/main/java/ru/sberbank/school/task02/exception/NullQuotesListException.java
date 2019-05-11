package ru.sberbank.school.task02.exception;

public class NullQuotesListException extends FxConversionException {

    public NullQuotesListException(String message) {
        super(message);
    }

    public NullQuotesListException(String message, Throwable cause) {
        super(message, cause);
    }
}
