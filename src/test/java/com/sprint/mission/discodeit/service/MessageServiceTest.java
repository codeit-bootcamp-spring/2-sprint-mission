package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.message.MessageCreationRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.util.mock.channel.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.util.mock.file.MockFile.createMockImageFile;
import static com.sprint.mission.discodeit.util.mock.message.MessageInfo.MESSAGE_CONTENT;
import static com.sprint.mission.discodeit.util.mock.user.UserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MessageServiceTest {

    private MessageService messageService;
    private BinaryContentRepository binaryContentRepository;
    private MessageResult setUpMessage;
    private User setUpUser;
    private Channel setUpPublicChannel;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = new JCFUserRepository();
        binaryContentRepository = new JCFBinaryContentRepository();
        ChannelRepository channelRepository = new JCFChannelRepository();
        messageService = new BasicMessageService(new JCFMessageRepository(), binaryContentRepository,
                channelRepository);

        setUpUser = userRepository.save(
                new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));
        setUpPublicChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, CHANNEL_NAME));
        setUpMessage = messageService.create(
                new MessageCreationRequest(MESSAGE_CONTENT, setUpPublicChannel.getId(), setUpUser.getId()),
                List.of());
    }

    @DisplayName("메시지를 생성한다면 생성된 메세지를 반환합니다")
    @Test
    void createMessage() {
        assertThat(setUpMessage.context()).isEqualTo(MESSAGE_CONTENT);
    }

    @Test
    @DisplayName("현재 존재하지 않는 채널에 메세지를 생성하려고 시도한다면 예외를 반환합니다.")
    void createMessage_Exception() {
        MessageCreationRequest messageCreationRequest = new MessageCreationRequest(MESSAGE_CONTENT,
                UUID.randomUUID(), setUpUser.getId());

        assertThatThrownBy(() -> messageService.create(messageCreationRequest, List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메시지 ID로 조회한다면 ID에 해당하는 메시지를 반환합니다.")
    @Test
    void getMessageById() {
        MessageResult message = messageService.getById(setUpMessage.messageId());

        assertThat(message.context()).isEqualTo(MESSAGE_CONTENT);
    }

    @DisplayName("채널 ID로 메시지들을 조회하면 생성 순서대로 반환합니다.")
    @Test
    void getAllMessagesByChannelId() {
        MessageResult messageResult = messageService.create(
                new MessageCreationRequest(MESSAGE_CONTENT, setUpPublicChannel.getId(), setUpUser.getId()),
                List.of());
        List<MessageResult> messages = messageService.getAllByChannelId(setUpPublicChannel.getId());

        assertThat(messages).containsExactly(setUpMessage, messageResult);
    }

    @DisplayName("메시지 내용을 수정하면 변경된 내용이 반환됩니다.")
    @Test
    void updateMessageContent() {
        MessageResult message = messageService.updateContext(setUpMessage.messageId(),
                MESSAGE_CONTENT + "123");

        assertThat(message.context()).isEqualTo(MESSAGE_CONTENT + "123");
    }

    @DisplayName("메시지를 삭제한 후 조회하면 예외가 발생한다.")
    @Test
    void deleteMessage() {
        messageService.delete(setUpMessage.messageId());
        assertThatThrownBy(() -> messageService.getById(setUpMessage.messageId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메시지를 삭제하면 바이너리 첨부 파일도 삭제됩니다.")
    @Test
    void deleteMessageDeletesAttachments() {
        MultipartFile file = createMockImageFile("dog.jpg");

        MessageCreationRequest messageCreationRequest = new MessageCreationRequest(MESSAGE_CONTENT,
                setUpPublicChannel.getId(), setUpUser.getId());
        MessageResult message = messageService.create(messageCreationRequest, List.of(file));

        messageService.delete(message.messageId());

        assertThat(
                binaryContentRepository.findByBinaryContentId(message.attachmentIds().get(0))).isEmpty();
    }
}