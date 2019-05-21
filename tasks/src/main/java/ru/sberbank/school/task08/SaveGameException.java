package ru.sberbank.school.task08;

import lombok.ToString;
import ru.sberbank.school.task08.state.Savable;

@ToString(callSuper = true)
public class SaveGameException extends Exception {

    private Type exceptionType = Type.SYSTEM;
    private Savable savable = null;

    SaveGameException(String message) {
        super(message);
    }

    SaveGameException(String message, Throwable cause, Type exceptionType, Savable savable) {
        super(message, cause);
        this.exceptionType = exceptionType;
    }

    SaveGameException(String message, Type exceptionType, Savable savable) {
        super(message);
        this.exceptionType = exceptionType;
    }

    enum Type {
        USER,
        SYSTEM,
        IO
    }
}
