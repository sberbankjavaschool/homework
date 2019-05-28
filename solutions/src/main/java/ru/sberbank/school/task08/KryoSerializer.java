package ru.sberbank.school.task08;

import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.ArrayList;
import java.util.List;

public class KryoSerializer extends Serializer<MapState<GameObject>> {

    @Override
    public void write(Kryo kryo, @NonNull Output output, @NonNull MapState<GameObject> object) {
        output.write(object.getGameObjects().size());
        output.writeString(object.getName());
        for (GameObject go : object.getGameObjects()) {
            output.writeString(go.getType().name());
            output.writeString(go.getStatus().name());
            output.writeLong(go.getHitPoints());
        }

    }

    @Override
    public MapState<GameObject> read(Kryo kryo, @NonNull Input input, Class<? extends MapState<GameObject>> type) {
        int size = input.readInt();
        String name = input.readString();
        List<GameObject> objectsList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            InstantiatableEntity.Status status = InstantiatableEntity.Status.valueOf(input.readString());
            InstantiatableEntity.Type typeIE = InstantiatableEntity.Type.valueOf(input.readString());
            Long hitPoints = input.readLong();
            objectsList.add(i, new GameObject(typeIE, status, hitPoints));
        }
        return new MapState<>(name, objectsList);
    }
}
