package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.messagedto.MessageCreationDto;
import com.sprint.mission.discodeit.application.messagedto.MessageDto;
import com.sprint.mission.discodeit.entity.User;
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

import java.util.ArrayList;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.MessageInfo.MESSAGE_CONTENT;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;

class MessageControllerTest {
    private MessageController messageController;
    private BasicBinaryContentService basicBinaryContentService;
    private User setUpUser;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = new JCFUserRepository();
        setUpUser = userRepository.save(
                new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));
        MessageService messageservice = new BasicMessageService(new JCFMessageRepository(), userRepository);
        basicBinaryContentService = new BasicBinaryContentService(new JCFBinaryContentRepository());
        messageController = new MessageController(messageservice, basicBinaryContentService);
    }

    @DisplayName("채널에 메세지 생성 테스트")
    @Test
    void 메세지_생성_테스트() {
        MessageDto message = messageController.createMessage(new MessageCreationDto(MESSAGE_CONTENT, UUID.randomUUID(), setUpUser.getId()), new ArrayList<>());

        assertThat(message.context()).isEqualTo(MESSAGE_CONTENT);
    }
}