package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.InvalidChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

  @InjectMocks
  private BasicChannelService channelService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private ReadStatusRepository readStatusRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private ChannelMapper channelMapper;

  @Test
  @DisplayName("Create Public Channel_성공")
  void createPublicChannel_shouldReturnCreatedChannel() {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("publicChannel",
        "테스트를 위한 PUBLIC 채널입니다.");

    Channel createdChannel = Channel.create(ChannelType.PUBLIC, request.name(),
        request.description());
    ReflectionTestUtils.setField(createdChannel, "id", channelId);

    ChannelDto channelDto = new ChannelDto(channelId, ChannelType.PUBLIC, request.name(),
        request.description(), null, null);

    given(channelRepository.save(any(Channel.class))).willReturn(createdChannel);
    given(channelMapper.toDto(createdChannel, null, null)).willReturn(channelDto);

    // when
    ChannelDto result = channelService.createPublicChannel(request);

    // then
    then(channelRepository).should().save(any(Channel.class));
    then(channelMapper).should().toDto(createdChannel, null, null);

    assertThat(result.name()).isEqualTo(request.name());
    assertThat(result.description()).isEqualTo(request.description());
  }

  @Test
  @DisplayName("Create Public Channel_실패_채널 이름이 null일 때")
  void createPublicChannel_shouldThrowException_whenNameIsNull() {
    // given
    PublicChannelCreateRequest request = new PublicChannelCreateRequest(null, "설명");

    // when & then
    assertThrows(IllegalArgumentException.class,
        () -> channelService.createPublicChannel(request));
  }

  @Test
  @DisplayName("Create Private Channel_성공")
  void createPrivateChannel_shouldReturnCreatedChannel() {
    // given
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();
    List<UUID> participantIds = Arrays.asList(userId1, userId2);

    User user1 = User.create("유저1", "user1@email.com", "user1234!", null);
    User user2 = User.create("유저2", "user2@email.com", "user4321!", null);
    ReflectionTestUtils.setField(user1, "id", userId1);
    ReflectionTestUtils.setField(user2, "id", userId2);

    UserStatus status1 = UserStatus.create(user1, Instant.now());
    ReflectionTestUtils.setField(user1, "status", status1);
    UserStatus status2 = UserStatus.create(user2, Instant.now());
    ReflectionTestUtils.setField(user2, "status", status1);

    UserDto userDto1 = new UserDto(userId1, user1.getUsername(), user1.getEmail(), null, true);
    UserDto userDto2 = new UserDto(userId2, user2.getUsername(), user2.getEmail(), null, true);
    List<UserDto> participantDtos = Arrays.asList(userDto1, userDto2);

    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);

    UUID channelId = UUID.randomUUID();
    Channel createdChannel = Channel.create(ChannelType.PRIVATE, null, null);
    ReflectionTestUtils.setField(createdChannel, "id", channelId);

    ChannelDto channelDto = new ChannelDto(channelId, ChannelType.PRIVATE, null,
        null, participantDtos, null);

    given(userRepository.findById(userId1)).willReturn(Optional.of(user1));
    given(userRepository.findById(userId2)).willReturn(Optional.of(user2));
    given(channelRepository.save(any(Channel.class))).willReturn(createdChannel);
    given(userMapper.toDto(user1, user1.getStatus().isOnline())).willReturn(userDto1);
    given(userMapper.toDto(user2, user2.getStatus().isOnline())).willReturn(userDto2);
    given(channelMapper.toDto(createdChannel, participantDtos, null)).willReturn(channelDto);

    // when
    ChannelDto result = channelService.createPrivateChannel(request);

    // then
    then(userRepository).should().findById(userId1);
    then(userRepository).should().findById(userId2);
    then(channelRepository).should().save(any(Channel.class));
    then(readStatusRepository).should(times(2)).save(any(ReadStatus.class));
    then(userMapper).should().toDto(user1, user1.getStatus().isOnline());
    then(userMapper).should().toDto(user2, user2.getStatus().isOnline());
    then(channelMapper).should().toDto(createdChannel, participantDtos, null);

    assertThat(result.name()).isNull();
    assertThat(result.type()).isEqualTo(ChannelType.PRIVATE);
    assertThat(result.participants()).containsExactlyInAnyOrder(userDto1, userDto2);
  }

  @Test
  @DisplayName("Create Private Channel_실패_참가자가 존재하지 않는 사용자일 때")
  void createPrivateChannel_shouldThrowException_whenParticipantNotFound() {
    // given
    UUID nonExistentUserId = UUID.randomUUID();
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
        List.of(nonExistentUserId));

    given(userRepository.findById(nonExistentUserId)).willReturn(Optional.empty());

    // when & then
    then(userRepository).should().findById(nonExistentUserId);
    then(channelRepository).should(never()).save(any());
    then(readStatusRepository).should(never()).save(any());
    then(channelMapper).should(never()).toDto(any(), any(), any());

    UserNotFoundException exception = assertThrows(UserNotFoundException.class,
        () -> channelService.createPrivateChannel(request));
    assertThat(exception.getDetails().get("userId")).isEqualTo(nonExistentUserId);
    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
  }

  @Test
  @DisplayName("Update Channel_성공")
  void updateChannel_shouldReturnUpdatedChannel() {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newPublicChannel",
        "update 테스트를 위한 PUBLIC 채널입니다.");

    Channel originalChannel = Channel.create(ChannelType.PUBLIC, "PublicChannel",
        "테스트를 위한 PUBLIC 채널입니다.");
    ReflectionTestUtils.setField(originalChannel, "id", channelId);

    ChannelDto channelDto = new ChannelDto(channelId, ChannelType.PUBLIC, request.newName(),
        request.newDescription(), null, null);

    when(channelRepository.findById(channelId)).thenReturn(Optional.of(originalChannel));
    when(channelMapper.toDto(originalChannel, null, null)).thenReturn(channelDto);

    // when
    ChannelDto result = channelService.updateChannel(channelId, request);

    // then
    assertThat(result.name()).isEqualTo(request.newName());
    assertThat(result.description()).isEqualTo(request.newDescription());

    verify(channelRepository).findById(channelId);
    verify(channelMapper).toDto(originalChannel, null, null);
  }

  @Test
  @DisplayName("Update Channel_실패_채널 유형이 PRIVATE일 때")
  void updateChannel_shouldThrowException_whenChannelTypeIsPrivate() {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newPublicChannel",
        "update 테스트를 위한 PUBLIC 채널입니다.");

    Channel originalChannel = Channel.create(ChannelType.PRIVATE, "PrivateChannel",
        "테스트를 위한 PRIVATE 채널입니다.");
    ReflectionTestUtils.setField(originalChannel, "id", channelId);

    when(channelRepository.findById(channelId)).thenReturn(Optional.of(originalChannel));

    // when & then
    InvalidChannelUpdateException exception = assertThrows(InvalidChannelUpdateException.class,
        () -> channelService.updateChannel(channelId, request));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_CHANNEL_TYPE);
    assertThat(exception.getDetails().get("channelId")).isEqualTo(channelId);

    verify(channelRepository).findById(channelId);
    verify(channelMapper, never()).toDto(any(), any(), any());
  }

  @Test
  @DisplayName("Delete Channel_성공")
  void deleteChannel() {
    // given
    UUID channelId = UUID.randomUUID();

    Channel channel = Channel.create(ChannelType.PRIVATE, null,
        null);
    ReflectionTestUtils.setField(channel, "id", channelId);

    // Mock 설정
    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
    when(messageRepository.existsByChannelId(channelId)).thenReturn(true);

    // when
    channelService.deleteChannel(channelId);

    // then
    verify(channelRepository).findById(channelId);
    verify(messageRepository).existsByChannelId(channelId);
    verify(messageRepository).deleteAllByChannelId(channelId);
    verify(readStatusRepository).deleteAllByChannelId(channelId);
    verify(channelRepository).deleteById(channelId);
  }

  @Test
  @DisplayName("Delete Channel_실패_채널이 존재하지 않을 때")
  void deleteChannel_shouldThrowException_whenChannelNotFound() {
    // given
    UUID channelId = UUID.randomUUID();

    Channel channel = Channel.create(ChannelType.PUBLIC, "PublicChannel",
        "테스트를 위한 PUBLIC 채널입니다.");
    ReflectionTestUtils.setField(channel, "id", channelId);

    // Mock 설정
    when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

    // when & then
    ChannelNotFoundException exception = assertThrows(ChannelNotFoundException.class,
        () -> channelService.deleteChannel(channelId));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CHANNEL_NOT_FOUND);
    assertThat(exception.getDetails().get("channelId")).isEqualTo(channelId);

    verify(channelRepository).findById(channelId);
    verify(channelRepository, never()).deleteById(channelId);
    verifyNoMoreInteractions(messageRepository, readStatusRepository);
  }

  @Test
  @DisplayName("FindAllByUserId_성공")
  void findAllByUserId_shouldReturnChannelList() {
    // given
    UUID userId = UUID.randomUUID();
    UUID publicChannelId = UUID.randomUUID();
    UUID privateChannelId = UUID.randomUUID();

    Channel publicChannel = Channel.create(ChannelType.PUBLIC, "public", "desc");
    Channel privateChannel = Channel.create(ChannelType.PRIVATE, null, null);
    ReflectionTestUtils.setField(publicChannel, "id", publicChannelId);
    ReflectionTestUtils.setField(privateChannel, "id", privateChannelId);

    User user = User.create("유저", "test@email.com", "password", null);
    UserStatus status = UserStatus.create(user, Instant.now());
    ReflectionTestUtils.setField(user, "status", status);

    ReadStatus readStatus = ReadStatus.create(user, privateChannel, Instant.now());

    UserDto userDto = new UserDto(UUID.randomUUID(), user.getUsername(), user.getEmail(), null,
        true);

    ChannelDto publicDto = new ChannelDto(publicChannelId, ChannelType.PUBLIC,
        publicChannel.getName(),
        publicChannel.getDescription(),
        new ArrayList<>(),
        Instant.parse("2024-01-01T10:00:00Z"));
    ChannelDto privateDto = new ChannelDto(privateChannelId, ChannelType.PRIVATE, null, null,
        List.of(userDto), null);

    // Mock 설정
    when(readStatusRepository.findAllByUserId(userId)).thenReturn(List.of(readStatus));
    when(channelRepository.findAll()).thenReturn(List.of(publicChannel, privateChannel));
    when(messageRepository.findLatestCreatedAtByChannelId(publicChannelId))
        .thenReturn(Optional.of(Instant.parse("2024-01-01T10:00:00Z")));
    when(messageRepository.findLatestCreatedAtByChannelId(privateChannelId))
        .thenReturn(Optional.empty());

    when(readStatusRepository.findAllByChannelId(privateChannelId))
        .thenReturn(List.of(readStatus));
    when(userMapper.toDto(user, user.getStatus().isOnline())).thenReturn(userDto);

    when(channelMapper.toDto(publicChannel, new ArrayList<>(),
        Instant.parse("2024-01-01T10:00:00Z")))
        .thenReturn(publicDto);
    when(channelMapper.toDto(privateChannel, List.of(userDto), null))
        .thenReturn(privateDto);

    // when
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // then
    assertThat(result).containsExactlyInAnyOrder(publicDto, privateDto);

    verify(readStatusRepository).findAllByUserId(userId);
    verify(channelRepository).findAll();
    verify(messageRepository).findLatestCreatedAtByChannelId(publicChannelId);
    verify(messageRepository).findLatestCreatedAtByChannelId(privateChannelId);
    verify(readStatusRepository).findAllByChannelId(privateChannelId);
    verify(userMapper).toDto(user, true);
    verify(channelMapper).toDto(publicChannel, new ArrayList<>(),
        Instant.parse("2024-01-01T10:00:00Z"));
    verify(channelMapper).toDto(privateChannel, List.of(userDto), null);
  }

  @Test
  @DisplayName("FindAllByUserId_실패 케이스 처리_유저 상태가 null인 경우 NPE 없이 처리")
  void findAllByUserId_shouldHandleNullUserStatus() {
    // given
    UUID userId = UUID.randomUUID();
    UUID privateChannelId = UUID.randomUUID();

    Channel privateChannel = Channel.create(ChannelType.PRIVATE, null, null);
    ReflectionTestUtils.setField(privateChannel, "id", privateChannelId);

    // User with null status
    User user = User.create("유저", "test@email.com", "password123!", null);
    ReflectionTestUtils.setField(user, "id", userId);
    // status는 null 상태

    ReadStatus readStatus = ReadStatus.create(user, privateChannel, Instant.now());
    UserDto userDto = new UserDto(userId, user.getUsername(), user.getEmail(), null, false);
    ChannelDto channelDto = new ChannelDto(privateChannelId, ChannelType.PRIVATE, null, null,
        List.of(userDto), null);

    // Mock 설정
    when(readStatusRepository.findAllByUserId(userId)).thenReturn(List.of(readStatus));
    when(channelRepository.findAll()).thenReturn(List.of(privateChannel));
    when(messageRepository.findLatestCreatedAtByChannelId(privateChannelId)).thenReturn(
        Optional.empty());
    when(readStatusRepository.findAllByChannelId(privateChannelId)).thenReturn(List.of(readStatus));
    when(userMapper.toDto(user, false)).thenReturn(userDto);
    when(channelMapper.toDto(privateChannel, List.of(userDto), null)).thenReturn(channelDto);

    // when
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // then
    assertThat(result).containsExactly(channelDto);

    verify(readStatusRepository).findAllByUserId(userId);
    verify(channelRepository).findAll();
    verify(messageRepository).findLatestCreatedAtByChannelId(privateChannelId);
    verify(readStatusRepository).findAllByChannelId(privateChannelId);
    verify(userMapper).toDto(user, false);
    verify(channelMapper).toDto(privateChannel, List.of(userDto), null);
  }


}
