package com.sprint.mission.discodeit.jcf.serviceimpl;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.service.UserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserChannelService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ValidationService validationService;

    public UserChannelService(
            UserRepository userRepository, 
            ChannelRepository channelRepository,
            ValidationService validationService) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.validationService = validationService;
    }
    
    public void addUserToChannel(UUID userId, UUID channelId) {
        User user = validationService.validateUserExists(userId);
        Channel channel = validationService.validateChannelExists(channelId);
        
        // 속해있는지 확인
        boolean userInChannel = channel.getUserList().contains(userId);
        boolean channelInUser = user.getBelongChannels().contains(channelId);
        
        //한쪽이라도 속할 경우 수정
        if (userInChannel || channelInUser) {
            channel.getUserList().add(userId);
            user.getBelongChannels().add(channelId);
            return;
        }
        
        // 이미 속할 경우
        if (userInChannel && channelInUser) {
            System.out.println("사용자 " + userId + "는 이미 채널 " + channelId + "에 속해 있습니다.");
            return;
        }
        
        channel.joinChannel(userId);
        user.addBelongChannel(channelId);
    }
    
    public void removeUserFromChannel(UUID userId, UUID channelId) {
        User user = validationService.findUserById(userId);
        Channel channel = validationService.findChannelById(channelId);
        
        // 채널 소유자인 경우 제거 불가
        if (channel.getOwnerID().equals(userId)) {
            throw new IllegalStateException("채널 소유자는 채널에서 나갈 수 없습니다. 채널을 삭제하세요.");
        }
        
        // 사용자가 채널에 속해 있는지 확인
        boolean userInChannel = channel.getUserList().contains(userId);
        boolean channelInUser = user.getBelongChannels().contains(channelId);
        
        if (!userInChannel && !channelInUser) {
            return; 
        }
        
        // 관계 제거
        try {
            channel.leaveChannel(userId);
            user.removeBelongChannel(channelId);
        } catch (Exception e) {
            // 예외 무시
        }
    }
    
    public Set<UUID> getChannelUsers(UUID channelId) {
        Channel channel = validationService.validateChannelExists(channelId);
        return new HashSet<>(channel.getUserList());
    }
    
    public void cleanupUserChannels(UUID userId) {
        User user = validationService.findUserById(userId);
        if (user == null) {
            return;
        }
        
        Set<UUID> userChannels = new HashSet<>(user.getBelongChannels());
        
        for (UUID channelId : userChannels) {
            cleanupUserFromChannel(user, channelId);
        }
        
        user.getBelongChannels().clear();
    }

    private void cleanupUserFromChannel(User user, UUID channelId) {
        Channel channel = validationService.findChannelById(channelId);
        if (channel == null) {
            return;
        }
        
        UUID userId = user.getUUIDId();
        
        if (channel.getOwnerID().equals(userId)) {
            // 사용자가 채널 소유자인 경우 채널 삭제
            cleanupChannelUsers(channelId);
            channelRepository.removeChannel(channelId);
        } else {
            try {
                // 일반 사용자인 경우 채널에서만 제거
                channel.leaveChannel(userId);
            } catch (Exception e) {
            
            }
        }
    }
    
    public void cleanupChannelUsers(UUID channelId) {
        Channel channel = validationService.findChannelById(channelId);
        if (channel == null) {
            return;
        }
        
        Set<UUID> channelUsers = new HashSet<>(channel.getUserList());
        
        for (UUID userId : channelUsers) {
            User user = validationService.findUserById(userId);
            if (user != null) {
                user.removeBelongChannel(channelId);
            }
        }
    }
} 