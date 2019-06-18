package ru.sberbank.school.task13.cacheproxy.exception;

public class CacheProxyException extends RuntimeException {
    public CacheProxyException(String message) {
        super(message);
    }

    public CacheProxyException(String message, Throwable cause) {
        super(message, cause);
    }
}
