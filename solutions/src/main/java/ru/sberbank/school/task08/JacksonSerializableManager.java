package ru.sberbank.school.task08;

import com.fasterxml.jackson.core.type.TypeReference;
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
public class JacksonSerializableManager extends SaveGameManager {

    private ObjectMapper objectMapper;
    private String separator = File.separator;

    public JacksonSerializableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        objectMapper = new ObjectMapper();
        if (filesDirectory.equals("")) {
            separator = "";
        }
    }

    @Override
    public void saveGame(@NonNull String filename,
                         @NonNull Savable gameState) throws SaveGameException {
        try {
            objectMapper.writeValue(new File(filesDirectory + separator + filename), gameState);
        } catch (FileNotFoundException e) {
            throw new SaveGameException("Открыть файл не удалось.",
                    SaveGameException.Type.IO, gameState);
        } catch (IOException e) {
            throw new SaveGameException("Ощибка записи класса.",
                    SaveGameException.Type.SYSTEM, gameState);
        }
    }

    @Override
    public Savable loadGame(@NonNull String filename) throws SaveGameException {
        MapState savable = null;
        try {
            File fis = new File(filesDirectory + separator + filename);
            savable = objectMapper.readValue(fis, new TypeReference<MapState<GameObject>>(){});
        } catch (FileNotFoundException e) {
            throw new SaveGameException("Файл не найден!",
                    SaveGameException.Type.IO, savable);
        } catch (IOException e) {
            throw new SaveGameException("Ошибка чтения класса.",
                    SaveGameException.Type.USER, savable);
        }

        return savable;
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
