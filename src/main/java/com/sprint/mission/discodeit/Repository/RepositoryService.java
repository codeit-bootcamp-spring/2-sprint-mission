package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.entity.Server;

import java.util.List;

public interface RepositoryService <T extends BaseEntity, R extends BaseEntity> {
    public abstract List<R> repository(T t);
    public abstract void add(T t, R r);
    public abstract void remove(T t);
    public abstract List<R> print(T t);
    public abstract void update(T t);
    public abstract void search(T t);
    public abstract R getHead();

}
