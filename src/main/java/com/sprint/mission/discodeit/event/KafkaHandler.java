package com.sprint.mission.discodeit.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.event.NotificationAsyncFailedEvent;
import com.sprint.mission.discodeit.dto.event.NotificationNewMessageEvent;
import com.sprint.mission.discodeit.dto.event.NotificationRoleUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaHandler {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String NEW_MESSAGES_TOPIC = "discodeit.new-messages";
    private static final String UPDATE_ROLES_TOPIC = "discodeit.role-changed";
    private static final String ASYNC_TASK_FAILED_TOPIC = "discodeit.async-task-failed";

    @Async("kafkaExecutor")
    @TransactionalEventListener(
        phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleNewMessage(NotificationNewMessageEvent event) {
        try {
            log.info("kafka 메세지 전송 시작: topic = {}, messageId = {}", NEW_MESSAGES_TOPIC, event.messageDto().id());
            String playLoad = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(NEW_MESSAGES_TOPIC, playLoad);
            log.info("kafka 메세지 전송 완료: topic = {}, messageId = {}", NEW_MESSAGES_TOPIC, event.messageDto().id());
        } catch (Exception e) {
            log.info("kafka 메세지 전송 실패: topic = {}, messageId = {}, error = {}",
                NEW_MESSAGES_TOPIC, event.messageDto().id(), e.getMessage());
        }
    }

    @Async("kafkaExecutor")
    @TransactionalEventListener(
        phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleUpdateRole(NotificationRoleUpdateEvent event) {
        try {
            log.info("kafka 권한 변경 전송 시작: topic = {}, userId = {}", UPDATE_ROLES_TOPIC, event.userDto().id());
            String playLoad = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(UPDATE_ROLES_TOPIC, playLoad);
            log.info("kafka 권한 변경 전송 완료: topic = {}, userId = {}", UPDATE_ROLES_TOPIC, event.userDto().id());
        } catch (Exception e) {
            log.info("kafka 권한 변경 전송 실패: topic = {}, userId = {}, error = {}",
                UPDATE_ROLES_TOPIC, event.userDto().id(), e.getMessage());
        }
    }

    @Async("kafkaExecutor")
    @EventListener()
    public void handleAsyncTaskFailed(NotificationAsyncFailedEvent event) {
        try {
            log.info("kafka 비동기 실패 전송 시작: topic = {}, taskName = {}, failureReason = {}",
                UPDATE_ROLES_TOPIC, event.taskName(), event.failureReason());
            String playLoad = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(ASYNC_TASK_FAILED_TOPIC, playLoad);
            log.info("kafka 비동기 실패 전송 완료: topic = {}, taskName = {}, failureReason = {}",
                UPDATE_ROLES_TOPIC, event.taskName(), event.failureReason());
        } catch (Exception e) {
            log.info("kafka 비동기 실패 전송 실패: topic = {}, taskName = {}, error = {}",
                UPDATE_ROLES_TOPIC, event.taskName(), e.getMessage());
        }
    }

}
