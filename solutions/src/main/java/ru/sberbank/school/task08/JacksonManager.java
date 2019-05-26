package ru.sberbank.school.task08;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.util.Solution;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Solution(8)
public class JacksonManager extends SaveGameManager<MapState<GameObject>, GameObject> {
    private ObjectMapper mapper;

    /**
     * Класс-наследник должен иметь конструктор эквивалентный этому.
     *
     * @param filesDirectoryPath Путь до директории, в которой хранятся файлы сохранений
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
        Objects.requireNonNull(filename, "Имя файла не должно быть null");
        Objects.requireNonNull(gameState, "Сохраняемый объект не должен быть null");

        try {
            mapper.writeValue(new File(filesDirectory + File.separator + filename), gameState);
        } catch (NullPointerException | IOException e) {
            throw new SaveGameException("Возникла ошибка при записи объекта", e, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        Objects.requireNonNull(filename, "Имя файла не должно быть null");
        MapState savable = null;

        try {
            savable = mapper.readValue(new File(filesDirectory + File.separator + filename), MapState.class);
        } catch (IOException e) {
            throw new SaveGameException("Возникла ошибка при считывании объекта", e,
                    SaveGameException.Type.IO, savable);
        }

        return savable;
    }

    @Override
    public GameObject createEntity(InstantiatableEntity.Type type,
                                   InstantiatableEntity.Status status,
                                   long hitPoints) {
//        Objects.requireNonNull(type, "Тип не должен быть null");
//        Objects.requireNonNull(status, "Статус не должен быть null");
//        if (hitPoints < 0) {
//            throw new IllegalArgumentException("Урон не может быть меньше 0");
//        }

        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject> createSavable(String name, List<GameObject> entities) {
//        Objects.requireNonNull(name, "Название локации не должено быть null");
//        Objects.requireNonNull(entities, "Список объектов не должен быть null");

        return new MapState<>(name, entities);
    }

}
