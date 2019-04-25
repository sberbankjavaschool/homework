package ru.sberbank.school.task02.exception;

public class ConverterConfigurationException extends RuntimeException {

    public ConverterConfigurationException(String message) {
        super(message);
    }

    public ConverterConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
