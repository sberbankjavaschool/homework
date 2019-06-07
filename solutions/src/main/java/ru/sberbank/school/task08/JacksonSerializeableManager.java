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

@Solution(8)
public class JacksonSerializeableManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    public JacksonSerializeableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    private ObjectMapper objectMapper;

    @Override
    public void initialize() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void saveGame(String filename, MapState gameState) throws SaveGameException {
        try {
            File file = new File(filesDirectory + filename);
            objectMapper.writeValue(file, gameState);
        } catch (IOException ex) {
            throw new SaveGameException("IO error", ex, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        MapState<GameObject> load = null;
        try {
            File file = new File(filesDirectory + filename);
            TypeReference<MapState<GameObject>> tRef = new TypeReference<MapState<GameObject>>() {};
            load = objectMapper.readValue(file, tRef);
        } catch (IOException ex) {
            throw new SaveGameException("IO error", ex, SaveGameException.Type.IO, load);
        }
        return load;
    }

    @Override
    public GameObject createEntity(InstantiatableEntity.Type type, InstantiatableEntity.Status status, long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject> createSavable(String name, List entities) {
        return new MapState<GameObject>(name, entities);
    }
}
    /*ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping();
                objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
                objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
                objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);*/