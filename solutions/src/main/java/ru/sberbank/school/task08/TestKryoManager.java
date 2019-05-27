package ru.sberbank.school.task08;

import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.ArrayList;
import java.util.List;

public class TestKryoManager {

    public static void main(String[] args) {
        String path = "C:\\Users\\Anastasia\\Desktop\\Java\\serialize";
        KryoManager kryoManager = new KryoManager(path);
        kryoManager.initialize();
        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(kryoManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.SPAWNED, 100));
        gameObjects.add(kryoManager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.KILLED, 200));
        gameObjects.add(kryoManager.createEntity(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.SPAWNED, 400));
        gameObjects.add(kryoManager.createEntity(InstantiatableEntity.Type.ITEM,
                InstantiatableEntity.Status.KILLED, 10));
        gameObjects.add(kryoManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.DESPAWNED, 0));
        gameObjects.add(kryoManager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.DESPAWNED, 500));

        MapState<GameObject> mapStateGameObjects = kryoManager.createSavable("Some_name_kryo", gameObjects);
        System.out.println("Before serialization: " + mapStateGameObjects);
        try {
            kryoManager.saveGame("KryoManager.bin", mapStateGameObjects);
        } catch (SaveGameException ex) {
            ex.printStackTrace();
        }
        try {
            MapState<GameObject> afterSerMap = kryoManager.loadGame("KryoManager.bin");
            System.out.println("After serialization: " + afterSerMap);

        } catch (SaveGameException ex) {
            ex.printStackTrace();
        }


    }

}

