package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserLeftChannelEventListener {
    private final ChannelRepository channelRepository;
    
    @EventListener
    public void handleUserLeftChannelEvent(UserLeftChannelEvent event) {
        UUID userId = event.getUserId();
        UUID channelId = event.getChannelId();
        
        Channel channel = channelRepository.findChannelById(channelId).orElse(null);
        if (channel == null) return;
        
        // 채널 엔티티의 메서드 활용
        channel.leaveChannel(userId);
        channelRepository.updateChannel(channel);
    }
} 