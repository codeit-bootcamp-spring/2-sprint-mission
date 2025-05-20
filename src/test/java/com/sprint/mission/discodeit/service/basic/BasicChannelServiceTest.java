package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private MessageRepository messageRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ChannelMapper channelMapper;
  @InjectMocks
  private BasicChannelService channelService;

  @Test
  void create_success_public_channel() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("test", "test");
    UUID uuid = UUID.randomUUID();

    given(channelMapper.toDto(any())).willReturn(
        new ChannelDto(uuid, ChannelType.PUBLIC, request.name(), request.description(), List.of(),
            Instant.now())
    );

    ChannelDto result = channelService.create(request);

    then(channelRepository).should(times(1)).save(any());

    assertNotNull(result);
    assertEquals(request.name(), result.name());
    assertEquals(request.description(), result.description());
    assertEquals(ChannelType.PUBLIC, result.type());
  }

  @Test
  void create_success_private_channel() {
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(Collections.emptyList());
    UUID uuid = UUID.randomUUID();

    ChannelDto channelDto = new ChannelDto(uuid, ChannelType.PRIVATE, null, null, null,
        Instant.now());

    given(channelMapper.toDto(any())).willReturn(channelDto);

    ChannelDto result = channelService.create(request);

    then(channelRepository).should(times(1)).save(any());

    assertNotNull(result);
    assertEquals(ChannelType.PRIVATE, result.type());
  }

  @Test
  void delete_success() {
    UUID uuid = UUID.randomUUID();

    given(channelRepository.existsById(uuid)).willReturn(true);

    channelService.delete(uuid);

    then(channelRepository).should().existsById(uuid);
    then(channelRepository).should().deleteById(uuid);
  }

  @Test
  void delete_fail() {
    UUID uuid = UUID.randomUUID();

    given(channelRepository.existsById(uuid)).willReturn(false);

    ChannelNotFoundException exception = assertThrows(ChannelNotFoundException.class, () -> {
      channelService.delete(uuid);
    });

    then(channelRepository).should(never()).deleteById(uuid);

    assertTrue(exception.getMessage().contains("채널을 찾을 수 없습니다."));
  }
}