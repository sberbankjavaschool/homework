package ru.sberbank.school.task09.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.sberbank.school.task09.City;
import ru.sberbank.school.task09.Route;

import java.util.LinkedList;
import java.util.List;

public class RouteSerializer extends Serializer<Route<City>> {

//    private String routeName;
//    private List<C> cities = new LinkedList<>();


    @Override
    public void write(Kryo kryo, Output output, Route<City> route) {
        output.writeString(route.getRouteName());
        kryo.writeObjectOrNull(output, route.getCities(), LinkedList.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Route<City> read(Kryo kryo, Input input, Class<? extends Route<City>> type) {
        Route route = kryo.newInstance(type);
//        kryo.setReferences(true);
        kryo.reference(route);
        route.setRouteName(input.readString());
        route.setCities((List) kryo.readClassAndObject(input));
        return route;
    }
}
