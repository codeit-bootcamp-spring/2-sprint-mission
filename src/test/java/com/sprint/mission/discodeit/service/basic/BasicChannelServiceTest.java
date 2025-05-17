package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.service.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.service.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.service.channel.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.service.channel.PublicChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class BasicChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private MessageRepository messageRepository;
  @Mock
  private ChannelMapper channelMapper;
  @InjectMocks
  private BasicChannelService basicChannelService;

  private UUID id;
  private ChannelType type;
  private String name;
  private String description;
  private Channel channel;

  @BeforeEach
  public void setUp() {
    id = UUID.randomUUID();
    type = ChannelType.PUBLIC;
    name = "channelName";
    description = "channelDescription";

    channel = new Channel(type, name, description);
    ReflectionTestUtils.setField(channel, "id", id);
  }

  @Test
  public void createPrivateChannel_success() {
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();
    List<UUID> participants = List.of(userId1, userId2);
    PrivateChannelRequest request = new PrivateChannelRequest(participants);

    User user1 = new User("user1", "user1@example.com", "password", null);
    User user2 = new User("user2", "user2@example.com", "password", null);
    List<User> users = List.of(user1, user2);

    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    ReflectionTestUtils.setField(channel, "id", UUID.randomUUID());

    ChannelDto dto = channelMapper.toDto(channel);

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(userRepository.findAllById(participants)).willReturn(users);
    given(readStatusRepository.saveAll(anyList())).willReturn(Collections.emptyList());

    ChannelDto res = basicChannelService.create(request);

    assertThat(res).isEqualTo(dto);
    then(channelRepository).should().save(any(Channel.class));
    then(userRepository).should().findAllById(participants);
    then(readStatusRepository).should().saveAll(anyList());
    then(channelMapper).should().toDto(channel);
  }

  @Test
  public void createPublicChannel_success() {
    PublicChannelRequest request = new PublicChannelRequest(name, description);
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);
    ReflectionTestUtils.setField(channel, "id", UUID.randomUUID());
    ChannelDto dto = channelMapper.toDto(channel);

    given(channelRepository.save(any(Channel.class))).willReturn(channel);

    ChannelDto res = basicChannelService.create(request);

    assertThat(res).isEqualTo(dto);
    then(channelRepository).should().save(any(Channel.class));
    then(channelMapper).should().toDto(channel);
  }

  @Test
  public void updatePrivateChannel_throwPrivateChannelUpdateException() {
    UUID channelId = UUID.randomUUID();
    Channel privateChannel = new Channel(ChannelType.PRIVATE, null, null);
    ReflectionTestUtils.setField(privateChannel, "id", channelId);
    given(channelRepository.findById(channelId)).willReturn(Optional.of(privateChannel));
    ChannelUpdateRequest request = new ChannelUpdateRequest(name, description);

    assertThatThrownBy(() -> basicChannelService.update(channelId, request))
        .isInstanceOf(PrivateChannelUpdateException.class);
  }

  @Test
  public void updatePublicChannel_NotFound_throwChannelNotFoundException() {
    ChannelUpdateRequest request = new ChannelUpdateRequest(name, description);
    given(channelRepository.findById(id)).willReturn(Optional.empty());

    assertThatThrownBy(() -> basicChannelService.update(id, request))
        .isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  public void updatePublicChannel_success() {
    ChannelUpdateRequest request = new ChannelUpdateRequest("newName", "newDescription");
    given(channelRepository.findById(id)).willReturn(Optional.of(channel));
    channel.update("newName", "newDescription");
    ChannelDto dto = new ChannelDto(id, type, "newName", "newDescription", null, null);

    given(channelMapper.toDto(any(Channel.class))).willReturn(dto);

    ChannelDto res = basicChannelService.update(id, request);
    assertThat(res).isEqualTo(dto);
    then(channelRepository).should().findById(id);
  }

  @Test
  public void deleteChannel_NotFound_throwChannelNotFoundException() {
    given(channelRepository.existsById(id)).willReturn(false);

    assertThatThrownBy(() -> basicChannelService.delete(id))
        .isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  public void deleteChannel_success() {
    given(channelRepository.existsById(id)).willReturn(true);

    basicChannelService.delete(id);

    then(channelRepository).should().existsById(id);
    then(messageRepository).should().deleteAllByChannelId(id);
    then(readStatusRepository).should().deleteAllByChannelId(id);
    then(channelRepository).should().deleteById(id);
  }

  @Test
  public void findAllByUserId_success() {
    UUID userId = UUID.randomUUID();
    List<UUID> joinedIds = List.of(UUID.randomUUID(), UUID.randomUUID());
    given(readStatusRepository.findAllChannelIdByUserId(userId))
        .willReturn(joinedIds);

    Channel publicChannel = new Channel(ChannelType.PUBLIC, "public", "all users");
    ReflectionTestUtils.setField(publicChannel, "id", UUID.randomUUID());
    Channel joinedChannel = new Channel(ChannelType.PRIVATE, "joined", "only joined");
    ReflectionTestUtils.setField(joinedChannel, "id", joinedIds.get(0));

    List<Channel> channels = List.of(publicChannel, joinedChannel);
    given(channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, joinedIds))
        .willReturn(channels);

    ChannelDto dto1 = new ChannelDto(
        publicChannel.getId(), ChannelType.PUBLIC,
        publicChannel.getName(), publicChannel.getDescription(),
        null, null);
    ChannelDto dto2 = new ChannelDto(
        joinedChannel.getId(), ChannelType.PRIVATE,
        joinedChannel.getName(), joinedChannel.getDescription(),
        null, null);
    List<ChannelDto> expected = List.of(dto1, dto2);
    given(channelMapper.toDtoList(channels))
        .willReturn(expected);

    List<ChannelDto> result = basicChannelService.findAllByUserId(userId);

    assertThat(result).isEqualTo(expected);

    then(readStatusRepository).should().findAllChannelIdByUserId(userId);
    then(channelRepository).should().findAllByTypeOrIdIn(ChannelType.PUBLIC, joinedIds);
    then(channelMapper).should().toDtoList(channels);
  }

}
