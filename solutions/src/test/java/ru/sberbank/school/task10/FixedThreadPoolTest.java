package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixedThreadPoolTest {
    private static FixedThreadPool fixedThreadPool;

    @BeforeAll
    public static void initialization() {
        fixedThreadPool = new FixedThreadPool(10);
        fixedThreadPool.start();
    }

    void start() {
        for (int i = 0; i < 10; i++) {
            fixedThreadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " is done");
            });
        }
    }


    @Test
    @DisplayName("checkThreads")
    void checkThreads() {
        start();
        fixedThreadPool.checkThreads();
        fixedThreadPool.stopNow();
        System.out.println("=======================");
        fixedThreadPool.checkThreads();
    }
}