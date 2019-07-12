package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import ru.sberbank.school.task06.CountMap;
import ru.sberbank.school.task06.CountMapImpl;

import java.util.Map;

class TestsThreadPool {

    private final Object lock = new Object();
    private int count;
    private CountMap<String> countMapThreads;

    TestsThreadPool() {
        countMapThreads = new CountMapImpl();
    }

    void checkRepeatStart(ThreadPool pool) {
        Assertions.assertThrows(IllegalStateException.class, pool::start);
    }

    void checkIncrement(ThreadPool pool, int countTasks, int sleep) throws InterruptedException {
        for (int i = 0; i < countTasks; i++) {
            pool.execute(() -> {
                synchronized (lock) {
                    count++;
                }
            });
        }
        Thread.sleep(sleep);
        Assertions.assertEquals(countTasks, count);
    }

    void checkAdequacy(ThreadPool pool, int countTasks, int sleep) throws InterruptedException {

        for (int i = 0; i < countTasks; i++) {
            pool.execute(() -> {
                synchronized (lock) {
                    countMapThreads.add(Thread.currentThread().getName());
                }
            });
        }
        Thread.sleep(sleep);

        int countCompletedTasks = 0;
        Map<String, Integer> mapThreads = countMapThreads.toMap();
        for (String threadName : mapThreads.keySet()) {
            countCompletedTasks += countMapThreads.getCount(threadName);
        }

        System.out.println(mapThreads.toString());
        Assertions.assertEquals(countTasks, countCompletedTasks);
    }
}
