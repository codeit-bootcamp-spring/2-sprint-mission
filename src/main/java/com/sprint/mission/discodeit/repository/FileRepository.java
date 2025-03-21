package com.sprint.mission.discodeit.repository;

public interface FileRepository<T>{
    void init();

    T load();

    void save(T list);

}
