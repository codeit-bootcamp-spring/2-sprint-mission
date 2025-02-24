package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface CommonService<Entity> {
    void create(Entity entity);
    Entity find(UUID id);
    List<Entity> findAll();
    void update(UUID id, Entity entity);
    void delete(UUID id);
}
