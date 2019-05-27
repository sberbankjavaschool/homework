package ru.sberbank.school.task08.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class GameObject implements InstantiatableEntity, Serializable {
    private final Type type;
    private final Status status;
    private long hitPoints;

    private GameObject() {
        type = null;
        status = null;
    }

    @JsonCreator
    public GameObject(@JsonProperty("type") Type type,
                      @JsonProperty("status") Status status,
                      @JsonProperty("hitPoints") long hitPoints) {
        this.type = type;
        this.status = status;
        this.hitPoints = hitPoints;
    }

    public Type getType() {
        return this.type;
    }

    public Status getStatus() {
        return this.status;
    }

    public long getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(long hitPoints) {
        this.hitPoints = hitPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameObject)) {
            return false;
        }
        GameObject that = (GameObject) o;
        return type == that.type && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, status);
    }

    public String toString() {
        return "GameObject(type=" + this.getType()
                + ", status=" + this.getStatus()
                + ", hitPoints=" + this.getHitPoints() + ")";
    }

}
