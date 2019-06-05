package ru.sberbank.school.task08;

import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.util.Solution;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;
import java.util.Objects;

@Solution(8)
public class JaxbManager extends SaveGameManager<MapState<GameObject>, GameObject> {

    private JAXBContext context;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    /**
     * Конструктор не меняйте.
     */
    public JaxbManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {

        try {
            context = JAXBContext.newInstance(MapState.class);
            marshaller = context.createMarshaller();
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            System.out.println(e.toString());
        }

    }

    @Override
    public void saveGame(String filename, MapState<GameObject> gameState) throws SaveGameException {

        Objects.requireNonNull(filename, "Parameter filename must be not null!");
        Objects.requireNonNull(gameState, "Parameter gameState must be not null!");

        String fullName = filesDirectory + "/" + filename;

        try {
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(gameState, new File(fullName));
        } catch (NullPointerException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.IO, gameState);
        } catch (JAXBException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.IO, gameState);
        }

    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {

        Objects.requireNonNull(filename, "Parameter filename must be not null!");

        String fullName = filesDirectory + "/" + filename;

        try {
            unmarshaller = context.createUnmarshaller();
            return (MapState) unmarshaller.unmarshal(new File(fullName));
        } catch (NullPointerException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.IO, null);
        } catch (JAXBException e) {
            throw new SaveGameException(e.toString(), SaveGameException.Type.IO, null);
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
