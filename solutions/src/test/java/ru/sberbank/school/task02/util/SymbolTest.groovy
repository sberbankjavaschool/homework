package ru.sberbank.school.task02.util

import static ru.sberbank.school.task02.util.Symbol.*

class SymbolTest extends GroovyTestCase {

    void testGetSymbol1() {
        String target = "USD/RUB";
        String value = USD_RUB.getSymbol();
        assertEquals(target, value);
    }

    void testGetSymbol2() {
        String target = "RUB/USD";
        String value = RUB_USD.getSymbol();
        assertEquals(target, value);
    }

    void testIsCross1() {
        assertEquals(false, USD_RUB.isCross());
    }

    void testIsCross2() {
        assertEquals(true, RUB_USD.isCross());
    }
}
