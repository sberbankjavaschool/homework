package ru.sberbank.school.task08;

import lombok.NonNull;
import ru.sberbank.school.task08.state.*;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//@Solution(8)
//public class SerializableManager extends SaveGameManager {
//    /**
//     * Конструктор не меняйте.
//     */
//    private int numberOfFile = 0;
//
//    public SerializableManager(@NonNull String filesDirectoryPath) {
//        super(filesDirectoryPath);
//    }
//
//    @Override
//    public void initialize() {
//        numberOfFile +=1 ;
//        InstantiatableEntity game = createEntity(InstantiatableEntity.Type.BUILDING,
//                InstantiatableEntity.Status.DESPAWNED, numberOfFile);
//        List<InstantiatableEntity> listEntitites = new ArrayList<>();
//        Savable<InstantiatableEntity> mapSavedEntitites = createSavable("saveGame"+numberOfFile, listEntitites);
//        System.out.println("Before serialization: " + game);
//        String filename = filesDirectory.concat("saveGame"+numberOfFile+".bin");
//        try {
//            saveGame(filename, mapSavedEntitites);
//            Savable loadedData = loadGame(filename);
//            System.out.println("After deserialization: " + loadedData);
//        } catch (SaveGameException ex) {
//
//        }
//
//    }
//
//    @Override
//    public void saveGame(String filename, Savable gameState) throws SaveGameException {
//        try (FileOutputStream fos = new FileOutputStream(filesDirectory+File.separator+filename);
//             ObjectOutputStream out = new ObjectOutputStream(fos))
//        {
//            out.writeObject(gameState);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    @Override
//    public Savable loadGame(String filename) throws SaveGameException {
//        try (FileInputStream fis = new FileInputStream(filesDirectory+File.separator+filename);
//             ObjectInputStream in = new ObjectInputStream(fis)) {
//            return (Savable) in.readObject();
//        } catch (IOException | ClassNotFoundException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public InstantiatableEntity createEntity(InstantiatableEntity.Type type,
//                                             InstantiatableEntity.Status status,
//                                             long hitPoints) {
//        return new GameObject(type, status, hitPoints);
//    }
//
//    @Override
//    public <GameObject extends InstantiatableEntity> Savable<GameObject> createSavable(String name,
//                                                                     List<GameObject> entities) {
//        return new MapState<GameObject>(name, entities);
//    }
//
//    public static void main(String[] args) {
//        SerializableManager serializableManager
//                = new SerializableManager("C:\\Users\\Anastasia\\Desktop\\Java\\serialize");
//        serializableManager.initialize();
//    }
//
//}
