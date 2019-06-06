package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScalableThreadPoolTest {
    private static final ScalableThreadPool scalableThreadPool = new ScalableThreadPool(2, 4);

    @BeforeAll
    public static void initialization() {
        //scalableThreadPool = ;
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
    @DisplayName("checkIllegalArgumentException")
    void checkNullThreads() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ScalableThreadPool(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ScalableThreadPool(2, 1));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ScalableThreadPool(-1, 1));
    }

    @Test
    @DisplayName("checkActiveThreads")
    void checkActiveThreads() {
        start();
        scalableThreadPool.stopNow();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (scalableThreadPool) {
            boolean checkThreads = scalableThreadPool.checkScalableThreads();
            Assertions.assertEquals(checkThreads, true);
        }
    }

    @Test
    @DisplayName("checkCallAfterStop")
    void checkCallAfterStop() {
//        start();
//        scalableThreadPool.stopNow();
        synchronized (scalableThreadPool) {
            Assertions.assertThrows(RuntimeException.class, () -> scalableThreadPool.execute(() -> {
            }));
        }
    }

}