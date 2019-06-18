package ru.sberbank.school.task13.fieldcopier;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.sberbank.school.task13.BeanFieldCopier;

public class BeanFieldCopierImplTest {

    @Test
    public void testCopy() {
        BeanFieldCopier beanFieldCopier = new BeanFieldCopierImpl();
        Husky husky = new Husky();
        Dog dog = new Dog();

        beanFieldCopier.copy(husky, dog);

        System.out.println(dog);

        Assertions.assertEquals(husky.name, dog.name);
        Assertions.assertEquals(husky.age, dog.age);
        Assertions.assertEquals(husky.sex, dog.sex);
        Assertions.assertNull(husky.breed);
    }


    class Husky {
        private String name = "Leo";
        private int age = 8;
        private boolean sex = true;
        private String breed;

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public boolean getSex() {
            return sex;
        }

        public void doSomething() {

        }
    }

    class Dog {
        private String name;
        private int age;
        private boolean sex;

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setSex(boolean sex) {
            this.sex = sex;
        }

        public void doSomething() {

        }
    }
}
