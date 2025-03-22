package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.messagedto.MessageCreationDto;
import com.sprint.mission.discodeit.application.messagedto.MessageDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.MessageInfo.MESSAGE_CONTENT;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MessageServiceTest {
    private MessageService messageService;
    private MessageDto setUpMessage;
    private UUID channelId;
    private User setUpUser;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = new JCFUserRepository();
        setUpUser = userRepository.save(
                new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));

        messageService = new BasicMessageService(new JCFMessageRepository(), userRepository, new JCFBinaryContentRepository());
        channelId = UUID.randomUUID();
        setUpMessage = messageService.create(new MessageCreationDto(MESSAGE_CONTENT, channelId, setUpUser.getId()), new ArrayList<>());
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

    @DisplayName("조회시 채널 ID를 받으면 채널에 해당하는 메세지들을 생성 순서에 맞게 반환합니다.")
    @Test
    void findAllByChannelIdTest() {
        MessageDto messageDto = messageService.create(new MessageCreationDto(MESSAGE_CONTENT, channelId, setUpUser.getId()), new ArrayList<>());

        List<MessageDto> messages = messageService.findAllByChannelId(channelId);
        assertThat(messages).containsExactly(setUpMessage, messageDto);
    }

    @Test
    void 메세지_내용_수정() {
        MessageDto message = messageService.updateContext(setUpMessage.messageId(), MESSAGE_CONTENT + "123");

        assertThat(message.context()).isEqualTo(MESSAGE_CONTENT + "123");
    }

    @Test
    void 메세지_삭제() {
        messageService.delete(setUpMessage.messageId());
        UUID initId = setUpMessage.messageId();

        assertThatThrownBy(() -> messageService.findById(initId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}