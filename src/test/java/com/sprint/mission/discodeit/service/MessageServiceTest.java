package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.application.MessageDto;
import com.sprint.mission.discodeit.jcf.JCFMessageService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageServiceTest {
    private MessageService messageService;
    private MessageDto messageDto;
    private static final String CONTEXT = "안녕하세요";

    @BeforeEach
    void setUp() {
        messageService = new JCFMessageService();
        messageDto = messageService.create(CONTEXT);
    }

    @Test
    void 메세지_생성() {
        assertThat(messageDto.context()).isEqualTo(CONTEXT);
    }

    @Test
    void 메세지_단건_조회() {
        List<MessageDto> messages = messageService.findById(messageDto.messageId());

        assertThat(messages.get(0).context()).isEqualTo(CONTEXT);
    }
}