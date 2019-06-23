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
    void copyToSameTest() {
        Spruce result = new Spruce();
        beanFieldCopier.copy(spruce, result);
        Assertions.assertNotSame(spruce, result);
        Assertions.assertEquals(spruce, result);
        System.out.println(result.toString());
    }

    @Test
    void copyToSuperTest() {
        Plant plant = new Plant();
        beanFieldCopier.copy(spruce, plant);
        Assertions.assertNotSame(spruce, plant);
        Assertions.assertEquals(spruce.toString(), plant.toString());
        System.out.println(plant.toString());
    }

    @Test
    void copyToSuperExtendedTest() {
        SprucePlantation sprucePlantation = new SprucePlantation();
        sprucePlantation.setPlant(spruce);
        Plantation plantation = new Plantation();
        beanFieldCopier.copy(sprucePlantation, plantation);
        Assertions.assertEquals(sprucePlantation.getPlant().toString(), plantation.getPlant().toString());
        System.out.println(sprucePlantation.toString());
        System.out.println(plantation.toString());
    }

    @Test
    void copyAutoboxingTest() {
        Palm palm = new Palm();
        beanFieldCopier.copy(spruce, palm);
        Assertions.assertEquals(spruce.isEvergreen(), palm.getEvergreen());
        Assertions.assertEquals(spruce.getAge(), palm.getAge());
        Assertions.assertEquals(spruce.getHeight(), palm.getHeight());
        Assertions.assertEquals(spruce.getClimateZone(), palm.getClimateZone());
        System.out.println(palm.toString());
    }

    @Test
    void copyPartialTest() {
        XenoTree xenoTree = new XenoTree();
        beanFieldCopier.copy(spruce, xenoTree);
        Assertions.assertEquals(spruce.isEvergreen(), xenoTree.isEvergreen());
        Assertions.assertEquals(spruce.getAge(), xenoTree.getAge());
        Assertions.assertNotEquals(spruce.getHeight(), xenoTree.getHeight());
        System.out.println(spruce.toString());
        System.out.println(xenoTree.toString());
    }

    @Data
    class Plant {
        static final String INFO = "1 level";
        boolean evergreen;
        Integer age;
        BigDecimal height;
        String climateZone;

        public String toString() {
            return "(evergreen=" + this.isEvergreen() + ", age=" + this.getAge() + ", "
                    + "height=" + this.getHeight() + ", climateZone=" + this.getClimateZone() + ")";
        }
    }

    private class Tree extends BeanFieldCopierServiceTest.Plant {
        static final String INFO = "2 level";
    }

    private class Spruce extends BeanFieldCopierServiceTest.Tree {
        static final String INFO = "3 level";
    }

    @Data
    class Palm {
        static final String INFO = "another classification";
        Boolean evergreen;
        int age;
        BigDecimal height;
        String climateZone;
    }

    @Data
    class XenoTree {
        static final String INFO = "level DANGER";
        boolean evergreen;
        Integer age;
        String height;
        String spaceZone;
    }

    @Data
    class SprucePlantation {
        Spruce plant;
    }

    @Data
    class Plantation {
        Plant plant;
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