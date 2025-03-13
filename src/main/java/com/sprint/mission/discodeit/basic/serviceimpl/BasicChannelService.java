package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.util.ValidationUtil;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserChannelService userChannelService;
    

    private static BasicChannelService instance;

    private BasicChannelService(
            ChannelRepository channelRepository,
            UserChannelService userChannelService) {
        this.channelRepository = channelRepository;
        this.userChannelService = userChannelService;
    }
    
    
    public static synchronized BasicChannelService getInstance(
            ChannelRepository channelRepository,
            UserChannelService userChannelService) {
        if (instance == null) {
            instance = new BasicChannelService(channelRepository, userChannelService);
        }
        return instance;
    }

    @Override
    public void createChannel(String channelName, UUID userId) {
        ValidationUtil.validateNotEmpty(channelName, "채널 이름");
        ValidationUtil.validateNotNull(userId, "사용자 ID");


        checkChannelNameDuplication(channelName);
        // 채널 객체 생성
        Channel channel = new Channel(channelName, userId);

        // 채널을 저장소에 등록
        channelRepository.register(channel);

        // 채널 등록 후 사용자를 채널에 추가
        userChannelService.addUserToChannel(userId, channel);
        // 채널 생성 메서드


    }

    @Override
    public Set<UUID> findByAllChannel() {
        return channelRepository.allChannelIdList();
    }

    @Override
    public void setChannelName(UUID channelId, String newChannelName, UUID userId) {
        ValidationUtil.validateNotNull(channelId, "채널 ID");
        ValidationUtil.validateNotEmpty(newChannelName, "새 채널 이름");
        ValidationUtil.validateNotNull(userId, "사용자 ID");
        
        Channel channel = channelRepository.findChannelById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다: " + channelId));
        
        // 사용자가 채널 소유자인지 확인
        if (!channel.getOwnerID().equals(userId)) {
            throw new IllegalArgumentException("해당 사용자는 채널 소유자가 아닙니다.");
        }
        checkChannelNameDuplication(newChannelName);
        
        channel.setChannelName(newChannelName);
        
        // 변경된 채널 정보를 저장
        channelRepository.updateChannel(channel);
    }

    //채널 이름 반환
    @Override
    public String getChannelName(UUID channelId) {
        ValidationUtil.validateNotNull(channelId, "채널 ID");
       Channel channel = channelRepository.findChannelById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다: " + channelId));
       return channel.getChannelName();

    }

    public Set<UUID> getChannelUserList(UUID channelId) {
        ValidationUtil.validateNotNull(channelId,"채널 ID");
        Channel channel = channelRepository.findChannelById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다: " + channelId));
        return channel.getUserList();
    }
    @Override
    public void joinChannel(UUID channelId, UUID userId) {
        ValidationUtil.validateNotNull(channelId, "채널 ID");
        ValidationUtil.validateNotNull(userId, "사용자 ID");

        Channel channel = channelRepository.findChannelById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다: " + channelId));

        userChannelService.addUserToChannel(userId, channel);
    }

    @Override
    public void leaveChannel(UUID channelId, UUID userId) {
        ValidationUtil.validateNotNull(channelId, "채널 ID");
        ValidationUtil.validateNotNull(userId, "사용자 ID");
        
        userChannelService.removeUserFromChannel(userId, channelId);
    }
    // 채널명 중복 체크 로직
    private void checkChannelNameDuplication(String channelName) {
        Optional<Channel> existingChannel = channelRepository.findChannelByName(channelName);

        if (existingChannel.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 채널명입니다: " + channelName);
        }
    }

}

