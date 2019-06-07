package ru.sberbank.school.task10;

import lombok.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class FixedThreadPool extends AbstractThreadPool {
    protected FixedThreadPool(int core) {
        super(core, core);
    }

    /**
     * Складывает задание в очередь. Освободившийся поток должен выполнить это задание.
     * Каждое задание должны быть выполнено ровно 1 раз
     *
     * @param runnable Задача для выполнения
     */
    @Override
    public void execute(@NonNull Runnable runnable) {
        if (workers.isEmpty()) {
            throw new IllegalStateException("нельзя добавить задачу в остановленный ThreadPool");
        }
        synchronized (tasks) {
            tasks.add(runnable);
            tasks.notify();
        }
    }
}
