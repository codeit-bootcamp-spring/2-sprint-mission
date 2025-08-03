package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.event.BinaryContentStatusEvent;
import com.sprint.mission.discodeit.event.EventPublisher;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentEventListener {

    private final EventPublisher eventPublisher;

    @Async
    @EventListener
    public void handleBinaryContentStatusEvent(BinaryContentStatusEvent event) {
        BinaryContentDto binaryContentDto = event.getBinaryContentDto();
        UUID userId = event.getUserId();


        if (userId != null) {
            eventPublisher.publishFileUploadStatus(userId, binaryContentDto);
        } else {
            log.warn("파일 {}에 대한 사용자 ID를 찾을 수 없습니다.", binaryContentDto.id());
        }
    }
} 