package ru.sberbank.school.task09.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.TimeSerializers;
import lombok.NonNull;
import ru.sberbank.school.task09.City;
import ru.sberbank.school.task09.Route;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

public class KryoManager implements RouteSerializeManager<Route<City>, City> {

    private Kryo kryo;
    private String directoryPath;

    public KryoManager(String directoryPath) {
        this.kryo = new Kryo();
        kryo.register(Route.class, new RouteSerializer());
        kryo.register(City.class, new CitySerializer());
        kryo.register(LinkedList.class);
        kryo.register(ArrayList.class);
        kryo.register(LocalDate.class);
        TimeSerializers.addDefaultSerializers(kryo);

        kryo.setReferences(true);

        this.directoryPath = directoryPath;
    }

    public void saveRoute(@NonNull String fileName, @NonNull Route<City> route) {
        try (Output output = new Output(new FileOutputStream(directoryPath + File.separator + fileName))) {

            kryo.writeClassAndObject(output, route);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Route<City> loadRoute(@NonNull String fileName) {
        Route<City> route = null;
        try (Input input = new Input(new FileInputStream(directoryPath + File.separator + fileName))) {

            route = (Route<City>) kryo.readClassAndObject(input);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return route;
    }
}
