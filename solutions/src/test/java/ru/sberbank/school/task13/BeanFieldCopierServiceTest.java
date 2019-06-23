package ru.sberbank.school.task13;

import lombok.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static ru.sberbank.school.task13.BeanFieldCopierServiceTest.ClimateZone.TEMPERATE;

class BeanFieldCopierServiceTest {

    BeanFieldCopier beanFieldCopier = new BeanFieldCopierService();
    Spruce spruce = new Spruce();

    {
        spruce.setEvergreen(true);
        spruce.setAge(119);
        spruce.setHeight(new BigDecimal(1024));
        spruce.setClimateZone(TEMPERATE.getKey());
    }

    @Test
    void copyTest() {
        Spruce result = new Spruce();
        beanFieldCopier.copy(spruce, result);
        Assertions.assertNotSame(spruce, result);
        Assertions.assertEquals(spruce, result);
        System.out.println(result.toString());
    }

    @Test
    void copyToSuperTest() {
        Plant result = new Plant();
        beanFieldCopier.copy(spruce, result);
        Assertions.assertNotSame(spruce, result);
        Assertions.assertEquals(spruce.toString(), result.toString());
        System.out.println(result.toString());
    }

    @Test
    void copyPartialTest() {
        Palm result = new Palm();
        beanFieldCopier.copy(spruce, result);
        Assertions.assertEquals(spruce.isEvergreen(), result.isEvergreen());
        Assertions.assertEquals(spruce.getHeight(), result.getHeight());
        Assertions.assertEquals(spruce.getClimateZone(), result.getClimateZone());
        Assertions.assertNotEquals(spruce.getAge(), result.getAge());
        System.out.println(result.toString());
    }

    @Data
    class Plant {
        static final String INFO = "1 level";
        boolean evergreen;
        int age;
        BigDecimal height;
        String climateZone;
        public String toString() {
            return "(evergreen=" + this.isEvergreen() + ", age=" + this.getAge() + ", " +
                    "height=" + this.getHeight() + ", climateZone=" + this.getClimateZone() + ")";
        }
    }

    @Data
    private class Tree extends BeanFieldCopierServiceTest.Plant {
        static final String INFO = "2 level";
        boolean evergreen;
        int age;
        BigDecimal height;
        String climateZone;
    }

    @Data
    private class Spruce extends BeanFieldCopierServiceTest.Tree {
        static final String INFO = "3 level";
        boolean evergreen;
        int age;
        BigDecimal height;
        String climateZone;
        public String toString() {
            return "(evergreen=" + this.isEvergreen() + ", age=" + this.getAge() + ", " +
                    "height=" + this.getHeight() + ", climateZone=" + this.getClimateZone() + ")";
        }
    }

    @Data
    class Palm {
        static final String INFO = "3.5 level";
        boolean evergreen;
        Integer age;
        BigDecimal height;
        String climateZone;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    enum ClimateZone {
        ARCTIC,
        SUBARCTIC,
        TEMPERATE("Temperate"),
        SUBTROPICAL,
        TROPICAL,
        EQUATORIAL;
        private String key;
    }
}