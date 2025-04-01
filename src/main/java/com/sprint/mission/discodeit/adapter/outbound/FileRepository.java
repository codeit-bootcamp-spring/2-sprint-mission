package com.sprint.mission.discodeit.adapter.outbound;

public interface FileRepository<T>{
    void init();

    T load();

    void save(T list);

}
