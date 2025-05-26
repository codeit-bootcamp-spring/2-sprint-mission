package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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

  //create(PUBLIC)
  @Test
  void givenPublicRequest_whenCreate_thenReturnsDto() {
    var req = new PublicChannelCreateRequest("Announcements", "All users");
    var channel = new Channel(ChannelType.PUBLIC, "Announcements", "All users");
    var expectedDto = new ChannelDto(
        channel.getId(),
        ChannelType.PUBLIC,
        "Announcements",
        "All users",
        Collections.emptyList(),
        null
    );

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(expectedDto);

    ChannelDto result = channelService.create(req);

    then(channelRepository).should().save(any(Channel.class));
    then(channelMapper).should().toDto(any(Channel.class));
    assertThat(result).isEqualTo(expectedDto);
  }

  //create(PRIVATE)
  @Test
  void givenPrivateRequest_whenCreate_thenReturnsDtoAndSavesStatuses() {
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();
    var req = new PrivateChannelCreateRequest(List.of(userId1, userId2));

    var channel = new Channel(ChannelType.PRIVATE, null, null);
    var user1 = new User("u1", "u1@example.com", "pw", null);
    var user2 = new User("u2", "u2@example.com", "pw", null);

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(userRepository.findAllById(req.participantIds()))
        .willReturn(List.of(user1, user2));

    var expectedDto = new ChannelDto(
        channel.getId(),
        ChannelType.PRIVATE,
        null,
        null,
        Collections.emptyList(),
        null
    );
    given(channelMapper.toDto(any(Channel.class))).willReturn(expectedDto);

    ChannelDto result = channelService.create(req);

    then(channelRepository).should().save(any(Channel.class));
    then(userRepository).should().findAllById(req.participantIds());
    then(readStatusRepository).should().saveAll(anyList());
    then(channelMapper).should().toDto(any(Channel.class));
    assertThat(result).isEqualTo(expectedDto);
  }

  //find
  @Test
  void givenExistingId_whenFind_thenReturnsDto() {
    UUID id = UUID.randomUUID();
    var channel = new Channel(ChannelType.PUBLIC, "Name", "Desc");

    var expectedDto = new ChannelDto(
        id,
        ChannelType.PUBLIC,
        "Name",
        "Desc",
        Collections.emptyList(),
        null
    );
    given(channelRepository.findById(id)).willReturn(Optional.of(channel));
    given(channelMapper.toDto(any(Channel.class))).willReturn(expectedDto);

    ChannelDto result = channelService.find(id);

    then(channelRepository).should().findById(id);
    then(channelMapper).should().toDto(any(Channel.class));
    assertThat(result).isEqualTo(expectedDto);
  }

  @Test
  void givenUnknownId_whenFind_thenThrowsNotFound() {
    UUID id = UUID.randomUUID();
    given(channelRepository.findById(id)).willReturn(Optional.empty());

    assertThatThrownBy(() -> channelService.find(id))
        .isInstanceOf(ChannelNotFoundException.class)
        .extracting("details", InstanceOfAssertFactories.MAP)
        .containsEntry("channelId", id);

    then(channelRepository).should().findById(id);
  }

  @Test
  void givenSubscriptions_whenFindAllByUserId_thenReturnsDtos() {
    UUID userId = UUID.randomUUID();
    var ch1 = new Channel(ChannelType.PUBLIC, "A", "a");
    var ch2 = new Channel(ChannelType.PUBLIC, "B", "b");
    var rs1 = new ReadStatus(new User("x", "x@x", "pw", null), ch1, Instant.now());
    var rs2 = new ReadStatus(new User("y", "y@y", "pw", null), ch2, Instant.now());
    var expectedDto1 = new ChannelDto(ch1.getId(), ChannelType.PUBLIC, "A", "a",
        Collections.emptyList(), null);
    var expectedDto2 = new ChannelDto(ch2.getId(), ChannelType.PUBLIC, "B", "b",
        Collections.emptyList(), null);

    given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(rs1, rs2));
    given(channelRepository.findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), anyList()))
        .willReturn(List.of(ch1, ch2));
    given(channelMapper.toDto(any(Channel.class)))
        .willReturn(expectedDto1)
        .willReturn(expectedDto2);

    var results = channelService.findAllByUserId(userId);

    then(readStatusRepository).should().findAllByUserId(userId);
    then(channelRepository).should().findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), anyList());
    assertThat(results).containsExactly(expectedDto1, expectedDto2);
  }

  //update
  @Test
  void givenPublicChannel_whenUpdate_thenReturnsDto() {
    UUID id = UUID.randomUUID();
    var existing = new Channel(ChannelType.PUBLIC, "old", "d");
    var req = new PublicChannelUpdateRequest("new", "nd");

    var expectedDto = new ChannelDto(id, ChannelType.PUBLIC, "new", "nd", Collections.emptyList(),
        null);
    given(channelRepository.findById(id)).willReturn(Optional.of(existing));
    given(channelMapper.toDto(any(Channel.class))).willReturn(expectedDto);

    ChannelDto result = channelService.update(id, req);

    then(channelRepository).should().findById(id);
    then(channelMapper).should().toDto(any(Channel.class));
    assertThat(result).isEqualTo(expectedDto);
  }

  @Test
  void givenPrivateChannel_whenUpdate_thenThrowsException() {
    UUID id = UUID.randomUUID();
    var existing = new Channel(ChannelType.PRIVATE, null, null);
    var req = new PublicChannelUpdateRequest("x", "y");

    given(channelRepository.findById(id)).willReturn(Optional.of(existing));

    assertThatThrownBy(() -> channelService.update(id, req))
        .isInstanceOf(PrivateChannelUpdateException.class);

    then(channelRepository).should().findById(id);
    then(channelMapper).should(never()).toDto(any());
  }

  @Test
  void givenExistingId_whenDelete_thenDeletesEverything() {
    UUID id = UUID.randomUUID();
    given(channelRepository.existsById(id)).willReturn(true);

    channelService.delete(id);

    then(channelRepository).should().existsById(id);
    then(messageRepository).should().deleteAllByChannelId(id);
    then(readStatusRepository).should().deleteAllByChannelId(id);
    then(channelRepository).should().deleteById(id);
  }

  @Test
  void givenUnknownId_whenDelete_thenThrowsNotFound() {
    UUID id = UUID.randomUUID();
    given(channelRepository.existsById(id)).willReturn(false);
    
    assertThatThrownBy(() -> channelService.delete(id))
        .isInstanceOf(ChannelNotFoundException.class)
        .extracting("details", InstanceOfAssertFactories.MAP)
        .containsEntry("channelId", id);

    then(channelRepository).should().existsById(id);
    then(messageRepository).should(never()).deleteAllByChannelId(any());
    then(readStatusRepository).should(never()).deleteAllByChannelId(any());
    then(channelRepository).should(never()).deleteById(any());
  }
}
