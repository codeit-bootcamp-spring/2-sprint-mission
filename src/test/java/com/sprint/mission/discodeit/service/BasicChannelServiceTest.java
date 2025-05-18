package com.sprint.mission.discodeit.service;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.ChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

  @Mock
  ChannelRepository channelRepository;
  @Mock
  ReadStatusRepository readStatusRepository;
  @Mock
  MessageRepository messageRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  ChannelMapper channelMapper;

  @InjectMocks
  BasicChannelService channelService;

  // 더미 UserDto 생성 메소드
  private UserDto dummyUserDto(String username) {
    return new UserDto(
        UUID.randomUUID(),
        username,
        username + "@example.com",
        null, // profile
        true
    );
  }

  // CREATE (PUBLIC)
  @Test
  void createPublicChannel_success() {
    // Given
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("공지", "공지사항 채널");
    Channel channel = new Channel(ChannelType.PUBLIC, "공지", "공지사항 채널");
    List<UserDto> participants = List.of(dummyUserDto("user1"), dummyUserDto("user2"));
    ChannelDto dto = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PUBLIC,
        "공지",
        "공지사항 채널",
        participants,
        Instant.now()
    );

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(dto);

    // When
    ChannelDto result = channelService.create(request);

    // Then
    assertNotNull(result);
    assertEquals("공지", result.name());
    assertEquals(ChannelType.PUBLIC, result.type());
    assertEquals(2, result.participants().size());
    then(channelRepository).should().save(any(Channel.class));
  }

  // CREATE (PRIVATE)
  @Test
  void createPrivateChannel_success() {
    // Given
    UUID userId = UUID.randomUUID();
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(userId));
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    List<UserDto> participants = List.of(dummyUserDto("user1"));
    ChannelDto dto = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PRIVATE,
        null,
        null,
        participants,
        Instant.now()
    );

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(userRepository.findAllById(any())).willReturn(List.of(mock(User.class)));
    given(readStatusRepository.saveAll(any())).willReturn(Collections.emptyList());
    given(channelMapper.toDto(any(Channel.class))).willReturn(dto);

    // When
    ChannelDto result = channelService.create(request);

    // Then
    assertNotNull(result);
    assertEquals(ChannelType.PRIVATE, result.type());
    assertEquals(1, result.participants().size());
    then(readStatusRepository).should().saveAll(any());
  }

  // FIND
  @Test
  void findChannel_success() {
    // Given
    UUID channelId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PUBLIC, "공지", "공지사항");
    List<UserDto> participants = List.of(dummyUserDto("user1"));
    ChannelDto dto = new ChannelDto(
        channelId,
        ChannelType.PUBLIC,
        "공지",
        "공지사항",
        participants,
        Instant.now()
    );

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(channelMapper.toDto(channel)).willReturn(dto);

    // When
    ChannelDto result = channelService.find(channelId);

    // Then
    assertNotNull(result);
    assertEquals("공지", result.name());
    assertEquals(ChannelType.PUBLIC, result.type());
  }

  @Test
  void findChannel_fail_notFound() {
    // Given
    UUID channelId = UUID.randomUUID();
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // When & Then
    assertThrows(ChannelNotFoundException.class, () -> channelService.find(channelId));
  }

  // FIND BY USER ID
  @Test
  void findAllByUserId_success() {
    // Given
    UUID userId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PUBLIC, "공지", "공지사항");
    List<UserDto> participants = List.of(dummyUserDto("user1"), dummyUserDto("user2"));
    ChannelDto dto = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PUBLIC,
        "공지",
        "공지사항",
        participants,
        Instant.now()
    );
    ReadStatus readStatus = new ReadStatus(mock(User.class), channel, Instant.now());

    given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(readStatus));
    given(channelRepository.findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), anyList()))
        .willReturn(List.of(channel));
    given(channelMapper.toDto(channel)).willReturn(dto);

    // When
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // Then
    assertEquals(1, result.size());
    assertEquals("공지", result.get(0).name());
    assertEquals(2, result.get(0).participants().size());
  }

  // UPDATE
  @Test
  void updatePublicChannel_success() {
    // Given
    UUID channelId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PUBLIC, "공지", "공지사항");
    List<UserDto> participants = List.of(dummyUserDto("user1"));
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("새이름", "새설명");
    ChannelDto dto = new ChannelDto(
        channelId,
        ChannelType.PUBLIC,
        "새이름",
        "새설명",
        participants,
        Instant.now()
    );

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(channelMapper.toDto(channel)).willReturn(dto);

    // When
    ChannelDto result = channelService.update(channelId, request);

    // Then
    assertNotNull(result);
    assertEquals("새이름", result.name());
    assertEquals("새설명", result.description());
  }

  @Test
  void updateChannel_fail_privateType() {
    // Given
    UUID channelId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("새이름", "새설명");

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

    // When & Then
    assertThrows(ChannelUpdateNotAllowedException.class,
        () -> channelService.update(channelId, request));
  }

  // DELETE
  @Test
  void deleteChannel_success() {
    // Given
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(true);

    // When
    channelService.delete(channelId);

    // Then
    then(messageRepository).should().deleteAllByChannelId(channelId);
    then(readStatusRepository).should().deleteAllByChannelId(channelId);
    then(channelRepository).should().deleteById(channelId);
  }

  @Test
  void deleteChannel_fail_notFound() {
    // Given
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(false);

    // When & Then
    assertThrows(ChannelNotFoundException.class, () -> channelService.delete(channelId));
  }
}


