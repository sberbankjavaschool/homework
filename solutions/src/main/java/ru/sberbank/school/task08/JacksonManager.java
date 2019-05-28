package ru.sberbank.school.task08;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.List;
import java.util.Objects;

import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

@Solution(8)
public class JacksonManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    private ObjectMapper mapper;

    /**
     * Конструктор не меняйте.
     */
    public JacksonManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        mapper = new ObjectMapper();
    }

    @Override
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {

        Objects.requireNonNull(filename, "Parameter filename must be not null!");
        Objects.requireNonNull(gameState, "Parameter gameState must be not null!");

        String fullName = filesDirectory + "/" + filename;

        try (FileOutputStream output = new FileOutputStream(fullName)) {
            mapper.writeValue(output, gameState);
        } catch (FileNotFoundException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.USER, null);
        } catch (IOException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.IO, null);
        }

    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {

        Objects.requireNonNull(filename, "Parameter filename must be not null!");

        String fullName = filesDirectory + "/" + filename;

        try (FileInputStream input = new FileInputStream(fullName)) {
            return mapper.readValue(input, new TypeReference<MapState<GameObject>>(){});
        } catch (FileNotFoundException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.USER, null);
        } catch (IOException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.IO, null);
        }

    }

    @Override
    public InstantiatableEntity createEntity(InstantiatableEntity.Type type, InstantiatableEntity.Status status, long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject>  createSavable(String name, List<GameObject> entities) {
        return new MapState<>(name, entities);
    }
}
