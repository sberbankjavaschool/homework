package ru.sberbank.school.task10;

import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;

class FixedThreadPoolTest {

    @Test
    void workingTest() {
        FixedThreadPool pool = new FixedThreadPool(10);
        pool.start();
        for (int i = 0; i < 100; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("Thread " + Thread.currentThread().getName() + " is working");
                        //Thread.sleep(3000);
                    } finally {

                    }
                }
            });
        }
       // pool.stopNow();
    }
}