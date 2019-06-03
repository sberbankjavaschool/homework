package ru.sberbank.school.task10;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScalableThreadPoolTest {
    private static ScalableThreadPool scalableThreadPool;

    @BeforeAll
    public static void initialization() {
        scalableThreadPool = new ScalableThreadPool(2, 5);
        scalableThreadPool.start();
    }

    @Test
    void start() {
        for (int i = 0; i < 100; i++) {
            scalableThreadPool.execute(() -> {
                       // System.out.println(Thread.currentThread().getName() + " done");
                        try {
                            Thread.sleep(100);
                            System.out.println(Thread.currentThread().getName() + " is running");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
    }
}