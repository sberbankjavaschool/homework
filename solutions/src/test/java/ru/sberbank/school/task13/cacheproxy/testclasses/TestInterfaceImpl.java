package ru.sberbank.school.task13.cacheproxy.testclasses;

public class TestInterfaceImpl implements TestInterface {
    @Override
    public String toFileOneArg(String strArg) {
        return strArg + strArg;
    }

    @Override
    public String toRamOneArg(String strArg) {
        return strArg + strArg;
    }

    @Override
    public String toFileFirstArgIdent(String strArg, int intArg) {
        return strArg + intArg;
    }

    @Override
    public String toRamFirstArgIdent(String strArg, int intArg) {
        return strArg + intArg;
    }

    @Override
    public String toFileSecondArgIdent(String strArg, int intArg) {
        return strArg + intArg;
    }

    @Override
    public String toRamSecondArgIdent(String strArg, int intArg) {
        return strArg + intArg;
    }
}
