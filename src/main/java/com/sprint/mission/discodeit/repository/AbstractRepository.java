package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BaseEntity;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public abstract class AbstractRepository <T extends BaseEntity> implements Repository<T> {
    private final Class<T> entityClass;
    protected Map<UUID, T> storage;

    protected AbstractRepository(Class<T> entityClass, Map<UUID, T> map) {
        this.entityClass = entityClass;
        this.storage = map;
    }

    @Override
    public void add(T entity) {
        if (entity == null) {
            throw new NullPointerException("entity is null");
        }
        this.storage.put(entity.getId(), entity);
    }

    @Override
    public boolean existsById(UUID id) {
        if (id == null) {
            throw new NullPointerException(entityClass.getSimpleName() + "ID is null");
        }
        return this.storage.containsKey(id);
    }

    @Override
    public T findById(UUID id) {
        if (!existsById(id)) {
            throw new NoSuchElementException("해당 ID를 가진 " + entityClass.getSimpleName() + "가 존재하지 않습니다.");
        }
        return this.storage.get(id);
    }

    @Override
    public Map<UUID, T> getAll() {
        return this.storage;
    }

    @Override
    public void deleteById(UUID id) {
        if (!existsById(id)) {
            throw new NoSuchElementException("해당 ID를 가진 " + entityClass.getSimpleName() + "가 존재하지 않습니다.");
        }
        this.storage.remove(id);
    }
}
