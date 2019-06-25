package ru.sberbank.school.task13.copier;

import org.junit.jupiter.api.*;

class CopierTest {

    private static Pet cat;
    private static Pet dog;
    private static Animal tiger;
    private static AnimalTrainer trainer;
    private static Person girl;
    private static Copier copier;

    @BeforeEach
    void init() {
        cat = new Pet("Pushok", 1, 9, "Pers", true);
        dog = new Pet("Rex", 3, 15, "Dog", false);
        tiger = new Animal("1", 2, 50);
        trainer = new AnimalTrainer("Vasya", Byte.valueOf("30"), 75, tiger);
        girl = new Person("Masha", Byte.valueOf("8"), 30, cat);
        copier = new Copier();
    }

    @Test
    void copyPetToPet() {
        copier.copy(cat, dog);
        Assertions.assertEquals(cat.getBreed(), dog.getBreed());
        Assertions.assertEquals(cat.getName(), dog.getName());
        Assertions.assertEquals(cat.getAge(), dog.getAge());
        Assertions.assertEquals(cat.getWeight(), dog.getWeight());
        Assertions.assertEquals(cat.getCode(), dog.getCode());
        Assertions.assertEquals(cat.isFluffy(), dog.isFluffy());
    }

    @Test
    void copyPetToAnimal() {
        copier.copy(dog, tiger);
        Assertions.assertEquals("Rex3", tiger.getCode());
        Assertions.assertEquals(Integer.valueOf(3), tiger.getAge());
        Assertions.assertEquals(Integer.valueOf(15), tiger.getWeight());
    }

    @Test
    @DisplayName("Проверка неполного копирования.")
    void copyPersonToAnimalTrainer() {
        copier.copy(girl, trainer);
        // Поля name и age копируются
        Assertions.assertEquals("Masha", trainer.getName());
        Assertions.assertEquals(Byte.valueOf("8"), trainer.getAge());
        // Поле weight не копируется, т.к. разный модификатор доступа
        Assertions.assertEquals(Integer.valueOf(75), trainer.getWeight());
        // Копирование объекта Pet в Animal
        Assertions.assertEquals(girl.getAnimal().getCode(), trainer.getAnimal().getCode());
        Assertions.assertEquals(girl.getAnimal().getAge(), trainer.getAnimal().getAge());
        Assertions.assertEquals(girl.getAnimal().getWeight(), trainer.getAnimal().getWeight());
    }
}
