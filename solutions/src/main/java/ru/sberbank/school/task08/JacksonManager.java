package ru.sberbank.school.task08;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.util.List;
import java.util.Objects;

@Solution(8)
public class JacksonManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    private ObjectMapper objectMapper;

    public JacksonManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {
        Objects.requireNonNull(filename, "File name is not to be null");
        Objects.requireNonNull(gameState, "Game state is not to be null");

        String path = filesDirectory + "/" + filename;

        try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {

            objectMapper.writeValue(fileOutputStream, gameState);

        } catch (FileNotFoundException e) {
            throw new SaveGameException("The path was not found", e, SaveGameException.Type.SYSTEM, gameState);
        } catch (IOException e) {
            throw new SaveGameException("Fail or interrupt I/O operations", e, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        Objects.requireNonNull(filename, "File name is not to be null");

        String path = filesDirectory + "/" + filename;

        MapState<GameObject> gameState = null;

        try (FileInputStream fileInputStream = new FileInputStream(path)) {

            gameState = objectMapper.readValue(fileInputStream, new TypeReference<MapState<GameObject>>(){});
            return gameState;

        } catch (FileNotFoundException e) {
            throw new SaveGameException("The file was not found", e, SaveGameException.Type.SYSTEM, gameState);
        } catch (IOException e) {
            throw new SaveGameException("Fail or interrupt I/O operations", e, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public GameObject createEntity(InstantiatableEntity.Type type,
                                   InstantiatableEntity.Status status,
                                   long hitPoints) {

        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject> createSavable(String name, List<GameObject> entities) {
        return new MapState<>(name, entities);
    }
}
