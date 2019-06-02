package ru.sberbank.school.task08;

import com.esotericsoftware.kryo.io.Input;
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

    }

    @Override
    public void saveGame(String filename, Savable gameState) {
        try (FileOutputStream fos = new FileOutputStream(filesDirectory + filename);
             ObjectOutputStream out = new ObjectOutputStream(fos)) {
            out.writeObject(gameState);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Savable loadGame(String filename) {

        try (FileInputStream fis = new FileInputStream(filesDirectory + filename);
             ObjectInputStream mapState = new ObjectInputStream(fis)) {
            return (MapState) mapState.readObject();
        } catch (IOException | ClassNotFoundException ex) {
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
