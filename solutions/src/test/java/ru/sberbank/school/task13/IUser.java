package ru.sberbank.school.task13;

import ru.sberbank.school.task13.util.Cache;
import ru.sberbank.school.task13.util.CacheType;

public interface IUser {
    @Cache(cacheType = CacheType.FILE, fileNameOrKey = "", zip = true, identityBy = {Integer.class, String.class})
    IUser getUserFromFile(int age, String name);

    String getName();

    @Cache(cacheType = CacheType.MEMORY, fileNameOrKey = "rename", zip = false, identityBy = {IUser.class})
    String rename(IUser newName);

    @Cache(cacheType = CacheType.MEMORY, fileNameOrKey = "newUser", zip = false, identityBy = {Integer.class, String.class})
    IUser getUserFromMemory(int age, String name);
}