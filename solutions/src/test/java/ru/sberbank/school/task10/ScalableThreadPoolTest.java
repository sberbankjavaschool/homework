package ru.sberbank.school.task10;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScalableThreadPoolTest {
    private static ScalableThreadPool scalableThreadPool;

    @BeforeAll
    public static void initialization() {
        scalableThreadPool = new ScalableThreadPool(2, 4);
        scalableThreadPool.start();
    }

    @Test
    void start() {
        for (int i = 0; i < 6; i++) {
            scalableThreadPool.execute(() -> {
                        try {
                            Thread.sleep(1);
                            System.out.println(Thread.currentThread().getName() + " is done");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
    }
}