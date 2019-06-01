package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

public class KryoCacheService extends CacheService {

    private final Kryo kryo = new Kryo();

    public KryoCacheService(String filesDirectory) {
        super(filesDirectory);
    }

    @Override
    public void initialize() {
        kryo.setReferences(true);
        kryo.register(Route.class, new RouteKryoSerializer());
        kryo.register(ArrayList.class);
        kryo.register(LinkedList.class);
        kryo.register(City.class, new CityKryoSerializer());
        kryo.register(LocalDate.class);
    }

    @Override
    public void save(String filename, Route<City> route) {
        Objects.requireNonNull(filename, "Filename should be provided");
        Objects.requireNonNull(route, "No routes provided");

        try (OutputStream os = new FileOutputStream(filesDirectory + File.separator + filename);
                Output output = new Output(os)) {

            kryo.writeObject(output, route);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Route<City>> load(String filename) {
        Objects.requireNonNull(filename, "Filename should be provided");
        Route<City> route = null;

        File file = new File(filesDirectory + File.separator + filename);
        if (file.exists()) {
            try (InputStream is = new FileInputStream(filesDirectory + File.separator + filename);
                    Input input = new Input(is)) {
                route = (Route<City>) kryo.readObjectOrNull(input, Route.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.ofNullable(route);
    }
}
