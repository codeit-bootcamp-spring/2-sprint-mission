package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    // 채널 생성
    void createChannel(Channel channel);

    // 채널 하나 읽기
    // UUID id를 파라미터로 사용해 불러오기엔, UUID는 난수값이라 미리 알기가 힘들고 Channel DB를 따로 사용하지 않으므로 알길이 없음
    // => channelName으로 User를 찾는게 훨씬 편할 것 같다.
    Channel getChannel(String channelName);

    // 채널 모두 읽기
    List<Channel> getAllChannels();

    // 채널 수정
    // channelName도 변경 안되게 막아놔서 바꿀게 채널의 Users 필드밖에 없음
    // -> Users를 삭제하거나 추가하는 메소드 두개로 분리
    // void updateChannel(Channel channel);
    void addUsersToChannel(User user, Channel channel);
    void removeUsersFromChannel(User user, Channel channel);


    // 채널 삭제
    void deleteChannel(String channelName);


}
