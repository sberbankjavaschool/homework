package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.sberbank.school.util.Solution;

import java.util.LinkedList;

public class RouteSerializer extends Serializer<Route<City>> {
    @Override
    public void write(Kryo kryo, Output output, Route<City> object) {
        output.writeString(object.getRouteName());
        kryo.writeObjectOrNull(output, object.getCities(), LinkedList.class);
    }

    @Override
    public Route<City> read(Kryo kryo, Input input, Class<? extends Route<City>> type) {
        Route<City> result = kryo.newInstance(type);
        kryo.reference(result);

        result.setRouteName(input.readString());
        result.setCities(kryo.readObject(input, LinkedList.class));

        return result;
    }
}
