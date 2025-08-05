package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageConsumer {

    private final SimpMessageSendingOperations messagingTemplate;
    private final SseService sseService;

    // 채팅 메시지 Consumer (백그라운드 처리용)
    @KafkaListener(topics = "chat-messages", groupId = "discodeit-group")
    public void consumeChatMessage(MessageDto messageDto) {
        try {
            log.info("Kafka 메시지 수신: 채널 {}, 메시지 {}", messageDto.channelId(), messageDto.id());
            
            // 백그라운드 작업들
            // 1. 메시지 검색 인덱싱
            // 2. AI 분석
            // 3. 오프라인 사용자 푸시 알림
            // 4. 메시지 통계
            
            log.info("메시지 백그라운드 처리 완료: {}", messageDto.id());
        } catch (Exception e) {
            log.error("채팅 메시지 처리 실패: {}", e.getMessage(), e);
        }
    }

    // 알림 Consumer
    @KafkaListener(topics = "notifications", groupId = "discodeit-group")
    public void consumeNotification(Object notificationData) {
        try {
            log.info("Kafka 알림 수신: {}", notificationData);
            // SSE로 알림 전송 등 추가 처리
        } catch (Exception e) {
            log.error("알림 처리 실패: {}", e.getMessage(), e);
        }
    }

    // 채널 이벤트 Consumer
    @KafkaListener(topics = "channel-events", groupId = "discodeit-group")
    public void consumeChannelEvent(Object channelEvent) {
        try {
            log.info("Kafka 채널 이벤트 수신: {}", channelEvent);
            // 채널 관련 이벤트 처리
        } catch (Exception e) {
            log.error("채널 이벤트 처리 실패: {}", e.getMessage(), e);
        }
    }

    // 파일 상태 이벤트 Consumer
    @KafkaListener(topics = "file-status", groupId = "discodeit-group")
    public void consumeFileStatusEvent(Object fileStatusEvent) {
        try {
            log.info("Kafka 파일 상태 이벤트 수신: {}", fileStatusEvent);
            // 파일 상태 변경 이벤트 처리
        } catch (Exception e) {
            log.error("파일 상태 이벤트 처리 실패: {}", e.getMessage(), e);
        }
    }
} 