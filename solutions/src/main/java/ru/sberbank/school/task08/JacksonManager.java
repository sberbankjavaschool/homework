package ru.sberbank.school.task08;

//import com.sun.org.apache.bcel.internal.generic.RETURN;
//import lombok.NonNull;
//import ru.sberbank.school.task08.state.GameObject;
//import ru.sberbank.school.task08.state.InstantiatableEntity;
//import ru.sberbank.school.task08.state.MapState;
//import ru.sberbank.school.task08.state.Savable;
//import ru.sberbank.school.util.Solution;
//
//import com.fasterxml.jackson.core.JsonGenerationException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import sun.java2d.loops.DrawGlyphListAA;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@Solution(8)
//public class JacksonManager extends SaveGameManager {
//    private int numberOfFile = 0;
//    ObjectMapper mapper;
//    //private ObjectMapper objectMapper;
//
//    public JacksonManager(@NonNull String filesDirectoryPath) {
//        super(filesDirectoryPath);
//    }
//
//    @Override
//    public void initialize() {
//        numberOfFile += 1;
//        InstantiatableEntity game = createEntity(InstantiatableEntity.Type.BUILDING,
//                InstantiatableEntity.Status.DESPAWNED, numberOfFile);
//        List<InstantiatableEntity> listEntitites = new ArrayList<>();
//        Savable<InstantiatableEntity> mapSavedEntitites = createSavable("saveGame" + numberOfFile, listEntitites);
//        System.out.println("Before serialization: " + game);
//
//
//    }
//
//    @Override
//    public void saveGame(String filename, Savable gameState) throws SaveGameException {
//        mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//
//        try {
//            // Здесь происходит самая главная магия
//            mapper.writeValue(new File(filesDirectory + File.pathSeparator + filename), gameState);
//            System.out.println(mapper.createArrayNode());
//        } catch (JsonGenerationException exc) {
//            exc.printStackTrace();
//        } catch (JsonMappingException exc) {
//            exc.printStackTrace();
//        } catch (IOException exc) {
//            exc.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public Savable loadGame(String filename) throws SaveGameException {
//        try {
//            // Здесь происходит самая главная магия
//            return mapper.readValue(new File(filesDirectory + File.pathSeparator + filename), MapState.class);
//        } catch (JsonGenerationException exc) {
//            exc.printStackTrace();
//        } catch (JsonMappingException exc) {
//            exc.printStackTrace();
//        } catch (IOException exc) {
//            exc.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @Override
//    public InstantiatableEntity createEntity(InstantiatableEntity.Type type,
//                                             InstantiatableEntity.Status status,
//                                             long hitPoints) {
//        return new GameObject(type, status, hitPoints);
//    }
//
//    @Override
//    public <GameObject extends InstantiatableEntity> Savable<GameObject> createSavable(String name,
//                                                                                            List<GameObject> entities)
//    {
//        return new MapState<GameObject>(name, entities);
//    }
//
//    public static void main(String[] args) {
//        JacksonManager jacksonManager
//                = new JacksonManager("C:\\Users\\Anastasia\\Desktop\\Java\\serialize");
//        jacksonManager.initialize();
//    }
//}
