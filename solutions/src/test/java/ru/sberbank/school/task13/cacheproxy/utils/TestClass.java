package ru.sberbank.school.task13.cacheproxy.utils;

import ru.sberbank.school.task13.cacheproxy.Cache;
import ru.sberbank.school.task13.beanfieldcopier.utils.Player;

public interface TestClass {
    @Cache(cacheType = Cache.cacheType.MEMORY)
    String getResFromMemory();

    @Cache(cacheType = Cache.cacheType.MEMORY)
    String getResFromMemoryWithArgs(String smth, int i);

    @Cache(cacheType = Cache.cacheType.FILE)
    String getResFromFile();

    @Cache(cacheType = Cache.cacheType.FILE, fileNamePrefix = "withArgsTest", uniqueFields = {String.class})
    String getResFromFileWithArgs(String smth, int i);

    @Cache(cacheType = Cache.cacheType.FILE, fileNamePrefix = "withPlayersTest")
    String getResFromFileWithPlayers(Player player1, Player player2);

    @Cache(cacheType = Cache.cacheType.FILE, fileNamePrefix = "zipTest", zip = true)
    String getResFromZip();

    int getCountCalls();

}
