package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseService<T> {
    T create(T entity);
    Optional<T> findById(UUID id);
    List<T> findAll();
    T update(T entity);
    void delete(T entity);
    void deleteById(UUID id);
}

