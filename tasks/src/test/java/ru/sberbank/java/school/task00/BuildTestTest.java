package ru.sberbank.java.school.task00;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BuildTestTest {
    @Test
    public void test() {
        assertEquals(4, BuildTest.sum(2, 2));
    }

    @Test
    public void testToFail() {
        assertEquals(2, BuildTest.sum(1, 1));
    }
}
