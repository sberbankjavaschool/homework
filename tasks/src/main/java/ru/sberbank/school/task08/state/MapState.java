package ru.sberbank.school.task08.state;

import lombok.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Value
public class MapState implements Serializable {
   // private HashMap<GameObject.Type, List<GameObject>> gameObjects = new HashMap<>();

    private List<GameObject> gameObjects = new ArrayList<>();
}
