package ru.sberbank.school.task13;

/**
 * Created by Mart
 * 20.07.2019
 **/
public class BeanFieldCopierException extends RuntimeException {
    public BeanFieldCopierException(String msg) {
        super(msg);
    }

    public BeanFieldCopierException(String message, Throwable cause) {
        super(message, cause);
    }
}
