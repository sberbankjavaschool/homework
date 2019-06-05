package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScalableThreadPoolTest {
    private static ScalableThreadPool scalableThreadPool;

    @BeforeAll
    public static void initialization() {
        scalableThreadPool = new ScalableThreadPool(2, 4);
        scalableThreadPool.start();
    }

    void start() {
        for (int i = 0; i < 6; i++) {
            scalableThreadPool.execute(() -> {
                        System.out.println(Thread.currentThread().getName() + " is done");
                    }
            );
        }
    }


    @Test
    @DisplayName("checkThreads")
    void checkThreads() {
        start();
        scalableThreadPool.checkThreads();
        scalableThreadPool.stopNow();
        scalableThreadPool.checkThreads();
        scalableThreadPool.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " try");
        });
        //System.out.println("=======================");
        //scalableThreadPool.checkThreads();
    }

}