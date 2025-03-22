package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.messagedto.MessageCreationDto;
import com.sprint.mission.discodeit.application.messagedto.MessageDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.MessageInfo.MESSAGE_CONTENT;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MessageServiceTest {
    private MessageService messageService;
    private MessageDto setUpMessage;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = new JCFUserRepository();
        User user = userRepository.save(
                new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));

        messageService = new JCFMessageService(new JCFMessageRepository(), userRepository);
        setUpMessage = messageService.create(new MessageCreationDto(MESSAGE_CONTENT, UUID.randomUUID(), user.getId()), new ArrayList<>());
    }

    @Test
    void 메세지_생성() {
        assertThat(setUpMessage.context()).isEqualTo(MESSAGE_CONTENT);
    }

    @Test
    void 메세지_단건_조회() {
        MessageDto message = messageService.findById(setUpMessage.messageId());

        assertThat(message.context())
                .isEqualTo(MESSAGE_CONTENT);
    }

    @Test
    void 메세지_내용_수정() {
        messageService.updateContext(setUpMessage.messageId(), MESSAGE_CONTENT + "123");

        assertThat(messageService.findById(setUpMessage.messageId()).context())
                .isNotEqualTo(MESSAGE_CONTENT);
    }

    @Test
    void 메세지_삭제() {
        messageService.delete(setUpMessage.messageId());
        UUID initId = setUpMessage.messageId();

        assertThatThrownBy(() -> messageService.findById(initId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}