package ru.sberbank.school.task02;

import ru.sberbank.school.task02.exception.FxConversionException;

public class QuotesListNull extends FxConversionException {

    public QuotesListNull(String message) {
        super(message);
    }

    public QuotesListNull(String message, Throwable cause) {
        super(message, cause);
    }
}
