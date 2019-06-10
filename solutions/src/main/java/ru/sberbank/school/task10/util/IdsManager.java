package ru.sberbank.school.task10.util;

import java.util.*;

public class IdsManager {
    private final Queue<Integer> freeIds = new LinkedList<>();
    private volatile int mainId;
    private volatile int maxId;

    public IdsManager(int maxId) {
        this.maxId = maxId;
    }

    public void addFreeId(int id) {
        freeIds.add(id);
    }

    public void reset() {
        mainId = 0;
        freeIds.clear();
    }

    public int getId() {
        synchronized (freeIds) {
            if (freeIds.isEmpty()) {
                if (mainId < maxId) {
                    return mainId++;
                }
                return maxId;
            }
            return freeIds.poll();
        }
    }
}
