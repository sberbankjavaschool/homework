package ru.sberbank.school.task13.cache;

public interface IPerson {
    @Cache(serviceName = "name")
    String getName();

    void setName(String name);

    @Cache(identityBy = String.class)
    String combineHair(String newHair);

    @Cache(cacheType = CacheType.FILE, zip = true, serviceName = "Details", identityBy = {Details.class})
    Details getNameAndAge();

    @Cache(cacheType = CacheType.FILE, zip = true, serviceName = "Details")
    void setDetails(Details details);

    @Cache(cacheType = CacheType.FILE, serviceName = "SuperTemp", identityBy = Temp.class)
    void setSuperTemp(Temp superTemp);

    @Cache(cacheType = CacheType.FILE, serviceName = "SuperTemp")
    Temp getSuperTemp();
}
