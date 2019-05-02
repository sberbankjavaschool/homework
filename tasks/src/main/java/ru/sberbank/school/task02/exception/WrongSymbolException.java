package ru.sberbank.school.task02.exception;

public class WrongSymbolException extends FxConversionException {
    public WrongSymbolException(String message) {
        super(message);
    }
}
