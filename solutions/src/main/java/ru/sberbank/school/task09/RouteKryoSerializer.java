package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;

public class RouteKryoSerializer extends Serializer<Route<City>> {

    @Override
    public void write(Kryo kryo, @NonNull Output output, @NonNull Route<City> object) {
        output.writeString(object.getRouteName());
        kryo.writeObjectOrNull(output, object.getCities(), LinkedList.class);
    }

    @Override
    public Route<City> read(Kryo kryo, @NonNull Input input, Class<? extends Route<City>> type) {
        Route route = kryo.newInstance(type);
        kryo.reference(route);
        route.setRouteName(input.readString());
        route.setCities(kryo.readObjectOrNull(input, LinkedList.class));
        return route;
//        String name = input.readString();
//        List<City> cityObjects = kryo.readObjectOrNull(input, LinkedList.class);
//        return new Route<>(name, cityObjects);
    }
}
