package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
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
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BasicChannelServiceTest {

  @InjectMocks
  private BasicChannelService channelService;

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

  @Test
  @DisplayName("Public 채널 생성 성공")
  void createPublicChannel() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("채널이름", "설명");
    channelService.create(request);
    then(channelRepository).should().save(any(Channel.class));
  }

  @Test
  @DisplayName("Private 채널 생성 성공")
  void createPrivateChannel() {
    List<UUID> participants = List.of(UUID.randomUUID());
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participants);
    given(userRepository.findAllById(participants)).willReturn(
        List.of(new User("user", "email", "pw", null)));
    channelService.create(request);
    then(channelRepository).should().save(any(Channel.class));
    then(readStatusRepository).should().saveAll(any());
  }

  @Test
  @DisplayName("채널 업데이트 실패 - private 채널")
  void updateFailPrivateChannel() {
    UUID id = UUID.randomUUID();
    Channel privateChannel = new Channel(ChannelType.PRIVATE, null, null);
    given(channelRepository.findById(id)).willReturn(Optional.of(privateChannel));
    assertThatThrownBy(() ->
        channelService.update(id, new PublicChannelUpdateRequest("new", "new")))
        .isInstanceOf(PrivateChannelUpdateException.class);
  }

  @Test
  @DisplayName("채널 삭제 실패 - 존재하지 않음")
  void deleteFailNotExist() {
    UUID id = UUID.randomUUID();
    given(channelRepository.existsById(id)).willReturn(false);
    assertThatThrownBy(() -> channelService.delete(id))
        .isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  @DisplayName("채널 삭제 성공")
  void deleteSuccess() {
    UUID id = UUID.randomUUID();
    given(channelRepository.existsById(id)).willReturn(true);
    channelService.delete(id);
    then(messageRepository).should().deleteAllByChannelId(id);
    then(readStatusRepository).should().deleteAllByChannelId(id);
    then(channelRepository).should().deleteById(id);
  }
}
