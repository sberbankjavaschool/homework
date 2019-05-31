package ru.sberbank.school.task09.util;

import lombok.NonNull;
import ru.sberbank.school.task09.City;
import ru.sberbank.school.task09.Route;

import java.io.*;

public class SerializeManager implements RouteSerializeManager<Route<City>, City> {
    private String directoryPath;

    public SerializeManager(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public void saveRoute(@NonNull String fileName, @NonNull Route<City> route) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(
                directoryPath + File.separator + fileName))) {

            outputStream.writeObject(route);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Route<City> loadRoute(@NonNull String fileName) {
        Route<City> route = null;

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(
                directoryPath + File.separator + fileName))) {

            route = (Route<City>) inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return route;
    }

}
