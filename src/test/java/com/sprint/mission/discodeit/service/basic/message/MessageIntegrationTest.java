package com.sprint.mission.discodeit.service.basic.message;

import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.dto.MessageResult;
import com.sprint.mission.discodeit.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.message.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sprint.mission.discodeit.service.basic.message.BasicMessageServiceTest.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class MessageIntegrationTest {

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private BasicMessageService messageService;

    @DisplayName("유저가 메세지 내용을 입력하면, 채널에 메세지를 생성합니다.") // lazy 로딩이 어떻게 되는가?
    @Test
    void createTest() {
        // given
        Channel savedChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, "", ""));
        User savedUser = userRepository.save(new User("", "", "", null)); // 여기서 넣는게 맞나?// 아 근데 여기서 넣어주는게
        UserStatus savedUserStatus = userStatusRepository.save(new UserStatus(savedUser, Instant.now())); // 이렇 이렇게 넣어주는게 맞나?, 불편쓰 하네

        // 유저 만들고, 유저 Status를 채웁니다.
        MessageCreateRequest messageCreateRequest = new MessageCreateRequest(MESSAGE_CONTENT, savedChannel.getId(), savedUser.getId());

        // when
        MessageResult message = messageService.create(messageCreateRequest, List.of());

        // then
        assertAll(
                () -> Assertions.assertThat(message.content()).isEqualTo(MESSAGE_CONTENT),
                () -> Assertions.assertThat(message.author().id()).isEqualTo(savedUser.getId())
        );
    }

}
