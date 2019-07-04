package ru.sberbank.school.task13.beanfieldcopier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task13.BeanFieldCopier;
import ru.sberbank.school.task13.beanfieldcopier.util.MyAnotherUser;
import ru.sberbank.school.task13.beanfieldcopier.util.MyUser;
import ru.sberbank.school.task13.beanfieldcopier.util.MyUserChild;

class BeanFieldCopierImplTest {

    @Test
    void simpleCopy() {
        MyUser from = new MyUser("Alex", 25, true, new MyUser("Bob", 44, false, null));
        MyUser to = new MyUser();
        BeanFieldCopier beanFieldCopier = new BeanFieldCopierImpl();
        beanFieldCopier.copy(from, to);
        Assertions.assertEquals(from, to);
    }

    @Test
    void subClassCopy() {
        MyUser from = new MyUser("Alex", 25, true, new MyUser("Bob", 44, false, null));
        MyUser to = new MyUserChild();
        BeanFieldCopier beanFieldCopier = new BeanFieldCopierImpl();
        beanFieldCopier.copy(from, to);
        Assertions.assertEquals(from, to);
    }

    @Test
    void anotherClassCopy() {
        MyUser from = new MyUser("Alex", 25, true, new MyUser("Bob", 44, false, null));
        MyAnotherUser to = new MyAnotherUser();
        BeanFieldCopier beanFieldCopier = new BeanFieldCopierImpl();
        beanFieldCopier.copy(from, to);
        Assertions.assertEquals(from.getAge(), to.getAge());
        Assertions.assertEquals(from.getName(), to.getName());
        Assertions.assertEquals(from.isReady(), to.isReady());
        Assertions.assertNotEquals(from.getParent(), to.getParent());
    }

}

