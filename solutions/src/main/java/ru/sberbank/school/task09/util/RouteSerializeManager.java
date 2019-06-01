package ru.sberbank.school.task09.util;

import ru.sberbank.school.task09.City;
import ru.sberbank.school.task09.Route;

public interface RouteSerializeManager<T extends Route<C>, C extends City> {

    void saveRoute(String fileName, T route);

    T loadRoute(String fileName);
}
