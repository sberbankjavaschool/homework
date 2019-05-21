package ru.sberbank.school.task08;

public class SaveGameException extends Exception {

    private Type exceptionType = Type.SYSTEM;

    SaveGameException() {
    }

    SaveGameException(String message, Throwable cause, Type exceptionType) {
        super(message, cause);
        this.exceptionType = exceptionType;
    }

    SaveGameException(String message, Type exceptionType) {
        super(message);
        this.exceptionType = exceptionType;
    }

    enum Type {
        USER,
        SYSTEM,
        IO
    }
}
