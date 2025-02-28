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
    //이 상황에서 findbyId의 UUID id가 적절한지 delete (UUID id)가 나은지 delete (T entity)가 더 적절한지 .?
}

