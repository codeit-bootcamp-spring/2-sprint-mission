package com.sprint.mission.discodeit.controller;

import static com.sprint.mission.discodeit.config.SetUpUserInfo.LONGIN_USER;
import static com.sprint.mission.discodeit.constant.MessageInfo.MESSAGE_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MessageControllerTest {
    private MessageController messageController;
    private User user;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = new JCFUserRepository();
        user = userRepository.save(
                new User(LONGIN_USER.getName(), LONGIN_USER.getEmail(), LONGIN_USER.getPassword(), null));
        MessageService messageservice = new BasicMessageService(new JCFMessageRepository(), userRepository);
        messageController = new MessageController(messageservice);
    }

    @DisplayName("채널에 메세지 생성 테스트")
    @Test
    void 메세지_생성_테스트() {
        MessageDto message = messageController.createMessage(MESSAGE_CONTENT, UUID.randomUUID(), user.getId());

        assertThat(message.context()).isEqualTo(MESSAGE_CONTENT);
    }
}