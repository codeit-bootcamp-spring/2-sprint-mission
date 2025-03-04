package com.sprint.sprint1.mission.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    void save(T entity);
    Optional<T> findByEmail(String email); // ID 기반 조회
    List<T> findAll(); //모든 엔티티 조회
    boolean deleteById(String id); //ID 기반 삭제


}
