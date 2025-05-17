package com.sprint.mission.discodeit.message.service;

import com.sprint.mission.discodeit.binarycontent.service.BinaryContentStorageService;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.dto.MessageResult;
import com.sprint.mission.discodeit.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.mapper.MessageResultMapper;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.message.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

    protected static final String MESSAGE_CONTENT = "안녕하세요";
    protected static final String UPDATED_MESSAGE_CONTENT = "반갑습니다";
    protected static final String USER_EMAIL = "email";

    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BinaryContentStorageService binaryContentStorageService;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private MessageResultMapper messageResultMapper;
    @InjectMocks
    private BasicMessageService messageService;

    @DisplayName("유저가 메세지 내용을 입력하면, 채널에 메세지를 생성합니다.")
    @Test
    void createTest() {
        // given
        UUID channelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String content = "안녕하세요";

        MessageCreateRequest request = new MessageCreateRequest(content, channelId, userId);

        // 단순한 유저/채널만 stub
        Channel channel = new Channel(ChannelType.PUBLIC, "name", "desc");
        User user = new User("username", USER_EMAIL, "pass", null);
        Message savedMessage = new Message(channel, user, content, List.of());

        // 최종 결과만 stub
        MessageResult expectedResult = new MessageResult(
                UUID.randomUUID(),
                null,
                null,
                UserResult.fromEntity(user, false),
                content,
                channelId,
                List.of()
        );

        BDDMockito.given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        BDDMockito.given(userRepository.findById(userId)).willReturn(Optional.of(user));
        BDDMockito.given(messageRepository.save(any())).willReturn(savedMessage);
        BDDMockito.given(messageResultMapper.convertToMessageResult(savedMessage, user)).willReturn(expectedResult);

        // when
        MessageResult result = messageService.create(request, List.of());

        // then
        assertAll(
                () -> Assertions.assertThat(result.content()).isEqualTo(content),
                () -> Assertions.assertThat(result.author().email()).isEqualTo(USER_EMAIL)
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
        MessageCreateRequest messageCreateRequest = new MessageCreateRequest(MESSAGE_CONTENT, UUID.randomUUID(), UUID.randomUUID());
        Channel channel = new Channel(ChannelType.PUBLIC, "", "");

        BDDMockito.given(channelRepository.findById(any())).willReturn(Optional.of(channel));
        BDDMockito.given(userRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> messageService.create(messageCreateRequest, List.of()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("메세지 아이디로 조회하면, 메세지를 반환한다.")
    @Test
    void getByIdTest() {
        // given
        Channel channel = new Channel(ChannelType.PUBLIC, "", "");
        User user = new User("", "", "", null);
        Message message = new Message(channel, user, MESSAGE_CONTENT, List.of());
        MessageResult expectedResult = new MessageResult(null, null, null,
                UserResult.fromEntity(user, false), message.getContext(), UUID.randomUUID(), List.of());

        BDDMockito.given(messageRepository.findById(any()))
                .willReturn(Optional.of(message));
        BDDMockito.given(messageResultMapper.convertToMessageResult(any(), any()))
                .willReturn(expectedResult);

        // when
        MessageResult messageResult = messageService.getById(UUID.randomUUID());

        // then
        Assertions.assertThat(messageResult.content()).isEqualTo(MESSAGE_CONTENT);
    }

    @DisplayName("메세지 아이디로 조회했지만 없는 메세지라면, 예외를 반환한다.")
    @Test
    void getByIdTest_Exception() {
        // given
        BDDMockito.given(messageRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> messageService.getById(UUID.randomUUID()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("메세지 내용 수정본을 받으면, 메세지 내용을 덮어쓴다.")
    @Test
    void updateContextTest() {
        // given
        Channel channel = new Channel(ChannelType.PUBLIC, "", "");
        User user = new User("", "", "", null);
        Message message = new Message(channel, user, MESSAGE_CONTENT, List.of());
        Message updatedMessage = new Message(channel, user, UPDATED_MESSAGE_CONTENT, List.of());
        MessageResult expectedResult = new MessageResult(null, null, null,
                UserResult.fromEntity(user, false), updatedMessage.getContext(), UUID.randomUUID(), List.of());

        BDDMockito.given(messageRepository.findById(any()))
                .willReturn(Optional.of(message));
        BDDMockito.given(messageRepository.save(any()))
                .willReturn(updatedMessage);
        BDDMockito.given(messageResultMapper.convertToMessageResult(any(), any()))
                .willReturn(expectedResult);

        // when
        MessageResult messageResult = messageService.updateContext(UUID.randomUUID(), UPDATED_MESSAGE_CONTENT);

        // then
        Assertions.assertThat(messageResult.content()).isEqualTo(UPDATED_MESSAGE_CONTENT);
    }

    @DisplayName("메세지 내용 수정시도했지만 해당 메세지가 없다면, 예외를 반환한다.")
    @Test
    void updateContextTest_EntityNotFoundException() {
        // given
        BDDMockito.given(messageRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> messageService.updateContext(UUID.randomUUID(), UPDATED_MESSAGE_CONTENT))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("메세지를 삭제하면, 메세지와 메세지에 등록된 첨부파일도 삭제한다.")
    @Test
    void deleteTest() {
        // given
        Channel channel = new Channel(ChannelType.PUBLIC, "", "");
        User user = new User("", "", "", null);
        Message message = new Message(channel, user, "hello", List.of());

        BDDMockito.given(messageRepository.findById(any())).willReturn(Optional.of(message));

        // when
        messageService.delete(UUID.randomUUID());

        // then
        BDDMockito.verify(binaryContentStorageService).deleteBinaryContentsBatch(any());
        BDDMockito.verify(messageRepository).deleteById(any());
    }

    @DisplayName("삭제하려는 메세지가 없으면 예외를 반환한다")
    @Test
    void deleteTest_EntityNotFound() {
        // given
        BDDMockito.given(messageRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> messageService.delete(UUID.randomUUID()))
                .isInstanceOf(EntityNotFoundException.class);
    }

}