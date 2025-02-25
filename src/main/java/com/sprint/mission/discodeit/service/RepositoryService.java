package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BaseEntity;

public interface RepositoryService <T extends BaseEntity, R extends BaseEntity> {
    public void add(T t, R r);
    public void remove(T t, R r);
    public void print(T t);
    public void search(T t);
    public void update(T t);
}
