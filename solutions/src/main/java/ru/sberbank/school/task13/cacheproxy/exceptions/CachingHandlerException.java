package ru.sberbank.school.task13.cacheproxy.exceptions;

public class CachingHandlerException extends RuntimeException {
    public CachingHandlerException(String message) {
        super(message);
    }

    public CachingHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
