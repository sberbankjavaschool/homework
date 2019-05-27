package ru.sberbank.school.task08;

import ru.sberbank.school.task08.state.GameObject;

import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.task08.state.Savable;
import ru.sberbank.school.util.Solution;

import java.util.ArrayList;
import java.util.List;

public class TestSerializableManager {

    public static void main(String[] args) {
        String path = "C:\\Users\\Anastasia\\Desktop\\Java\\serialize";
        SerializableManager serializableManager = new SerializableManager(path);
        serializableManager.initialize();
        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(serializableManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.SPAWNED, 100));
        gameObjects.add(serializableManager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.KILLED, 200));
        gameObjects.add(serializableManager.createEntity(InstantiatableEntity.Type.ENEMY,
                InstantiatableEntity.Status.SPAWNED, 400));
        gameObjects.add(serializableManager.createEntity(InstantiatableEntity.Type.ITEM,
                InstantiatableEntity.Status.KILLED, 10));
        gameObjects.add(serializableManager.createEntity(InstantiatableEntity.Type.BUILDING,
                InstantiatableEntity.Status.DESPAWNED, 0));
        gameObjects.add(serializableManager.createEntity(InstantiatableEntity.Type.NPC,
                InstantiatableEntity.Status.DESPAWNED, 500));

        MapState<GameObject> mapStateGameObjects = serializableManager.createSavable("Some_name", gameObjects);
        System.out.println("Before serialization: " + mapStateGameObjects);
        try {
            serializableManager.saveGame("SerializibaleManager.bin", mapStateGameObjects);
        } catch (SaveGameException ex) {
            ex.printStackTrace();
        }
        try {
            MapState<GameObject> afterSerMap = serializableManager.loadGame("SerializibaleManager.bin");
            System.out.println("After serialization: " + afterSerMap);

        } catch (SaveGameException ex) {
            ex.printStackTrace();
        }


    }

}

