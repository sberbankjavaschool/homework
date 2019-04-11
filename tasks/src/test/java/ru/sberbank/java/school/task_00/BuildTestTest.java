package ru.sberbank.java.school.task_00;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BuildTestTest {
    @Test
    public void test() {
        assertEquals(4, BuildTest.sum(2,2));
    }
}
