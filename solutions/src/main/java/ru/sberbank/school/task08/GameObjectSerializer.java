package ru.sberbank.school.task08;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.sberbank.school.task08.state.GameObject;

import static ru.sberbank.school.task08.state.InstantiatableEntity.*;

/**
 * Created by Mart
 * 03.06.2019
 **/
public class GameObjectSerializer extends Serializer<GameObject> {
    @Override
    public void write(Kryo kryo, Output output, GameObject object) {
        output.writeLong(object.getHitPoints());
        kryo.writeObjectOrNull(output, object.getType(), Type.class);
        kryo.writeObjectOrNull(output, object.getStatus(), Status.class);
    }

    @Override
    public GameObject read(Kryo kryo, Input input, Class<? extends GameObject> type) {
        long hitPoints = input.readLong();
        Type gameObjectType = kryo.readObjectOrNull(input, Type.class);
        Status gameObjectStatus = kryo.readObjectOrNull(input, Status.class);
        return new GameObject(gameObjectType, gameObjectStatus, hitPoints);
    }
}
