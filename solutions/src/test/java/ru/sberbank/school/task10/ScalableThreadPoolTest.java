package ru.sberbank.school.task10;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScalableThreadPoolTest {
    private ScalableThreadPool threadPool;

    @BeforeAll
    void init() {
        threadPool = new ScalableThreadPool(1, 3);
    }


    @Test
    @DisplayName("Тест с нагрузкой")
    void startManyThreads() {
        ScalableThreadPool threadPool = new ScalableThreadPool(1, 3);
        threadPool.start();

        for (int i = 0; i < 20; i++) {
            threadPool.execute(() -> {
                        try {
                            Thread.sleep(500);
                            System.out.println(Thread.currentThread().getName() + " done");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
    }

    @Test
    @DisplayName("Тест без расширения пула")
    void startOneThread() {
        ScalableThreadPool threadPool = new ScalableThreadPool(1, 3);
        threadPool.start();

        for (int i = 0; i < 20; i++) {
            threadPool.execute(() -> {
                        try {
                            Thread.sleep(500);
                            System.out.println(Thread.currentThread().getName() + " done");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}