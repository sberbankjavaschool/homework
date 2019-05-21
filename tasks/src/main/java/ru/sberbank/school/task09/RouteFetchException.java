package ru.sberbank.school.task09;

/**
 * Исключение на случай, если что-то пойдет не так с извлечением закешированного маршрута.
 */
public class RouteFetchException extends RuntimeException {
    public RouteFetchException(Throwable cause) {
        super("Can't fetch cached route!", cause);
    }
}
