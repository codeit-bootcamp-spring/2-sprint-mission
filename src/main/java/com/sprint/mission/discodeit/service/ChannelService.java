package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface ChannelService {
    // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)
    void createChannel(String channelName); //채널 생성

    Channel getChannel(String channelName); // 채널 조회
    List<Channel> getAllChannels(); //모든 채널 조회

    void updateChannelName(Channel channel, String newChannelName); //채널 이름 변경
    void addUserToChannel(Channel channel, String userName); //유저 추가
    void addMessageToChannel(Channel channel, Message message);   //메세지 추가


    void removeUserFromChannel(Channel channel, String userName); //유저 삭제
    void removeMessageFromChannel(Channel channel, Message message); //메세지 삭제

    void validateChannelExists(String channelName); // 채널 존재 확인

}
