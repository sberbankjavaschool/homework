package ru.sberbank.school.task08;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.util.List;

import static java.io.File.separator;
import static ru.sberbank.school.task08.SaveGameException.Type.IO;

/**
 * 31.05.2019 сериализация с помощью Jackson
 *
 * @author Gregory Melnikov
 */

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
    public void saveGame(@NonNull String filename, @NonNull MapState<GameObject> gameState) throws SaveGameException {
        String path = filesDirectory + separator + filename;
        try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {

            objectMapper.writeValue(fileOutputStream, gameState);

        } catch (IOException | NullPointerException e) {
            throw new SaveGameException("JacksonManager Saving error", e, IO, gameState);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapState<GameObject> loadGame(@NonNull String filename) throws SaveGameException {
        String path = filesDirectory + separator + filename;
        try (FileInputStream fileInputStream = new FileInputStream(path)) {

            MapState<GameObject> saved = objectMapper.readValue(fileInputStream, MapState.class);
            return saved;

        } catch (IOException | NullPointerException | ClassCastException e) {
            throw new SaveGameException("JacksonManager Loading error", e, IO, null);
        }
    }

    @Override
    public GameObject createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status,
                                             long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapState<GameObject> createSavable(String name, List<GameObject> entities) {
        return new MapState<>(name, entities);
    }
}