package ru.sberbank.school.task13.cacheProxy.exception;

public class CacheProxyException extends Exception {
    public CacheProxyException(String message) {
        super(message);
    }

    public CacheProxyException(String message, Throwable cause) {
        super(message, cause);
    }
}
