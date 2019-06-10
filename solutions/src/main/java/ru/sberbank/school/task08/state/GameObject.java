package ru.sberbank.school.task08.state;

import java.io.Serializable;
import java.util.Objects;

public class GameObject implements InstantiatableEntity, Serializable {
    private Type type;
    private Status status;
    private long hitPoints;

    public GameObject() {
    }

    public GameObject(Type type, Status status, long hitPoints) {
        this.type = type;
        this.status = status;
        this.hitPoints = hitPoints;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        return "GameObject(type=" + this.getType() + ", status=" + this.getStatus() + ")";
    }

}
