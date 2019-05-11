package ru.sberbank.school.task02.exeption;

import ru.sberbank.school.task02.exception.FxConversionException;

public class BuildRequestsNeverCallException extends FxConversionException {

    public BuildRequestsNeverCallException(String message) {
        super(message);
    }

}