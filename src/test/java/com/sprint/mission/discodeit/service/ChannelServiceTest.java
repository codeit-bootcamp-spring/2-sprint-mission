package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.InvalidChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
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
  private ChannelMapper channelMapper;

  private UUID channelId;
  private UUID userId;
  private String channelName;
  private String channelDescription;
  private Channel channel;
  private ChannelDto channelDto;
  private User user;

  @BeforeEach
  void setUp() {
    channelId = UUID.randomUUID();
    userId = UUID.randomUUID();
    channelName = "testChannel";
    channelDescription = "testDescription";

    channel = Channel.create(ChannelType.PUBLIC, channelName, channelDescription);
    ReflectionTestUtils.setField(channel, "id", channelId);
    channelDto = new ChannelDto(channelId, ChannelType.PUBLIC, channelName, channelDescription,
        List.of(), Instant.now());
    user = User.create("testUser", "test@example.com", "password", null);
  }

  @Test
  @DisplayName("Create Public Channel_성공")
  void createPublicChannel_shouldReturnCreatedChannel() {
    // given
    PublicChannelCreateRequest request = new PublicChannelCreateRequest(channelName,
        channelDescription);

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    // when
    ChannelDto result = channelService.createPublicChannel(request);

    // then
    then(channelRepository).should().save(any(Channel.class));
    then(channelMapper).should().toDto(channel);

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
    List<UUID> participantIds = List.of(userId);
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(userRepository.findAllById(participantIds)).willReturn(List.of(user));
    given(channelMapper.toDto(channel)).willReturn(channelDto);

    // when
    ChannelDto result = channelService.createPrivateChannel(request);

    // then
    then(channelRepository).should().save(any(Channel.class));
    then(userRepository).should().findAllById(participantIds);
    then(readStatusRepository).should(times(1)).<ReadStatus>saveAll(anyList());
    then(channelMapper).should().toDto(channel);

    assertThat(result.name()).isNull();
    assertThat(result.type()).isEqualTo(ChannelType.PRIVATE);
  }

  @Test
  @DisplayName("Update Channel_성공")
  void updateChannel_shouldReturnUpdatedChannel() {
    // given
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newPublicChannel",
        "update 테스트를 위한 PUBLIC 채널입니다.");

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(channelMapper.toDto(channel)).willReturn(channelDto);

    // when
    ChannelDto result = channelService.updateChannel(channelId, request);

    // then
    then(channelRepository).should().findById(channelId);
    then(channelMapper).should().toDto(channel);

    assertThat(result.name()).isEqualTo(request.newName());
    assertThat(result.description()).isEqualTo(request.newDescription());
  }

  @Test
  @DisplayName("Update Channel_실패_채널 유형이 PRIVATE일 때")
  void updateChannel_shouldThrowException_whenChannelTypeIsPrivate() {
    // given
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newPublicChannel",
        "update 테스트를 위한 PUBLIC 채널입니다.");

    Channel originalChannel = Channel.create(ChannelType.PRIVATE, "PrivateChannel",
        "테스트를 위한 PRIVATE 채널입니다.");
    ReflectionTestUtils.setField(originalChannel, "id", channelId);

    given(channelRepository.findById(channelId)).willReturn(Optional.of(originalChannel));

    // when & then
    InvalidChannelUpdateException exception = assertThrows(InvalidChannelUpdateException.class,
        () -> channelService.updateChannel(channelId, request));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_CHANNEL_TYPE);
    assertThat(exception.getDetails().get("channelId")).isEqualTo(channelId);

    then(channelRepository).should().findById(channelId);
    then(channelMapper).should(never()).toDto(any());
  }

  @Test
  @DisplayName("Update Channel_실패_채널이 존재하지 않을 때")
  void updateChannel_shouldThrowException_whenChannelNull() {
    // given
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newPublicChannel",
        "update 테스트를 위한 PUBLIC 채널입니다.");

    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // when & then
    ChannelNotFoundException exception = assertThrows(ChannelNotFoundException.class,
        () -> channelService.updateChannel(channelId, request));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CHANNEL_NOT_FOUND);
    assertThat(exception.getDetails().get("channelId")).isEqualTo(channelId);

    then(channelRepository).should().findById(channelId);
    then(channelMapper).should(never()).toDto(any());
  }

  @Test
  @DisplayName("Delete Channel_성공")
  void deleteChannel() {
    // given
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(messageRepository.existsByChannelId(channelId)).willReturn(true);

    // when
    channelService.deleteChannel(channelId);

    // then
    then(channelRepository).should().findById(channelId);
    then(messageRepository).should().existsByChannelId(channelId);
    then(messageRepository).should().deleteAllByChannelId(channelId);
    then(readStatusRepository).should().deleteAllByChannelId(channelId);
    then(channelRepository).should().deleteById(channelId);
  }

  @Test
  @DisplayName("Delete Channel_실패_채널이 존재하지 않을 때")
  void deleteChannel_shouldThrowException_whenChannelNotFound() {
    // given
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // when & then
    ChannelNotFoundException exception = assertThrows(ChannelNotFoundException.class,
        () -> channelService.deleteChannel(channelId));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CHANNEL_NOT_FOUND);
    assertThat(exception.getDetails().get("channelId")).isEqualTo(channelId);

    then(channelRepository).should().findById(channelId);
    then(channelRepository).should(never()).deleteById(channelId);
    then(messageRepository).shouldHaveNoMoreInteractions();
    then(readStatusRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  @DisplayName("findById_성공")
  void findById_Success() {
    // given
    given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(channel));
    given(channelMapper.toDto(channel)).willReturn(channelDto);

    // when
    ChannelDto result = channelService.findById(channelId);

    // then
    assertThat(result).isEqualTo(channelDto);

    then(channelRepository).should().findById(channelId);
    then(channelMapper).should().toDto(channel);
  }

  @Test
  @DisplayName("findById_실패_존재하지 않는 채널 조회")
  void findById_shouldThrowException_whenNonExistentId() {
    // given
    given(channelRepository.findById(eq(channelId))).willReturn(Optional.empty());

    // when & then
    ChannelNotFoundException exception = assertThrows(ChannelNotFoundException.class,
        () -> channelService.findById(channelId));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CHANNEL_NOT_FOUND);
    assertThat(exception.getDetails().get("channelId")).isEqualTo(channelId);

    then(channelRepository).should().findById(channelId);
    then(channelMapper).should(never()).toDto(channel);
  }

  @Test
  @DisplayName("FindAllByUserId_성공")
  void findAllByUserId_shouldReturnChannelList() {
    // given
    UUID privateChannelId = UUID.randomUUID();

    Channel privateChannel = Channel.create(ChannelType.PRIVATE, null, null);
    ReflectionTestUtils.setField(privateChannel, "id", privateChannelId);

    ReadStatus readStatus = ReadStatus.create(user, privateChannel, Instant.now());
    UserDto userDto = new UserDto(UUID.randomUUID(), user.getUsername(), user.getEmail(), null,
        true);
    ChannelDto privateDto = new ChannelDto(privateChannelId, ChannelType.PRIVATE, null, null,
        List.of(userDto), null);

    // Mock 설정
    given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(readStatus));
    given(channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        List.of(privateChannel.getId()))).willReturn(List.of(channel, privateChannel));
    given(channelMapper.toDto(channel))
        .willReturn(channelDto);
    given(channelMapper.toDto(privateChannel))
        .willReturn(privateDto);

    // when
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // then
    assertThat(result).containsExactlyInAnyOrder(channelDto, privateDto);

    then(readStatusRepository).should().findAllByUserId(userId);
    then(channelRepository).should().findAll();
    then(channelMapper).should().toDto(channel);
    then(channelMapper).should().toDto(privateChannel);
  }

}
