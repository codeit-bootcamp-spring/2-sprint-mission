package com.sprint.mission.discodeit.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.application.MessageDto;
import com.sprint.mission.discodeit.jcf.JCFMessageRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageRepositoryTest {
    private static final String CONTEXT = "안녕하세요";
    private MessageRepository messageRepository;
    private MessageDto setUpMessage;

    @BeforeEach
    void setUp() {
        messageRepository = new JCFMessageRepository();
        setUpMessage = messageRepository.create(CONTEXT);
    }

    @Test
    void 메세지_생성() {
        assertThat(setUpMessage.context()).isEqualTo(CONTEXT);
    }

    @Test
    void 메세지_단건_조회() {
        MessageDto message = messageRepository.findById(setUpMessage.messageId());

        assertThat(message.context())
                .isEqualTo(CONTEXT);
    }

    @Test
    void 메세지_내용_수정() {
        messageRepository.updateContext(setUpMessage.messageId(), "안녕1하세요");

        assertThat(messageRepository.findById(setUpMessage.messageId()).context())
                .isNotEqualTo(CONTEXT);
    }

    @Test
    void 메세지_삭제() {
        messageRepository.delete(setUpMessage.messageId());
        UUID initId = setUpMessage.messageId();

        assertThatThrownBy(() -> messageRepository.findById(initId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}