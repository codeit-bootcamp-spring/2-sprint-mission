package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.message.MessageCreationRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
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
    private UserRepository userRepository;
    private MessageRepository messageRepository;
    private BinaryContentRepository binaryContentRepository;
    private MessageResult setUpMessage;
    private UUID channelId;
    private User setUpUser;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        messageRepository = new JCFMessageRepository();
        binaryContentRepository = new JCFBinaryContentRepository();
        messageService = new BasicMessageService(messageRepository, binaryContentRepository);

        setUpUser = userRepository.save(new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));
        channelId = UUID.randomUUID();
        setUpMessage = messageService.create(new MessageCreationRequest(MESSAGE_CONTENT, channelId, setUpUser.getId()), new ArrayList<>());
    }

    @DisplayName("메시지 생성 시 내용이 올바르게 설정된다.")
    @Test
    void createMessage() {
        assertThat(setUpMessage.context()).isEqualTo(MESSAGE_CONTENT);
    }

    @DisplayName("메시지 ID로 단건 조회 시 올바른 메시지를 반환한다.")
    @Test
    void findMessageById() {
        MessageResult message = messageService.findById(setUpMessage.messageId());
        assertThat(message.context()).isEqualTo(MESSAGE_CONTENT);
    }

    @DisplayName("채널 ID로 메시지들을 조회하면 생성 순서대로 반환한다.")
    @Test
    void findAllMessagesByChannelId() {
        MessageResult messageResult = messageService.create(new MessageCreationRequest(MESSAGE_CONTENT, channelId, setUpUser.getId()), new ArrayList<>());
        List<MessageResult> messages = messageService.findAllByChannelId(channelId);
        assertThat(messages).containsExactly(setUpMessage, messageResult);
    }

    @DisplayName("메시지 내용을 수정하면 변경된 내용이 반영된다.")
    @Test
    void updateMessageContent() {
        MessageResult message = messageService.updateContext(setUpMessage.messageId(), MESSAGE_CONTENT + "123");
        assertThat(message.context()).isEqualTo(MESSAGE_CONTENT + "123");
    }

    @DisplayName("메시지를 삭제한 후 조회하면 예외가 발생한다.")
    @Test
    void deleteMessage() {
        messageService.delete(setUpMessage.messageId());
        assertThatThrownBy(() -> messageService.findById(setUpMessage.messageId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}