package com.sprint.mission.discodeit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

  @InjectMocks
  private BasicChannelService channelService;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private ReadStatusRepository readStatusRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private ChannelMapper channelMapper;

  @Test
  void createPublicChannel_success() {
    // given
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("공개방", "설명");
    Channel channel = new Channel(ChannelType.PUBLIC, "공개방", "설명");

    given(channelRepository.save(any())).willReturn(channel);
    given(channelMapper.toDto(any())).willReturn(
        new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "설명",
            "..", userRepository.findAll()
            .stream()
            .map(users -> {
              return userMapper.toDto(users);
            })
            .toList(),
            Instant.now()));

    // when
    ChannelDto result = channelService.create(request);

    // then
    then(channelRepository).should().save(any());
    assertEquals("공개방", result.name());
  }

  @Test
  void updatePrivateChannel_shouldFail() {
    // given
    UUID id = UUID.randomUUID();
    Channel privateChannel = new Channel(ChannelType.PRIVATE, null, null);
    given(channelRepository.findById(id)).willReturn(Optional.of(privateChannel));

    // when + then
    assertThrows(PrivateChannelUpdateException.class, () -> {
      channelService.update(id, new PublicChannelUpdateRequest("new", "desc"));
    });
  }
}
