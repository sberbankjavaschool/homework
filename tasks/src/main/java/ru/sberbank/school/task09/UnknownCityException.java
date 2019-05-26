package ru.sberbank.school.task09;

public class UnknownCityException extends RuntimeException {
    public UnknownCityException(String name) {
        super("Unknown city with name: " + name);
    }
}
