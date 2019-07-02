package ru.sberbank.school.task09;


/**
 * Created by Mart
 * 27.06.2019
 **/
public interface RouteSerializeManager<T extends Route<C>, C extends City> {
    void saveRoute(String fileName, T route);

    T loadRoute(String fileName);
}
