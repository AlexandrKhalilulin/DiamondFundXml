package com.epam.ak.model.model;

import java.util.UUID;

public abstract class BaseEntity {
    private UUID uuid;
    private int id;

    public BaseEntity() {
    }

    public BaseEntity(UUID uuid, int id) {
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
        if (!(o instanceof BaseEntity)) return false;

        BaseEntity baseEntity = (BaseEntity) o;

        if (id != baseEntity.id) return false;
        return !(uuid != null ? !uuid.equals(baseEntity.uuid) : baseEntity.uuid != null);

    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "uuid=" + uuid +
                ", id=" + id +
                '}';
    }
}
