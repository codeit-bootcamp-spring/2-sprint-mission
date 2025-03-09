package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;


import java.util.Optional;
import java.util.Set;
import java.util.UUID;
public interface ChannelRepository {
        // 채널 추가
        Channel registerChannel(Channel channel);
        // 채널 조회
        Set<UUID> AllChannelUserList();
        // 채널에 속한 사용자 조회

        boolean removeChannel(UUID channelId);
        //저장조회

        Optional<Channel> findByChannelId(UUID channelId);
        //채널 이름 조회
        Optional<Channel> findByChannelName(String channelName);
}


