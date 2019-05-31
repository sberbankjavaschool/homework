package ru.sberbank.school.task10;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface ThreadPool {
    /**
     * Запускает потоки, которыые бездействуют, до тех пор пока не появится новое задание в очереди (см. execute)
     */
    void start();

    /**
     * Отменяет запланированные задачи, останавливает запущенные задачи, останавливает потоки, переходит в начальное состояние.
     */
    void stopNow();

    /**
     * Складывает задание в очередь. Освободившийся поток должен выполнить это задание.
     * Каждое задание должны быть выполнено ровно 1 раз
     *
     * @param runnable Задача для выполнения
     */
    void execute(Runnable runnable);

    default <T> Future<T> execute(Callable<T> callable) {
        throw new UnsupportedOperationException();
    }
}
