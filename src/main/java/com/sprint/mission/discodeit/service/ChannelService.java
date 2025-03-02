package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.util.*;
public interface ChannelService
{
    //전체 채널 목록 조회
    void getChannelList();
    void updateChannel(String channelName, String newChannelName, String owner);
    // 선택한 채널의 유저목록 조회
    void getChannelUserList(String channelId);
    // 채널 생성
    void createChannel(String channelName,String userId);
    // 채널 유저 가입
    void joinChannel(String channelId, String userId);
    // 채널에서 유저 탈퇴
    void leaveChannel(String channelId, String userId);
}

