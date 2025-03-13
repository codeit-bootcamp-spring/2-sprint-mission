package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface ServerService {
    UUID createChannel(String serverId, String creatorId, String name);

    UUID joinChannel(String serverId, String userId, String channelId);

    UUID quitChannel(String serverId, String userId, String channelId);

    boolean printUsers(String serverId);

    boolean printChannels(String serverId);

    boolean printUsersInChannel(String channelId);

    boolean removeChannel(String serverId, String creatorId, String channelId);

    boolean updateChannelName(String serverId, String creatorId, String channelId, String replaceName);
}
