package ru.sberbank.school.task13.cache.exceptions;

/**
 * Created by Mart
 * 10.07.2019
 **/
public class CacheProxyException extends RuntimeException {
    public CacheProxyException(String message) {
        super(message);
    }

    public CacheProxyException(String message, Throwable cause) {
        super(message, cause);
    }
}
