package ru.sberbank.school.task07;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class CounterImplTest {

    @Test
    void count() {
        try {
            String path = "F:\\JAVA\\sberbank\\homework\\solutions\\src\\test\\resources\\task07.txt";
            CounterImpl counterImpl = new CounterImpl(new FileParserImpl());
            assertEquals(18, counterImpl.count(path));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound exception");
        }
    }
}