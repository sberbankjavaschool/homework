package ru.sberbank.school.task13.cacheproxy.exception;

public class SerializationInCacheException extends RuntimeException {
    public SerializationInCacheException(String message) {
        super(message);
    }

    public SerializationInCacheException(String message, Throwable cause) {
        super(message, cause);
    }

}
