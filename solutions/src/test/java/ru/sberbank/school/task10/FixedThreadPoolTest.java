package ru.sberbank.school.task10;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixedThreadPoolTest {
    private static FixedThreadPool fixedThreadPool;

    @BeforeAll
    public static void initialization() {
        fixedThreadPool = new FixedThreadPool(5);
        fixedThreadPool.start();
    }

    @Test
    void start() {
        for (int i = 0; i < 10; i++) {
            fixedThreadPool.execute(() -> {});
        }
    }
}