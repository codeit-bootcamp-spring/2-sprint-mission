package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelException;
import com.sprint.mission.discodeit.exception.UserException;
import com.sprint.mission.discodeit.exception.common.ErrorCode;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class BasicChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private ReadStatusRepository readStatusRepository;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private ChannelMapper channelMapper;

  @InjectMocks
  private BasicChannelService basicChannelService;

  private UUID channelId;
  private UUID userId;
  private Channel channel;
  private ChannelDto channelDto;
  private User user;

  @BeforeEach
  void setUp() {
    channelId = UUID.randomUUID();
    userId = UUID.randomUUID();
    String channelName = "testChannel";
    String channelDescription = "testDescription";
    channel = new Channel(ChannelType.PUBLIC, channelName, channelDescription);
    ReflectionTestUtils.setField(channel, "id", channelId);
    channelDto = new ChannelDto(channelId, ChannelType.PUBLIC, channelName, channelDescription,
        List.of(), Instant.now());
    user = new User("testUser", "test@example.com", "password", null);
  }

  @Test
  @DisplayName("공개 채널 생성 성공")
  void testCreatePublicChannelSuccess() {
    String name = "Public Channel";
    String description = "Public Channel Description";

    Channel savedChannel = new Channel(ChannelType.PUBLIC, name, description);
    PublicChannelCreateRequest request = new PublicChannelCreateRequest(name, description);

    ChannelDto expectedChannelDto = new ChannelDto(savedChannel.getId(), ChannelType.PUBLIC, name,
        description, null, null);

    given(channelRepository.save(any(Channel.class))).willReturn(savedChannel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(expectedChannelDto);

    ChannelDto result = basicChannelService.create(request);

    assertEquals(expectedChannelDto, result);

    then(channelRepository).should().save(any(Channel.class));
    then(channelMapper).should().toDto(any(Channel.class));
  }

  @Test
  @DisplayName("비공개 채널 생성 실패 - 사용자 없음")
  void testCreatePrivateChannelFailure_nonExistentUser() {
    List<UUID> participantIds = List.of();

    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);

    UserException exception = assertThrows(UserException.class, () -> {
      basicChannelService.create(request);
    });

    assertEquals(ErrorCode.INVALID_USER_LIST.getMessage(), exception.getMessage());
    assertTrue(exception.getDetails().containsKey("participantIds"));
    assertEquals(participantIds, exception.getDetails().get("participantIds"));

    then(channelRepository).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("채널 업데이트 성공")
  void testUpdateChannelSuccess() {
    String newName = "Updated Channel";
    String newDescription = "Updated Channel Description";

    Channel existingChannel = new Channel(ChannelType.PUBLIC, "Old Channel", "Old Description");
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(newName, newDescription);
    Channel updatedChannel = new Channel(ChannelType.PUBLIC, newName, newDescription);

    ChannelDto expectedChannelDto = new ChannelDto(channelId, ChannelType.PUBLIC, newName,
        newDescription, null, null);

    given(channelRepository.findById(any(UUID.class)))
        .willReturn(Optional.of(existingChannel));
    given(channelRepository.save(any(Channel.class))).willReturn(updatedChannel);
    given(channelMapper.toDto(any(Channel.class)))
        .willReturn(expectedChannelDto);

    ChannelDto result = basicChannelService.update(channelId, request);

    assertEquals(expectedChannelDto, result);

    then(channelRepository).should().findById(any(UUID.class));
    then(channelRepository).should().save(Mockito.argThat(channel ->
        channel.getName().equals(newName) &&
            channel.getDescription().equals(newDescription)));
    then(channelMapper).should().toDto(any(Channel.class));
  }

  @Test
  @DisplayName("비공개 채널 업데이트 실패")
  void testUpdatePrivateChannelFailure() {
    String newName = "Updated Private Channel";
    String newDescription = "Updated Private Channel Description";

    Channel existingPrivateChannel = new Channel(ChannelType.PRIVATE, "Old Private Channel",
        "Old Description");

    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(newName, newDescription);

    given(channelRepository.findById(any(UUID.class)))
        .willReturn(Optional.of(existingPrivateChannel));

    ChannelException exception = assertThrows(ChannelException.class, () -> {
      basicChannelService.update(channelId, request);
    });

    assertEquals(ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED.getMessage(), exception.getMessage());
    assertTrue(exception.getDetails().containsKey("channelId"));
    assertEquals(channelId.toString(), exception.getDetails().get("channelId").toString());

    then(channelRepository).should().findById(any(UUID.class));
    then(channelRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  @DisplayName("채널 삭제 성공")
  void testDeleteChannelSuccess() {
    given(channelRepository.existsById(any(UUID.class))).willReturn(true);

    basicChannelService.delete(channelId);

    then(messageRepository).should().deleteAllByChannelId(any(UUID.class));
    then(readStatusRepository).should().deleteAllByChannelId(any(UUID.class));
    then(channelRepository).should().deleteById(any(UUID.class));
  }

  @Test
  @DisplayName("채널 삭제 실패 - 채널이 존재하지 않음")
  void testDeleteChannelFailure_channelNotFound() {
    given(channelRepository.existsById(any(UUID.class))).willReturn(false);

    ChannelException exception = assertThrows(ChannelException.class, () -> {
      basicChannelService.delete(channelId);
    });

    assertEquals(ErrorCode.CHANNEL_NOT_FOUND.getMessage(), exception.getMessage());
    assertTrue(exception.getDetails().containsKey("channelId"));
    assertEquals(channelId.toString(), exception.getDetails().get("channelId").toString());

    then(channelRepository).should().existsById(any(UUID.class));
    then(channelRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  @DisplayName("사용자가 참가한 채널 조회 성공")
  void testFindByUserIdSuccess() {
    List<ReadStatus> readStatuses = List.of(new ReadStatus(user, channel, Instant.now()));
    given(readStatusRepository.findAllByUserId(eq(userId))).willReturn(readStatuses);
    given(
        channelRepository.findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), eq(List.of(channel.getId()))))
        .willReturn(List.of(channel));
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    List<ChannelDto> result = basicChannelService.findAllByUserId(userId);

    assertThat(result).containsExactly(channelDto);
  }

  @Test
  @DisplayName("사용자가 참가한 채널 조회 실패 - 가입된 채널이 없을 경우")
  void testFindByUserIdFailure_noSubscribedChannels() {
    List<ReadStatus> readStatuses = List.of();
    List<Channel> channels = List.of();

    List<ChannelDto> expectedChannelDtos = List.of();

    given(readStatusRepository.findAllByUserId(eq(userId))).willReturn(readStatuses);
    given(channelRepository.findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), eq(List.of())))
        .willReturn(channels);

    List<ChannelDto> result = basicChannelService.findAllByUserId(userId);

    assertEquals(expectedChannelDtos, result);

    then(readStatusRepository).should().findAllByUserId(any(UUID.class));
    then(channelRepository).should().findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), eq(List.of()));
  }
}
