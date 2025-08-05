package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.event.ChannelEvent;
import com.sprint.mission.discodeit.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelEventListener {

    private final EventPublisher eventPublisher;

    @Async
    @EventListener
    public void handleChannelEvent(ChannelEvent event) {

        // 모든 사용자에게 채널 목록 갱신 알림 브로드캐스트
        eventPublisher.publishChannelRefresh(event.getChannelId());
    }
} 