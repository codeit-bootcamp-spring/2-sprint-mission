package com.sprint.mission.discodeit.Repository.file;

public interface FileRepository<T>{
    void init();

    T load();

    void save(T list);

}
