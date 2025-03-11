package com.sprint.mission.discodeit.repository;

import java.util.Map;
import java.util.UUID;

public interface Repository<T> {
    void add(T entity);
    boolean existsById(UUID id);
    T findById(UUID id);
    Map<UUID, T> getAll();
    void deleteById(UUID id);
}
