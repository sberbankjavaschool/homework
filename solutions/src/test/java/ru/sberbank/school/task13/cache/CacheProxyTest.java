package ru.sberbank.school.task13.cache;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class CacheProxyTest {
    private static final String DIRECTORY_PATH = "F:\\testDirectory";
    private static CacheProxy cacheProxy;
    private static Person person;
    private static IPerson personProxy;

    @BeforeAll
    static void init() {
        cacheProxy  = new CacheProxy(DIRECTORY_PATH);
        person = new Person("Аристарх", 55, new Temp(36.364758d));
        personProxy = (IPerson) cacheProxy.cache(person);
    }

    @AfterAll
    static void cleanUp() {
        try {
            File fileDirectory = new File(DIRECTORY_PATH);
            for (File file : fileDirectory.listFiles()) {
                file.delete();
            }
        } catch (NullPointerException e) {
            //noop
        }

    }


    @Test
    @DisplayName("Выброс исключения при создании прокси с некорректной дирректорией.")
    void incorrectFilesDirectory() {
        assertThrows(NullPointerException.class, () -> new CacheProxy(null));
        assertThrows(IllegalArgumentException.class, () -> new CacheProxy("*"));
    }

    @Test
    @DisplayName("Проверка кеширования в JVM.")
    void cacheToJvm() {
        person.setHair("рыжий");
        String hair1 = personProxy.combineHair("кудрявый");
        person.setHair("Джигурда");
        String hair2 = personProxy.combineHair("кудрявый");

        System.out.println(hair1 + " - hair1");
        System.out.println(hair2 + " - hair2");

        assertEquals(hair1, hair2);
    }

    @Test
    @DisplayName("Проверка кеширования в файл")
    void cacheToFile() {
        Temp temp1 = personProxy.getSuperTemp();
        person.setSuperTemp(new Temp(38.277646d));
        Temp temp2 = personProxy.getSuperTemp();

        System.out.println(temp1 + " - temp1");
        System.out.println(temp2 + " - temp2");

        assertNotEquals(temp1, temp2);
        assertEquals(temp1.toString(), temp2.toString());
    }

    @Test
    @DisplayName("Проверка кеширования в zip")
    void cacheToZip() {
        Details det1 = personProxy.getNameAndAge();
        person.setDetails(new Details("Гена",20));
        Details det2 = personProxy.getNameAndAge();

        System.out.println(det1 + " - det1");
        System.out.println(det2 + " - det2");
        assertNotEquals(det1, det2);
        assertEquals(det1.toString(), det2.toString());
    }



}