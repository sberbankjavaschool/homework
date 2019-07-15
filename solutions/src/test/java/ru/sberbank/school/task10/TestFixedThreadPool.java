package ru.sberbank.school.task10;

import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.Before;
import org.junit.Test;


public class TestFixedThreadPool {

 //   private FixedThreadPool pool;
    private ScalableThreadPool pool;
    private final Object lock = new Object();
    private int count;
    private int thread_1;
    private int thread_2;
    private int thread_3;

    @Before
    public void initialize() {
       pool = new ScalableThreadPool(3, 5);
//        pool = new FixedThreadPool(3);
        pool.start();
    }

    @Test
    public void checkRepeatStart() {
        Assertions.assertThrows(IllegalStateException.class, () -> pool.start());
    }

    @Test
    public void checkIncrement() throws InterruptedException {
        for (int i = 0; i < 1000000; i++) {
            pool.execute(() -> {
                synchronized (lock) {
                    count++;
                }
            });
        }
        Thread.sleep(1000);

        Assertions.assertEquals(1000000, count);
    }

    @Test
    public void check() throws InterruptedException {
        for (int i = 0; i < 10000; i++) {
            pool.execute(() -> {
                synchronized (lock) {
                    if (Thread.currentThread().getName().equals("Thread-1")) {
                        thread_1++;
                    }
                    if (Thread.currentThread().getName().equals("Thread-2")) {
                        thread_2++;
                    }
                    if (Thread.currentThread().getName().equals("Thread-3")) {
                        thread_3++;
                    }
                }
            });
        }
        Thread.sleep(1000);

        Assertions.assertEquals(10000, thread_1 + thread_2 + thread_3);
    }

    @After
    public void stopPool() {
        pool.stopNow();
    }
}