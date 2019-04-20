package ru.sberbank.school.task02.exception;

public class WrongSymbolException extends RuntimeException {
    public WrongSymbolException(String message) {
        super(message);
    }
}
