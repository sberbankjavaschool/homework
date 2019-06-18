package ru.sberbank.school.task13.fieldcopier.exception;

public class BeanFieldCopierException extends RuntimeException {

    public BeanFieldCopierException(String message) {
        super(message);
    }

    public BeanFieldCopierException(String message, Throwable cause) {
        super(message, cause);
    }
}
