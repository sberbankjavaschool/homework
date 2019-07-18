package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.ArrayList;
import java.util.List;

public class RouteKryoSerializer extends Serializer<Route<City>> {

    @Override
    public void write(Kryo kryo, Output output, Route<City> object) {
        output.writeString(object.getRouteName());
        //kryo.writeObjectOrNull(output, object.getCities(), LinkedList.class);
        kryo.writeObject(output, object.getCities());
    }

    @Override
    public Route<City> read(Kryo kryo, Input input, Class<? extends Route<City>> type) {
        Route<City> route = kryo.newInstance(type);
        route.setRouteName(input.readString());
        //route.setCities(kryo.readObjectOrNull(input, LinkedList.class));
        route.setCities((List<City>) kryo.readObject(input, ArrayList.class));
        return route;
    }
}
