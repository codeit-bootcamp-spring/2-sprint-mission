package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    UUID signUp(String name, UUID userKey);
    UUID login(String name, UUID userKey);
    Channel create(String category, String name, String introduction, UUID memberKey, UUID ownerKey);
    Channel read(String name);
    List<Channel> readAll(List<String> names);
    Channel update(String originName, String category, String name, String introduction, UUID userKey);
    void delete(String channelName, UUID userKey);
    String getChannelName(UUID channelKey);
}
