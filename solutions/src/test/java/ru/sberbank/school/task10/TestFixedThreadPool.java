package ru.sberbank.school.task10;

import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import ru.sberbank.school.task06.CountMap;
import ru.sberbank.school.task06.CountMapImpl;

import java.util.Map;


public class TestFixedThreadPool {

 //   private FixedThreadPool pool;
    private ScalableThreadPool pool;
    private final Object lock = new Object();
    private int count;
    private int thread_1;
    private int thread_2;
    private int thread_3;
    private CountMap<String> countMapThreads;

    @Before
    public void initialize() {
       pool = new ScalableThreadPool(3, 5);
       countMapThreads = new CountMapImpl<>();
//        pool = new FixedThreadPool(3);
        pool.start();
    }

    @Test
    public void checkAdequacy() throws InterruptedException {
        checkAdequacy(pool, 10000, 1000);
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

    @After
    public void stopPool() {
        pool.stopNow();
    }
}