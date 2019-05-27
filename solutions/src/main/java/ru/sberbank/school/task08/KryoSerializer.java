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

public class KryoSerializer extends Serializer<MapState<GameObject>> {

    @Override
    public void write(Kryo kryo, @NonNull Output output, @NonNull MapState<GameObject> object) {

        output.writeString(object.getName());
        int size = object.getGameObjects() != null ? object.getGameObjects().size() : -1;
        output.writeInt(size);

        if (size != -1) {
            for (GameObject gameObject : object.getGameObjects()) {
                output.writeInt(gameObject.getType().getCode());
                output.writeInt(gameObject.getStatus().getCode());
                output.writeLong(gameObject.getHitPoints());
            }
        }


    }

    @Override
    public MapState<GameObject> read(Kryo kryo, @NonNull Input input,
                                      Class<? extends MapState<GameObject>> type) {

        String name = input.readString();
        int size = input.readInt();
        if (size == -1) {
            return new MapState<>(name, null);
        }

        List<GameObject> gameObjects = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            int code = input.readInt();
            InstantiatableEntity.Type loadType = findTypeByCode(code);
            code = input.readInt();
            InstantiatableEntity.Status loadStatus = findStatusByCode(code);
            long hp = input.readLong();
            gameObjects.add(new GameObject(loadType, loadStatus, hp));
        }

        return new MapState<>(name, gameObjects);

    }

    private InstantiatableEntity.Type findTypeByCode(int code) {

        for (InstantiatableEntity.Type type : InstantiatableEntity.Type.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;

    }

    private InstantiatableEntity.Status findStatusByCode(int code) {

        for (InstantiatableEntity.Status status : InstantiatableEntity.Status.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;

    }
}
