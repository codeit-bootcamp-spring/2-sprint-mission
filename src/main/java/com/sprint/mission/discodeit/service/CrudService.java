package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

// 각 도메인(User,Channel,Message)의 CRUD(Create,Read,Update,Delete)는 공통적으로 필요
// 제네릭타입을 사용해 여러 도메인 모델에 적용할려고 함

public interface CrudService<T> {
    void create(T entity);
    T read(UUID id);
    List<T> readAll();
    void update(UUID id, T entity);
    void delete(UUID id);
}
