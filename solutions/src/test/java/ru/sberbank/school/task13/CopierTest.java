package ru.sberbank.school.task13;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Mart
 * 20.07.2019
 **/
public class CopierTest {
    @Test
    public void testCopy() {
        BeanFieldCopier beanFieldCopier = new Copier();

        Human human = new Human(30, "Gennadiy", "Homo Naledi");
        Employee employee = new Employee();

        beanFieldCopier.copy(human, employee);
        System.out.println(human);
        System.out.println(employee);
        assertEquals(human.getName(), employee.getName());
        assertEquals(human.getName(), employee.getName());
    }
}
