package ru.sberbank.school.task10;

import org.junit.jupiter.api.*;


class ScalableThreadPoolTest {
    private static ScalableThreadPool threadPool;

    @BeforeAll
    static void init() {
        threadPool = new ScalableThreadPool(1, 3);
        threadPool.start();
    }

    @Test
    @Disabled
    @DisplayName("Тест без расширения пула")
    void startOneThread() {

        for (int i = 0; i < 10; i++) {
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

    @Test
    @DisplayName("Тест с нагрузкой")
    void startManyThreads() {

        for (int i = 0; i < 10; i++) {
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

}