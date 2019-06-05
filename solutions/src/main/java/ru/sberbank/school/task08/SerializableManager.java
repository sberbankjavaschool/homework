package ru.sberbank.school.task08;

import lombok.NonNull;
import ru.sberbank.school.task08.state.*;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.util.List;

@Solution(8)
public class SerializableManager extends SaveGameManager {
    /**
     * Конструктор не меняйте.
     */
    public SerializableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        throw new UnsupportedOperationException("Implement me!");
    }

    @Override
    public void saveGame(String filename, Savable gameState) throws SaveGameException {
        try (FileOutputStream fos = new FileOutputStream(filesDirectory + filename);
            ObjectOutputStream out = new ObjectOutputStream(fos)){
            out.writeObject(gameState); }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public Savable loadGame(String filename) throws SaveGameException {
        try (FileInputStream fis = new FileInputStream(filesDirectory + filename);
             ObjectInputStream in = new ObjectInputStream(fis)) {
            return (Savable) in.readObject();
        }
        catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public InstantiatableEntity createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status,
                                             long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public Savable createSavable(String name, List entities) {
        return new MapState<>(name, entities);
    }

}

