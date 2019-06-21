package ru.sberbank.school.task13.cacheproxy.exceptions;

public class CachingSerializationException extends RuntimeException {
    public CachingSerializationException(String message) {
        super(message);
    }

    public CachingSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
