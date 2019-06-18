package ru.sberbank.school.task13.beanfieldcopier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.school.task13.BeanFieldCopier;
import ru.sberbank.school.task13.beanfieldcopier.utils.Player;

class BeanFieldCopierImplTest {
    private BeanFieldCopier beanFieldCopier = new BeanFieldCopierImpl();

    @Test
    public void testNonEqualObjects() {
        Player player1 = new Player(20, "Ivanov", "CSKA", "no");
        Integer object = 1;
        Assertions.assertThrows(IllegalArgumentException.class, () -> beanFieldCopier.copy(player1, object));
    }

    @Test
    public void testEqualPlayers() {
        Player player1 = new Player(20, "Ivanov", "CSKA", "no");
        Player player2 = new Player();
        beanFieldCopier.copy(player1, player2);
        Assertions.assertNotEquals(player1.toStringAllFields(), player2.toStringAllFields());
        Assertions.assertEquals(player1.toString(), player2.toString());
    }

}