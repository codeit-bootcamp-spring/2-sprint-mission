package com.sprint.mission.discodeit.message.service;

import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.dto.MessageResult;
import com.sprint.mission.discodeit.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
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

import static com.sprint.mission.discodeit.message.service.BasicMessageServiceTest.MESSAGE_CONTENT;
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

    @DisplayName("유저가 메세지 내용을 입력하면, 채널에 메세지를 생성합니다.")
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

    @DisplayName("생성되지않은 채널에 메세지를 입력하면, 예외를 반환합니다.")
    @Test
    void createTest_ChannelNotExisting() {
        // given
        MessageCreateRequest messageCreateRequest = new MessageCreateRequest(MESSAGE_CONTENT, UUID.randomUUID(), UUID.randomUUID());

        BDDMockito.given(channelRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> messageService.create(messageCreateRequest, List.of()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("등록되지않은 유저가 메세지를 입력하면, 예외를 반환합니다.")
    @Test
    void createTest_UserNotExisting() {
        // given

        // when & then
    }

    @DisplayName("메세지 아이디로 조회하면, 메세지를 반환한다.")
    @Test
    void getByIdTest() {
        // given

        // when

        // then
    }

    @DisplayName("메세지 아이디로 조회했지만 없는 메세지라면, 예외를 반환한다.")
    @Test
    void getByIdTest_Exception() {
        // given

        // when & then
    }

    @DisplayName("메세지 내용 수정본을 받으면, 메세지 내용을 덮어쓴다.")
    @Test
    void updateContextTest() {
        // given

        // when

        // then
    }

    @DisplayName("메세지 내용 수정시도했지만 해당 메세지가 없다면, 예외를 반환한다.")
    @Test
    void updateContextTest_EntityNotFoundException() {
        // given

        // when & then
    }

    @DisplayName("메세지를 삭제하면, 메세지와 메세지에 등록된 첨부파일도 삭제한다.")
    @Test
    void deleteTest() {
        // given

        // when

        // then
    }

    @DisplayName("삭제하려는 메세지가 없으면 예외를 반환한다")
    @Test
    void deleteTest_EntityNotFound() {
        // given

        // when & then

    }


}
