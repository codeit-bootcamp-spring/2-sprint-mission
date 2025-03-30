package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.message.MessageCreationRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.MessageInfo.MESSAGE_CONTENT;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MessageControllerTest {
    private MessageController messageController;
    private BasicBinaryContentService basicBinaryContentService;
    private MessageService messageService;
    private BinaryContentRepository binaryContentRepository;
    private UserRepository userRepository;
    private User setUpUser;

    @BeforeEach
    void setUp() {
        binaryContentRepository = new JCFBinaryContentRepository();
        userRepository = new JCFUserRepository();
        messageService = new BasicMessageService(new JCFMessageRepository(), binaryContentRepository);
        basicBinaryContentService = new BasicBinaryContentService(binaryContentRepository);
        messageController = new MessageController(messageService, basicBinaryContentService);

        setUpUser = userRepository.save(new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));
    }

    @DisplayName("채널에 메시지를 생성하면 올바른 내용을 반환한다.")
    @Test
    void createMessageTest() {
        MessageResult message = messageController.createMessage(new MessageCreationRequest(MESSAGE_CONTENT, UUID.randomUUID(), setUpUser.getId()), new ArrayList<>());

        assertThat(message.context()).isEqualTo(MESSAGE_CONTENT);
    }

    @DisplayName("메시지를 삭제하면 첨부 파일도 삭제된다.")
    @Test
    void deleteMessageDeletesAttachments() {
        MockMultipartFile file = new MockMultipartFile(MediaType.IMAGE_JPEG_VALUE, MESSAGE_CONTENT.getBytes());

        MessageResult message = messageController.createMessage(new MessageCreationRequest(MESSAGE_CONTENT, UUID.randomUUID(), setUpUser.getId()), List.of(file));
        messageController.delete(message.messageId());

        assertThatThrownBy(() -> basicBinaryContentService.findById(message.attachmentIds().get(0))).isInstanceOf(IllegalArgumentException.class);
    }
}