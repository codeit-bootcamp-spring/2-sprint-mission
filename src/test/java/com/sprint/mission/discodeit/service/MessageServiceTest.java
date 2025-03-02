package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.infra.UserRepository;
import com.sprint.mission.discodeit.infra.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.infra.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageServiceTest {
    private static final String CONTEXT = "안녕하세요";
    private MessageService messageService;
    private MessageDto setUpMessage;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = new JCFUserRepository();
        User user = userRepository.save(new User("황지환", "hwang@naver.com", "12345"));
        messageService = new JCFMessageService(new JCFMessageRepository(), new JCFUserService(userRepository));
        setUpMessage = messageService.create(CONTEXT, UUID.randomUUID(), user.getId());
    }

    @Test
    void 메세지_생성() {
        assertThat(setUpMessage.context()).isEqualTo(CONTEXT);
    }

    @Test
    void 메세지_단건_조회() {
        MessageDto message = messageService.findById(setUpMessage.messageId());

        assertThat(message.context())
                .isEqualTo(CONTEXT);
    }

    @Test
    void 메세지_내용_수정() {
        messageService.updateContext(setUpMessage.messageId(), "안녕1하세요");

        assertThat(messageService.findById(setUpMessage.messageId()).context())
                .isNotEqualTo(CONTEXT);
    }

    @Test
    void 메세지_삭제() {
        messageService.delete(setUpMessage.messageId());
        UUID initId = setUpMessage.messageId();

        assertThatThrownBy(() -> messageService.findById(initId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}