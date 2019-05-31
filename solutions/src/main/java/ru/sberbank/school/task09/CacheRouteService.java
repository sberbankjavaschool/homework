package ru.sberbank.school.task09;

import ru.sberbank.school.task09.util.KryoManager;
import ru.sberbank.school.task09.util.RouteSerializeManager;

import java.io.File;
import java.util.Objects;

public class CacheRouteService extends InMemoryRouteService {

    private File cacheDirectory;
    private RouteSerializeManager serializeManager;

    public CacheRouteService(String directoryPath, RouteSerializeManager serializeManager) {
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

        String fileName = from + "_" + to + ".txt";
        Route<City> route = loadRouteFromCache(fileName);

        try {

            if (route == null) {
                route = super.getRouteInner(from, to);

                System.out.print("Save objects: ");
                long start = System.currentTimeMillis();

                serializeManager.saveRoute(fileName, route);

                System.out.println(System.currentTimeMillis() - start);
            }

        } catch (UnknownCityException e) {
            throw new RouteFetchException(e);
        }

        return route;
    }

    private Route<City> loadRouteFromCache(String fileName) {
        Route<City> route = null;

        File file = new File(cacheDirectory.getPath() + File.separator + fileName);

        if (file.exists()) {
            System.out.print("Load objects: ");
            long start = System.currentTimeMillis();

            route = serializeManager.loadRoute(fileName);

            System.out.println(System.currentTimeMillis() - start);
            System.out.println("File size: " + file.length());
        }

        return route;
    }

}
