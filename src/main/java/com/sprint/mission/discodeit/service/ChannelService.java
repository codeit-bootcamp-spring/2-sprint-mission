package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    UUID signUp(String name, UUID userId);
    UUID create(String category, String name, String introduction, UUID memberUuid, UUID ownerUuid);
    Channel read(String name);
    List<Channel> readAll(List<String> nameList);
    List<Channel> readAll();
    Channel update(String originName, String category, String name, String introduction);
    void delete(String channelName, UUID userUuid);
    UUID getChannelKey(String inputName, UUID userKey);
    String getChannelName(UUID channelKey);
}
