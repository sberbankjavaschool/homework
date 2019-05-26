package ru.sberbank.school.task08;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;

public class KryoSerializer extends Serializer<MapState<GameObject>> {

    @Override
    public void write(Kryo kryo, Output output, MapState<GameObject> object) {

        output.writeString(object.getName());
        output.writeInt(object.getGameObjects().size());

        for (GameObject gameObject : object.getGameObjects()) {
            output.writeString(gameObject.getType().toString());
            output.writeString(gameObject.getStatus().toString());
            output.writeLong(gameObject.getHitPoints());
        }


    }

    @Override
    public MapState<GameObject> read(Kryo kryo, Input input,
                                      Class<? extends MapState<GameObject>> type) {

        String name = input.readString();
        int size = input.readInt();
        List<GameObject> gameObjects = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            InstantiatableEntity.Type t = InstantiatableEntity.Type.valueOf(input.readString());
            InstantiatableEntity.Status s = InstantiatableEntity.Status.valueOf(input.readString());
            long hp = input.readLong();
            gameObjects.add(new GameObject(t, s, hp));
        }

        return new MapState<>(name, gameObjects);

    }
}
