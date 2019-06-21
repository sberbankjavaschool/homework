package ru.sberbank.school.task13.beanfieldcopier.exceptions;

public class BeanFieldCopierException extends RuntimeException {
    public BeanFieldCopierException() {
    }

    public BeanFieldCopierException(String message) {
        super(message);
    }

    public BeanFieldCopierException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanFieldCopierException(Throwable cause) {
        super(cause);
    }
}
