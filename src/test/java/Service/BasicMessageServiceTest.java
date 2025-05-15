package Service;

import com.sprint.mission.discodeit.dto.service.message.CreateMessageCommand;
import com.sprint.mission.discodeit.dto.service.message.CreateMessageResult;
import com.sprint.mission.discodeit.dto.service.message.FindMessageResult;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageCommand;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.MessageMapperImpl;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class BasicMessageServiceTest {

  @Mock
  MessageRepository messageRepository;

  @Mock
  ChannelRepository channelRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  BinaryContentService binaryContentService;

  @Mock
  BinaryContentStorage binaryContentStorage;

  @Spy
  MessageMapper messageMapper = new MessageMapperImpl();

  @InjectMocks
  BasicMessageService basicMessageService;

  @Test
  @DisplayName("메시지 생성 성공")
  void createMessage_success() {
    // given
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    CreateMessageCommand createMessageCommand = new CreateMessageCommand("test", channelId,
        authorId);
    // 갑자기 든 생각.. MessageService를 테스트하는데, User 필드를 굳이 채워줄 필요가 없을 것 같다.
    User user = User.builder().build();
    ReflectionTestUtils.setField(user, "id", authorId);
    ReflectionTestUtils.setField(user, "userStatus", new UserStatus(user, Instant.now()));
    Channel channel = Channel.builder().build();
    ReflectionTestUtils.setField(channel, "id", channelId);

    List<MultipartFile> multipartFiles = List.of(
        new MockMultipartFile("file", "test.png", "image/png", "test".getBytes()));

    given(userRepository.findById(authorId)).willReturn(Optional.of(user));
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

    // when
    CreateMessageResult messageResult = basicMessageService.create(createMessageCommand,
        multipartFiles);

    // then
    assertThat(createMessageCommand.content()).isEqualTo(messageResult.content());
    assertThat(createMessageCommand.authorId()).isEqualTo(messageResult.author().id());
    assertThat(createMessageCommand.channelId()).isEqualTo(messageResult.channelId());
    assertThat(multipartFiles.size()).isEqualTo(messageResult.attachments().size());

    then(messageRepository).should(times(1)).save(any(Message.class));
    then(binaryContentService).should(times(1)).create(any(BinaryContent.class));
    // 실제 메서드에서는 DB를 이용하기 때문에, GeneratedValue UUID로 Id가 생성되지만, 테스트 환경에서는 생성이 되지 않음
    // binaryContentStorage.put(binaryContent.getId())를 할 때, null로 들어가기 때문에 any(UUID.class)를 넣어주면 테스트가 실패
    // 호출 정도만 확인하는 것이기 때문에 any() 사용
    then(binaryContentStorage).should(times(1)).put(any(), any());
  }

  @Test
  @DisplayName("메시지 생성할 때, 유저를 찾지 못하는 경우 실패")
  void createUser_userNotFound_failed() {
    // given
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    List<MultipartFile> multipartFile = List.of(mock(MultipartFile.class));
    CreateMessageCommand createMessageCommand = new CreateMessageCommand("test", channelId,
        authorId);
    given(userRepository.findById(authorId)).willReturn(Optional.empty());

    // when + then
    assertThatThrownBy(() -> basicMessageService.create(createMessageCommand, multipartFile))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessageContaining("User not found");

    then(userRepository).should(times(1)).findById(authorId);
    then(channelRepository).should(never()).findById(channelId);
    then(binaryContentStorage).should(never()).put(any(), any());
    then(binaryContentService).should(never()).create(any());
    then(messageRepository).should(never()).save(any(Message.class));
  }

  @Test
  @DisplayName("메시지 생성할 때, 채널을 찾지 못하는 경우 실패")
  void createMessage_channelNotFound_failed() {
    // given
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    User user = User.builder().build();
    List<MultipartFile> multipartFile = List.of(mock(MultipartFile.class));
    CreateMessageCommand createMessageCommand = new CreateMessageCommand("test", channelId,
        authorId);
    given(userRepository.findById(authorId)).willReturn(Optional.of(user));
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // when + then
    assertThatThrownBy(() -> basicMessageService.create(createMessageCommand, multipartFile))
        .isInstanceOf(ChannelNotFoundException.class)
        .hasMessageContaining("Channel not found");

    then(userRepository).should(times(1)).findById(authorId);
    then(channelRepository).should(times(1)).findById(channelId);
    then(binaryContentStorage).should(never()).put(any(), any());
    then(binaryContentService).should(never()).create(any());
    then(messageRepository).should(never()).save(any(Message.class));
  }

  @Test
  @DisplayName("커서가 없을 때, 채널 별 메시지 조회 성공")
  void findMessage_all_byChannelId_noCursor_success() {
    // given
    UUID channelId = UUID.randomUUID();
    Channel channel = Channel.builder().build();
    ReflectionTestUtils.setField(channel, "id", channelId);
    Message message = Message.builder().channel(channel).build();
    int limit = 20;
    Pageable pageable = PageRequest.of(0, limit);
    Slice<Message> messages = new SliceImpl<>(List.of(message), pageable, true);
    FindMessageResult expected = messageMapper.toFindMessageResult(message);

    given(messageRepository.findAllByChannelIdInitial(channelId, pageable)).willReturn(messages);

    // when
    Slice<FindMessageResult> result = basicMessageService.findAllByChannelIdInitial(channelId,
        limit);

    // then
    assertThat(result.getContent().size()).isEqualTo(1);
    assertThat(result.getContent().get(0)).isEqualTo(expected);

    then(messageRepository).should(times(1)).findAllByChannelIdInitial(channelId, pageable);
  }

  @Test
  @DisplayName("커서가 있을 때, 채널 별 메시지 조회 성공")
  void findMessage_all_byChannelId_withCursor_success() {
    // given
    UUID channelId = UUID.randomUUID();
    Channel channel = Channel.builder().build();
    ReflectionTestUtils.setField(channel, "id", channelId);
    Message message = Message.builder().channel(channel).build();
    int limit = 20;
    Instant cursor = Instant.now();
    Pageable pageable = PageRequest.of(0, limit);
    Slice<Message> messages = new SliceImpl<>(List.of(message), pageable, true);
    FindMessageResult expected = messageMapper.toFindMessageResult(message);

    given(messageRepository.findAllByChannelIdAfterCursor(channelId, cursor,
        pageable)).willReturn(messages);

    // when
    Slice<FindMessageResult> result = basicMessageService.findAllByChannelIdAfterCursor(channelId,
        cursor, limit);

    // then
    assertThat(result.getContent().size()).isEqualTo(1);
    assertThat(result.getContent().get(0)).isEqualTo(expected);

    then(messageRepository).should(times(1))
        .findAllByChannelIdAfterCursor(channelId, cursor, pageable);
  }

  @Test
  @DisplayName("메시지 수정 성공")
  void updateMessage_success() {
    // given
    UUID messageId = UUID.randomUUID();
    Channel channel = Channel.builder().build();
    Message message = Message.builder()
        .channel(channel)
        .attachments(List.of(mock(BinaryContent.class)))
        .build();
    ReflectionTestUtils.setField(message, "id", messageId);
    List<MultipartFile> multipartFiles = List.of(
        new MockMultipartFile("file", "test.png", "image/png", "test".getBytes()));
    UpdateMessageCommand updateMessageCommand = new UpdateMessageCommand("updated content");
    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

    // when
    UpdateMessageResult updateMessageResult = basicMessageService.update(messageId,
        updateMessageCommand, multipartFiles);

    // then
    assertThat(updateMessageResult.content()).isEqualTo(updateMessageCommand.newContent());
    assertThat(updateMessageResult.attachments().size()).isEqualTo(1);
    assertThat(updateMessageResult.attachments().get(0).filename()).isEqualTo(
        multipartFiles.get(0).getOriginalFilename());

    then(messageRepository).should(times(1)).findById(messageId);
    then(binaryContentService).should(times(1)).delete(any());
    then(binaryContentStorage).should(times(1)).delete(any());
    then(binaryContentService).should(times(1)).create(any());
    then(binaryContentStorage).should(times(1)).put(any(), any());
    then(messageRepository).should(times(1)).save(message);
  }

  @Test
  @DisplayName("메시지 수정할 때, 메시지를 찾지 못하는 경우 실패")
  void updateMessage_messageNotFound_failed() {
    // given
    UUID messageId = UUID.randomUUID();
    UpdateMessageCommand updateMessageCommand = new UpdateMessageCommand("updated content");
    List<MultipartFile> multipartFiles = List.of(mock(MultipartFile.class));
    given(messageRepository.findById(messageId)).willReturn(Optional.empty());

    // when
    assertThatThrownBy(
        () -> basicMessageService.update(messageId, updateMessageCommand, multipartFiles))
        .isInstanceOf(MessageNotFoundException.class)
        .hasMessageContaining("Message not found");

    // then
    then(messageRepository).should(times(1)).findById(messageId);
    then(binaryContentService).should(never()).delete(any());
    then(binaryContentStorage).should(never()).delete(any());
    then(binaryContentService).should(never()).create(any());
    then(binaryContentStorage).should(never()).put(any(), any());
    then(messageRepository).should(never()).save(any(Message.class));
  }

  @Test
  @DisplayName("메시지 삭제 성공")
  void deleteMessage_success() {
    // given
    UUID messageId = UUID.randomUUID();
    Message message = Message.builder()
        .attachments(List.of(mock(BinaryContent.class)))
        .build();
    ReflectionTestUtils.setField(message, "id", messageId);

    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

    // when
    basicMessageService.delete(messageId);

    // then
    then(messageRepository).should(times(1)).findById(messageId);
    then(binaryContentService).should(times(1)).delete(any());
    then(messageRepository).should(times(1)).deleteById(messageId);
  }

  @Test
  @DisplayName("메시지 삭제할 때, 메시지를 찾지 못하는 경우 실패")
  void deleteMessage_messageNotFound_failed() {
    // given
    UUID messageId = UUID.randomUUID();
    given(messageRepository.findById(messageId)).willReturn(Optional.empty());

    // when
    assertThatThrownBy(() -> basicMessageService.delete(messageId))
        .isInstanceOf(MessageNotFoundException.class)
        .hasMessageContaining("Message not found");

    // then
    then(messageRepository).should(times(1)).findById(messageId);
    then(binaryContentService).should(never()).delete(any());
    then(messageRepository).should(never()).deleteById(any());
  }
}