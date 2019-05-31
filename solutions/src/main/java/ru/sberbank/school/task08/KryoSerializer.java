package ru.sberbank.school.task08;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KryoSerializer extends Serializer<MapState<GameObject>> {

    @Override
    public void write(Kryo kryo, @NonNull Output output, @NonNull MapState<GameObject> object) {
        output.writeString(object.getName());
        List<GameObject> objects = object.getGameObjects();
        int size = objects != null ? objects.size() : -1;

        output.writeInt(size);

        if (size != -1) {
            for (GameObject g : object.getGameObjects()) {
                output.writeString(g.getType().name());
                output.writeString(g.getStatus().name());
                output.writeLong(g.getHitPoints());
            }
        }
    }

    @Override
    public MapState<GameObject> read(Kryo kryo, @NonNull Input input, Class<? extends MapState<GameObject>> type) {
        String name = input.readString();
        int size = input.readInt();

        if (size == -1) {
            return new MapState<>(name, null);
        }

        List<GameObject> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            InstantiatableEntity.Type t = InstantiatableEntity.Type.valueOf(input.readString());
            InstantiatableEntity.Status s = InstantiatableEntity.Status.valueOf(input.readString());
            long l = input.readLong();
            list.add(new GameObject(t, s, l));
        }

        return new MapState<>(name, list);
    }
}
