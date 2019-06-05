package ru.sberbank.school.task08.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.sberbank.school.task08.KryoSerializableManager;
import ru.sberbank.school.task08.KryoSerializerMapState;
import ru.sberbank.school.task08.SaveGameException;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.Savable;

import java.util.ArrayList;
import java.util.List;

public class MainKryo {
    public static void main(String[] args) throws SaveGameException {
        KryoSerializableManager kryoSerializableManager = new KryoSerializableManager("D:\\");
        kryoSerializableManager.initialize();
        List<GameObject> list = new ArrayList<>();

        list.add(new GameObject(InstantiatableEntity.Type.BUILDING, InstantiatableEntity.Status.DESPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.ENEMY, InstantiatableEntity.Status.SPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.ITEM, InstantiatableEntity.Status.SPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.NPC, InstantiatableEntity.Status.SPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.BUILDING, InstantiatableEntity.Status.KILLED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.NPC, InstantiatableEntity.Status.DESPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.ENEMY, InstantiatableEntity.Status.KILLED, 5000L));
        Savable savable = kryoSerializableManager.createSavable("game", null);
        kryoSerializableManager.saveGame("game.bin", savable);
        Savable load = kryoSerializableManager.loadGame("game.bin");
        System.out.println(load.equals(savable));
    }
}
