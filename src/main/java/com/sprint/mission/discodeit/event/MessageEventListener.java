package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageEventListener {
    private final ReadStatusRepository readStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    
    @EventListener
    public void handleMessageDeletedEvent(MessageDeletedEvent event) {
        UUID messageId = event.getMessageId();
        
        // 메시지를 마지막으로 읽은 모든 ReadStatus 처리
        List<ReadStatus> allReadStatuses = readStatusRepository.findAllByUserId(null).stream()
            .filter(rs -> rs.getLastReadMessageId() != null && rs.getLastReadMessageId().equals(messageId))
            .collect(Collectors.toList());
            
        for (ReadStatus status : allReadStatuses) {
            // ReadStatus 엔티티의 메서드 활용
            status.updateLastReadMessage(null);
            readStatusRepository.updateReadStatus(status);
        }
        
        // 메시지 관련 바이너리 콘텐츠 삭제
        binaryContentRepository.delete(messageId);
    }
    
    @EventListener
    public void handleMessageCreatedEvent(MessageCreatedEvent event) {
        // 새 메시지에 대한 읽음 상태 초기화 등의 작업
        // 필요한 경우 구현
    }
} 