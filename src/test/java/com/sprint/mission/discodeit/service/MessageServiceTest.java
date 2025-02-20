package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.application.MessageDto;
import com.sprint.mission.discodeit.jcf.JCFMessageService;
import org.junit.jupiter.api.Test;

class MessageServiceTest {
    @Test
    void 메세지_생성(){
        MessageService messageService = new JCFMessageService();
        String context = "안녕하세요";

        MessageDto messageDto = messageService.create(context);
        assertThat(messageDto.context()).isEqualTo(context);
    }
}