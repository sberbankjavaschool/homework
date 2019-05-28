package ru.sberbank.school.task08;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
public class JacksonSaveGameManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    private  ObjectMapper mapper;

    public JacksonSaveGameManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        mapper = new ObjectMapper();
    }

    @Override
    public void saveGame(String filename, MapState<GameObject> mapState) throws SaveGameException {
        Objects.requireNonNull(filename, "Filename should be provided");
        Objects.requireNonNull(mapState, "No MapState provided");

        String fullFileName = filesDirectory + File.separator + filename;

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fullFileName), mapState);
        } catch (JsonProcessingException e) {
            throw new SaveGameException("Jackson exception", e, SaveGameException.Type.IO, mapState);
        } catch (IOException e) {
            throw new SaveGameException("IO exception", e, SaveGameException.Type.IO, mapState);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        Objects.requireNonNull(filename, "Filename should be provided");
        String fullFileName = filesDirectory + File.separator + filename;

        try {
            return (MapState<GameObject>) mapper.readValue(new File(fullFileName), MapState.class);
        } catch (JsonParseException e) {
            throw new SaveGameException("Jackson parse exception", e, SaveGameException.Type.IO, null);
        } catch (JsonMappingException e) {
            throw new SaveGameException("Jackson mapping exception", e, SaveGameException.Type.IO, null);
        } catch (IOException e) {
            throw new SaveGameException("IO exception", e, SaveGameException.Type.IO, null);
        }
    }

    @Override
    public GameObject createEntity(InstantiatableEntity.Type type, InstantiatableEntity.Status status,
                                   long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject> createSavable(String name, List<GameObject> entities) {
        return new MapState<>(name, entities);
    }
}
