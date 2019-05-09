package ru.sberbank.school.task02.exeption;

import ru.sberbank.school.task02.exception.FxConversionException;

/**
 * Created by Gregory Melnikov at 03.05.2019
 */

public class BuildRequestsNeverCallExeption extends FxConversionException {

    public BuildRequestsNeverCallExeption(String message) {
        super(message);
    }

}