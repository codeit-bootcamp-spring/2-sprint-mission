package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


public class UserEventListener {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;
    private final ReadStatusRepository readStatusRepository;
    
    @EventListener
    public void handleUserDeletedEvent(UserDeletedEvent event) {
        UUID userId = event.getUserId();
        
        // 사용자가 소유한 채널 삭제
        Set<UUID> allChannelIds = channelRepository.allChannelIdList();
        for (UUID channelId : allChannelIds) {
            Channel channel = channelRepository.findChannelById(channelId).orElse(null);
            if (channel != null && channel.getOwnerID().equals(userId)) {
                channelRepository.deleteChannel(channelId);
            }
        }
        
        // 사용자가 작성한 메시지 삭제
        List<Message> userMessages = messageRepository.findAll().stream()
                .filter(msg -> msg.getAuthorId().equals(userId))
                .toList();
        
        for (Message message : userMessages) {
            messageRepository.deleteMessage(message.getId());
        }
        
        // 사용자와 관련된 바이너리 콘텐츠 삭제
        binaryContentRepository.delete(userId);
        
        // 사용자의 읽음 상태 정보 삭제
        List<ReadStatus> userReadStatuses = readStatusRepository.findAllByUserId(userId);
        for (ReadStatus status : userReadStatuses) {
            readStatusRepository.deleteReadStatus(status.getId());
        }

        // 사용자 상태 정보 삭제
        userStatusRepository.findByUserId(userId).
                ifPresent(userStatus -> userStatusRepository.delete(userStatus.getId()));
    }
    
    @EventListener
    public void handleUserLeftChannelEvent(UserLeftChannelEvent event) {
        UUID userId = event.getUserId();
        UUID channelId = event.getChannelId();
        
        // 채널 조회 및 사용자 제거
        Channel channel = channelRepository.findChannelById(channelId).orElse(null);
        if (channel != null) {
            // 채널 엔티티의 메서드 활용
            channel.leaveChannel(userId);
            channelRepository.updateChannel(channel);
        }
        
        // 사용자 조회 및 채널 제거
        User user = userRepository.findByUser(userId).orElse(null);
        if (user != null) {
            // 사용자 엔티티의 메서드 활용
            user.removeBelongChannel(channelId);
            userRepository.updateUser(user);
        }
    }
} 