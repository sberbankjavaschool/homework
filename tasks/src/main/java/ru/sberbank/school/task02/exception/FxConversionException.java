package ru.sberbank.school.task02.exception;

/**
 * Общий класс для исключительных ситуаций процесса конверсии.
 */
public class FxConversionException extends RuntimeException {

    public FxConversionException(String message) {
        super(message);
    }

    public FxConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
