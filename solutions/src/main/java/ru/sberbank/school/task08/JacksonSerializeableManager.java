package ru.sberbank.school.task08;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.task08.state.Savable;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.util.List;

@Solution(8)
public class JacksonSerializeableManager extends SaveGameManager {

    public JacksonSerializeableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void saveGame(String filename, Savable gameState) throws SaveGameException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
        try {
            File file = new File(filesDirectory + filename);
            objectMapper.writeValue(file, gameState);
        } catch (IOException ex) {

        }
    }

    @Override
    public Savable loadGame(String filename) throws SaveGameException {
        MapState mapState = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
        try {
            File file = new File(filesDirectory + filename);
            mapState = objectMapper.readValue(file, MapState.class);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return mapState;
    }

    @Override
    public InstantiatableEntity createEntity(InstantiatableEntity.Type type, InstantiatableEntity.Status status, long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public Savable createSavable(String name, List entities) {
        return new MapState(name, entities);
    }
}
    /*ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping();
                objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
                objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
                objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);*/