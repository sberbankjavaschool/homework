package ru.sberbank.school.task13.cacheproxy.testclasses;

import ru.sberbank.school.task13.cacheproxy.annotations.Cache;
import ru.sberbank.school.task13.cacheproxy.annotations.CacheType;

public interface TestInterface {
    @Cache(cacheType = CacheType.FILE, fileNamePrefix = "test")
    String toFileOneArg(String strArg);

    @Cache(cacheType = CacheType.RAM)
    String toRamOneArg(String strArg);

    @Cache(cacheType = CacheType.FILE, fileNamePrefix = "test", identityBy = {String.class})
    String toFileFirstArgIdent(String strArg, int intArg);

    @Cache(cacheType = CacheType.RAM, identityBy = {String.class})
    String toRamFirstArgIdent(String strArg, int intArg);

    @Cache(cacheType = CacheType.FILE, fileNamePrefix = "test", identityBy = {Integer.class})
    String toFileSecondArgIdent(String strArg, int intArg);

    @Cache(cacheType = CacheType.RAM, identityBy = {Integer.class})
    String toRamSecondArgIdent(String strArg, int intArg);
}
