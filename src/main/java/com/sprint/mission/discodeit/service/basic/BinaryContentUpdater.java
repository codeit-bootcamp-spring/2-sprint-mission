package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.event.BinaryContentStatusEvent;
import com.sprint.mission.discodeit.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentUpdater {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final EventPublisher eventPublisher;

    @Transactional
    public void updateUploadStatus(UUID contentId, BinaryContentUploadStatus status) {
        binaryContentRepository.findById(contentId).ifPresent(content -> {
            content.updateUploadStatus(status);
            BinaryContent savedContent = binaryContentRepository.save(content);
            
            // 현재 로그인한 사용자 ID를 MDC에서 가져오기
            String userIdStr = MDC.get("userId");
            UUID userId = userIdStr != null ? UUID.fromString(userIdStr) : null;
            
            // 파일 업로드 상태 변경 이벤트 발행
            publishBinaryContentStatusEvent(savedContent, userId);
            
            // SSE를 통한 실시간 파일 업로드 상태 알림
            if (userId != null) {
                BinaryContentDto dto = binaryContentMapper.toDto(savedContent);
                eventPublisher.publishFileUploadStatus(userId, dto);
            }
        });
    }
    
    // 파일 업로드 상태 변경 이벤트 발행
    private void publishBinaryContentStatusEvent(BinaryContent binaryContent, UUID userId) {
        BinaryContentDto dto = binaryContentMapper.toDto(binaryContent);
        applicationEventPublisher.publishEvent(new BinaryContentStatusEvent(dto, userId));
    }
} 