package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    //생성
    void createChannel(String channelname);

    //읽기
    Channel getChannelById(UUID id);

    //모두읽기
    List<Channel> getAllChannels();

    //수정
    Channel updateChannelName(UUID id, String channelname);

    //삭제
    void deleteChannel(UUID id);
}
