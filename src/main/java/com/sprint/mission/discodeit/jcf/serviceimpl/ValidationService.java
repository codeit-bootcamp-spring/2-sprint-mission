package com.sprint.mission.discodeit.jcf.serviceimpl;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.service.UserRepository;

import java.util.UUID;

public class ValidationService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ValidationService(UserRepository userRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }
    
    // === 기본 검증 메서드 ===
    
    public void validateUUID(UUID id, String fieldName) {
        if (id == null) {
            throw new IllegalArgumentException(fieldName + "는 null 수 없습니다.");
        }
    }
    
    public void validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + "는 비어있을 수 없습니다.");
        }
    }
    
    // === 사용자 관련 검증 ===
    
    public User validateUserExists(UUID userId) {
        validateUUID(userId, "사용자 ID");
        return userRepository.findByUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
    }
    
    public User findUserById(UUID userId) {
        validateUUID(userId, "사용자 ID");
        return userRepository.findByUser(userId).orElse(null);
    }
    
    // === 채널 관련 검증 ===
    
    public void validateChannelNameNotExists(String channelName) {
        validateString(channelName, "채널 이름");
        if (channelRepository.findByChannelName(channelName).isPresent()) {
            throw new IllegalArgumentException("채널 이름이 이미 존재합니다.");
        }
    }
    
    public Channel validateChannelExists(UUID channelId) {
        validateUUID(channelId, "채널 ID");
        return channelRepository.findByChannelId(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다: " + channelId));
    }
    
    public Channel findChannelById(UUID channelId) {
        validateUUID(channelId, "채널 ID");
        return channelRepository.findByChannelId(channelId).orElse(null);
    }
    
    // === 사용자-채널 관계 검증 ===
    
    public void validateUserIsChannelOwner(UUID userId, UUID channelId) {
        User user = validateUserExists(userId);
        Channel channel = validateChannelExists(channelId);
        
        if (!channel.getOwnerID().equals(userId)) {
            throw new IllegalArgumentException("해당 사용자는 채널 소유자가 아닙니다.");
        }
    }
    
    public void validateUserInChannel(UUID channelId, UUID userId) {
        Channel channel = validateChannelExists(channelId);
        User user = validateUserExists(userId);

        boolean channelInUser = channel.getUserList().contains(userId);
        boolean userBelongsToChannel = user.getBelongChannels().contains(channelId);

        if (!(channelInUser && userBelongsToChannel)) {
            throw new IllegalArgumentException("해당 유저는 이 채널에 존재하지 않습니다.");
        }
    }
    
    public void validateUserNotInChannel(UUID channelId, UUID userId) {
        Channel channel = validateChannelExists(channelId);
        User user = validateUserExists(userId);

        boolean channelInUser = channel.getUserList().contains(userId);
        boolean userBelongsToChannel = user.getBelongChannels().contains(channelId);

        if (channelInUser || userBelongsToChannel) {
            throw new IllegalArgumentException("사용자가 이미 채널에 속해 있습니다.");
        }
    }
} 