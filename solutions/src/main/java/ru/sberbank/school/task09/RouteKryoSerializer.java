package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

<<<<<<< HEAD
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
=======
import java.util.LinkedList;
>>>>>>> a74517ca69f0dbbb085e7da973da37a50ddcfb07
=======
import java.util.LinkedList;
>>>>>>> a74517ca69f0dbbb085e7da973da37a50ddcfb07

public class RouteKryoSerializer extends Serializer<Route<City>> {

    @Override
    public void write(Kryo kryo, Output output, Route<City> object) {
        output.writeString(object.getRouteName());
<<<<<<< HEAD
<<<<<<< HEAD
        //kryo.writeObjectOrNull(output, object.getCities(), LinkedList.class);
        kryo.writeObject(output, object.getCities());
=======
        kryo.writeObjectOrNull(output, object.getCities(), LinkedList.class);
>>>>>>> a74517ca69f0dbbb085e7da973da37a50ddcfb07
=======
        kryo.writeObjectOrNull(output, object.getCities(), LinkedList.class);
>>>>>>> a74517ca69f0dbbb085e7da973da37a50ddcfb07
    }

    @Override
    public Route<City> read(Kryo kryo, Input input, Class<? extends Route<City>> type) {
        Route<City> route = kryo.newInstance(type);
        route.setRouteName(input.readString());
<<<<<<< HEAD
<<<<<<< HEAD
        //route.setCities(kryo.readObjectOrNull(input, LinkedList.class));
        route.setCities((List<City>) kryo.readObject(input, ArrayList.class));
=======
        route.setCities(kryo.readObjectOrNull(input, LinkedList.class));
>>>>>>> a74517ca69f0dbbb085e7da973da37a50ddcfb07
=======
        route.setCities(kryo.readObjectOrNull(input, LinkedList.class));
>>>>>>> a74517ca69f0dbbb085e7da973da37a50ddcfb07
        return route;
    }
}
