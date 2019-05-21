package ru.sberbank.school.task08;

import lombok.Value;

import java.util.UUID;

public class ServiceManager {
    public static PlaceholderService scoreService;
    public static PlaceholderService enemyService;
    public static PlaceholderService weaponService;

    @Value
    public static class PlaceholderService {
        private String uuid = UUID.randomUUID().toString();
    }
}
