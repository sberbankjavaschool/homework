package ru.sberbank.school.task08.state;

import lombok.Value;
import ru.sberbank.school.task08.ServiceManager;

import java.io.Serializable;

@Value
public class Score implements Serializable {
    private static Score EMPTY_SCORE = new Score(0,0, ServiceManager.scoreService);
    private long currentScore;
    private long maxScore;
    private transient ServiceManager.PlaceholderService scoreService;

}
