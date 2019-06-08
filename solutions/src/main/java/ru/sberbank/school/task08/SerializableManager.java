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
    public void initialize() {}

    @Override
    public void saveGame(String filename, Savable gameState) throws SaveGameException {
        try (FileOutputStream fos = new FileOutputStream(filesDirectory + filename);
            ObjectOutputStream out = new ObjectOutputStream(fos)){
            out.writeObject(gameState); }
        catch (FileNotFoundException ex) {
            throw new SaveGameException("File not found", ex, SaveGameException.Type.USER, gameState);
        }
        catch (IOException ex) {
            throw new SaveGameException("IO error", ex, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public Savable loadGame(String filename) throws SaveGameException {
        Savable load = null;
        try (FileInputStream fis = new FileInputStream(filesDirectory + filename);
             ObjectInputStream in = new ObjectInputStream(fis)) {
             load = (Savable) in.readObject();
        }
        catch (FileNotFoundException ex) {
            throw new SaveGameException("File not found", ex, SaveGameException.Type.USER, load);
        }
        catch (IOException ex) {
            throw new SaveGameException("IO error", ex, SaveGameException.Type.IO, load);
        }
        catch (ClassNotFoundException ex) {
            throw new SaveGameException("desirialize class not found", ex, SaveGameException.Type.SYSTEM, load);
        }
        return load;
    }

    @Override
    public InstantiatableEntity createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status,
                                             long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public Savable createSavable(String name, List entities) {
        return new MapState<GameObject>(name, entities);
    }

}

