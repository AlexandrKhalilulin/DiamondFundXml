package com.epam.ak.model.model;

import java.util.UUID;

public abstract class BaseModel {
    private UUID uuid;
    private int id;

    public BaseModel() {
    }

    public BaseModel(UUID uuid, int id) {
        this.uuid = UUID.randomUUID();
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseModel)) return false;

        BaseModel baseModel = (BaseModel) o;

        if (id != baseModel.id) return false;
        return !(uuid != null ? !uuid.equals(baseModel.uuid) : baseModel.uuid != null);

    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "uuid=" + uuid +
                ", id=" + id +
                '}';
    }
}
