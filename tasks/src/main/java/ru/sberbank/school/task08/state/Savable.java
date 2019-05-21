package ru.sberbank.school.task08.state;

import java.io.Serializable;

public interface Savable extends Serializable {

    Score getScore();

    void setScore(Score score);


}
