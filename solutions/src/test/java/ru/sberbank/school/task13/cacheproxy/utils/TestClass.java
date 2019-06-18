package ru.sberbank.school.task13.cacheproxy.utils;

import ru.sberbank.school.task13.cacheproxy.Cache;
import ru.sberbank.school.task13.beanfieldcopier.utils.Player;

public interface TestClass {
    @Cache(cacheType = Cache.Cachetype.MEMORY)
    String getResFromMemory();

    @Cache(cacheType = Cache.Cachetype.MEMORY)
    String getResFromMemoryWithArgs(String smth, int i);

    @Cache(cacheType = Cache.Cachetype.FILE)
    String getResFromFile();

    @Cache(cacheType = Cache.Cachetype.FILE, fileNamePrefix = "withArgsTest", uniqueFields = {String.class})
    String getResFromFileWithArgs(String smth, int i);

    @Cache(cacheType = Cache.Cachetype.FILE, fileNamePrefix = "withPlayersTest")
    String getResFromFileWithPlayers(Player player1, Player player2);

    @Cache(cacheType = Cache.Cachetype.FILE, fileNamePrefix = "zipTest", zip = true)
    String getResFromZip();

    int getCountCalls();

}
