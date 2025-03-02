package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.util.*;
public interface ChannelRepository {

        // 채널 추가
        void createChannel(String channelName, String userId);
        // 채널 조회
       List<String> AllChannelUserList();
        // 채널에 속한 사용자 조회
        List<String> getAllChannels();
        // 채널 이름수정
        void updateChannel(String channelName, String newChannelName, String owner);
        // 채널 삭제
        void deleteChannel(String channelId);
        // 채널에 유저 추가
        void addUserToChannel(String channelId,String userId);
        // 채널에서 유저 제거
        void removeUserFromChannel(String channelId, String userId);
        // 특정 채널의 유저 목록 조회
        List<String> getChannelUserList(String channelId);
        //채널에 속하는지 확인
        boolean containsChannel(String channelId);
        // 권한이 있는지 확인
        boolean isChannelOwner(String channelName, String userId);
}


