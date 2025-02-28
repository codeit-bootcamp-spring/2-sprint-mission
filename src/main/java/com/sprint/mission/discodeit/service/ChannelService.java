package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.ChannelEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService extends BaseService<ChannelEntity> {
    Optional<ChannelEntity> getChannelByName(String channelName); //특정 채널 조회
//    List<ChannelEntity> getUserChannelsById(UUID senderId); //user가 속한 채널 조회
    void updateChannelName(String channelName, String newName);
    void updateChannelType(String channelName, String newType);
}
