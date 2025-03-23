package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.message.MessageCreationDto;
import com.sprint.mission.discodeit.application.dto.message.MessageDto;
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
    private User setUpUser;

    @BeforeEach
    void setUp() {
        BinaryContentRepository binaryContentRepository = new JCFBinaryContentRepository();
        UserRepository userRepository = new JCFUserRepository();
        setUpUser = userRepository.save(
                new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));
        MessageService messageservice = new BasicMessageService(new JCFMessageRepository(), userRepository, binaryContentRepository);
        basicBinaryContentService = new BasicBinaryContentService(binaryContentRepository);
        messageController = new MessageController(messageservice, basicBinaryContentService);
    }

    @DisplayName("채널에 메세지 생성 테스트")
    @Test
    void 메세지_생성_테스트() {
        MessageDto message = messageController.createMessage(new MessageCreationDto(MESSAGE_CONTENT, UUID.randomUUID(), setUpUser.getId()), new ArrayList<>());

        assertThat(message.context()).isEqualTo(MESSAGE_CONTENT);
    }


    @DisplayName("메세지 삭제시 첨부파일도 삭제합니다.")
    @Test
    void 메세지_삭제시_첨부파일도_삭제() {
        MockMultipartFile file = new MockMultipartFile(
                MediaType.IMAGE_JPEG_VALUE,
                MESSAGE_CONTENT.getBytes()
        );

        MessageDto message = messageController.createMessage(new MessageCreationDto(MESSAGE_CONTENT, UUID.randomUUID(), setUpUser.getId()), List.of(file));
        messageController.delete(message.messageId());

        assertThatThrownBy(() -> basicBinaryContentService.findById(message.attachmentIds().get(0)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}