package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void signUp(String name, UUID userId);
    UUID create(String category, String name, String introduction, UUID memberUuid, UUID ownerUuid);
    Channel read(String name);
    List<String> getOwnerChannelName(UUID userUuid);
    List<Channel> readAll(List<String> nameList);
    void update(String originName, String category, String name, String introduction);
    void delete(String channelName, UUID userUuid);
}
