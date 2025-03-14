package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface ServerService {
    /**
     * 저장소를 초기화합니다.
     *
     * @param adminAuth 권한 여부
     * */
    void reset(boolean adminAuth);

    UUID createChannel(String serverId, String creatorId, String name);

    UUID joinChannel(String serverId, String userId, String ownerId,String channelId);

    UUID quitChannel(String serverId, String userId, String channelId);

    boolean printUsers(String serverId);

    boolean printChannels(String serverId);

    boolean printUsersInChannel(String channelId);

    boolean removeChannel(String serverId, String creatorId, String channelId);

    boolean updateChannelName(String serverId, String creatorId, String channelId, String replaceName);
}
