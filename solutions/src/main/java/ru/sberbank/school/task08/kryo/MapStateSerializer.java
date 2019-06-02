package ru.sberbank.school.task08.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;


public class MapStateSerializer extends Serializer<MapState<GameObject>> {
    @Override
    public void write(Kryo kryo, Output output, MapState<GameObject> object) {
        output.writeString(object.getName());

        kryo.writeClassAndObject(output, object.getGameObjects());
//        int volume = object.getGameObjects().size();
//        output.writeInt(volume);
//
//        for (GameObject gameObject : object.getGameObjects()) {
//            kryo.writeClassAndObject(output, gameObject.getType());
//            kryo.writeClassAndObject(output, gameObject.getStatus());
//            kryo.writeClassAndObject(output, gameObject.getHitPoints());
//        }

    }

    @Override
    public MapState read(Kryo kryo, Input input, Class<? extends MapState<GameObject>> type) {


        String name = input.readString();
//        int volume = input.readInt();
//
//        List<GameObject> list = new ArrayList<>();
//
//        for (int i = 0; i < volume; i++) {
//            InstantiatableEntity.Type type1 = (InstantiatableEntity.Type) kryo.readClassAndObject(input);
//            InstantiatableEntity.Status status = (InstantiatableEntity.Status) kryo.readClassAndObject(input);
//            long hitPoints = (long) kryo.readClassAndObject(input);
//
//            GameObject gameObject = new GameObject(type1, status, hitPoints);
//            list.add(gameObject);
//        }
        @SuppressWarnings("unchecked")
        List<GameObject> list = (List<GameObject>) kryo.readClassAndObject(input);

        return new MapState(name, list);
    }
}
