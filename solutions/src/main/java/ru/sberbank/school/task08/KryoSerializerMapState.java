package ru.sberbank.school.task08;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.task08.state.Savable;

import java.util.ArrayList;
import java.util.List;

public class KryoSerializerMapState extends Serializer<MapState> {

    @Override
    public void write(Kryo kryo, Output output, MapState object) {
        List<GameObject> list = object.getGameObjects();
        output.writeString(object.getName());
        if (list != null) {
            output.write(list.size());
            for (GameObject gameObject : list) {
                kryo.writeClassAndObject(output, gameObject.getType());
                kryo.writeClassAndObject(output, gameObject.getStatus());
                output.writeLong(gameObject.getHitPoints());
            }
        }
    }

    @Override
    public MapState read(Kryo kryo, Input input, Class<? extends MapState> type) {
        List<GameObject> list = new ArrayList<>();
        String name = input.readString();
        int size = input.read();
        for (int i = 0; i < size; i++) {
            list.add(new GameObject(
                    (InstantiatableEntity.Type) kryo.readClassAndObject(input),
                    (InstantiatableEntity.Status) kryo.readClassAndObject(input),
                    input.readLong())
            );
        }
        return new MapState<>(name, (size == -1) ? null : list);
    }
}
