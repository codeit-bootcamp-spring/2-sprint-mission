package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    //CRUD 기능을 선언
    UUID create(String name);
    Channel findById(UUID id);
    List<Channel> findAll();
    void update(UUID id, String name);
    void delete(UUID id);
}
