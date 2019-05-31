package ru.sberbank.school.task09.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Generics;
import ru.sberbank.school.task09.Route;

import java.util.List;

public class RouteSerializer extends Serializer<Route> {
    private final Generics.GenericsHierarchy genericsHierarchy;

    public RouteSerializer() {
        genericsHierarchy = new Generics.GenericsHierarchy(Route.class);
    }

    @Override
    public void write(Kryo kryo, Output output, Route object) {
        Generics generics = kryo.getGenerics();
        int pop = 0;
        Generics.GenericType[] genericTypes = generics.nextGenericTypes();
        if (genericTypes != null) {
            pop = generics.pushTypeVariables(genericsHierarchy, genericTypes);
        }

        output.writeString(object.getRouteName());

        kryo.writeClassAndObject(output, object.getCities());

        if (pop > 0) {
            generics.popTypeVariables(pop);
        }
        generics.popGenericType();
    }

    @Override
    public Route read(Kryo kryo, Input input, Class<? extends Route> type) {
        Generics generics = kryo.getGenerics();
        int pop = 0;
        Generics.GenericType[] genericTypes = generics.nextGenericTypes();
        if (genericTypes != null) {
            pop = generics.pushTypeVariables(genericsHierarchy, genericTypes);
        }

        Route object = new Route();
        kryo.reference(object);


        object.setRouteName(input.readString());

        object.setCities((List) kryo.readClassAndObject(input));

        if (pop > 0) {
            generics.popTypeVariables(pop);
        }
        generics.popGenericType();
        return object;
    }
}
