package ru.sberbank.school.task09;

import lombok.NonNull;
import ru.sberbank.school.task09.util.KryoManager;
import ru.sberbank.school.task09.util.RouteSerializeManager;
import ru.sberbank.school.util.Solution;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Solution(9)
public class CacheRouteService extends RouteService<City, Route<City>> {

    private File cacheDirectory;
    private RouteSerializeManager serializeManager;

    public CacheRouteService(String directoryPath, RouteSerializeManager serializeManager) {
        super(directoryPath);
        Objects.requireNonNull(directoryPath, "Путь к папке с файлами сохранения не должен быть null");

        if (directoryPath.isEmpty()) {
            throw new IllegalArgumentException("Путь к папке с файлами сохранения не должен быть задан пустой строкой");
        }

        cacheDirectory = new File(directoryPath);

        if (!cacheDirectory.isDirectory()) {
            throw new IllegalArgumentException("Файл по указанному пити не является директорией");
        }

        this.serializeManager = serializeManager;
    }

    public CacheRouteService(String directoryPath) {
        this(directoryPath, new KryoManager(directoryPath));
    }

    public Route<City> getRoute(String from, String to) {
        Objects.requireNonNull(from, "Город отправления не должен быть null");
        Objects.requireNonNull(to, "Город прибытия не должен быть null");

        String fileName = from + "_" + to;
        Route<City> route = loadRouteFromCache(fileName);

        if (route == null) {
            route = super.getRouteInner(from, to);
            serializeManager.saveRoute(fileName, route);
        }

        return route;
    }

    private Route<City> loadRouteFromCache(String fileName) {
        Route<City> route = null;

        File file = new File(path + File.separator + fileName);

        if (file.exists()) {
            route = serializeManager.loadRoute(fileName);
        }

        return route;
    }

    @Override
    protected City createCity(int id, @NonNull String cityName, @NonNull LocalDate foundDate,
                              long numberOfInhabitants) {
        return new City(id, cityName, foundDate, numberOfInhabitants);
    }

    @Override
    protected Route<City> createRoute(List<City> cities) {
        Objects.requireNonNull(cities, "Список городов не должен быть null");
        if (cities.isEmpty()) {
            throw new IllegalArgumentException("Список городов не должен быть пустым");
        }
        return new Route<>(UUID.randomUUID().toString(), cities);
    }
}
