package ru.sberbank.school.task08;

import ru.sberbank.school.task08.state.*;

import java.io.*;
import java.util.List;
import java.util.Objects;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

@Solution(8)
public class SerializableManager extends SaveGameManager<MapState<GameObject>, GameObject> {
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
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {

        Objects.requireNonNull(filename, "Parameter filename must be not null!");
        Objects.requireNonNull(gameState, "Parameter gameState must be not null!");

        String fullName = filesDirectory + "/" + filename;

        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fullName))) {

            output.writeObject(gameState);

        } catch (FileNotFoundException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.USER, gameState);
        } catch (IOException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.IO, gameState);
        }

    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {

        Objects.requireNonNull(filename, "Parameter filename must be not null!");

        String fullName = filesDirectory + "/" + filename;
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(fullName))) {

            return (MapState<GameObject>) input.readObject();
            
        } catch (FileNotFoundException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.USER, null);
        }  catch (ClassNotFoundException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.SYSTEM, null);
        } catch (IOException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.IO, null);
        }
    }

    @Override
    public InstantiatableEntity createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status,
                                             long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject> createSavable(String name, List<GameObject> entities) {
        return new MapState<>(name, entities);
    }

}
