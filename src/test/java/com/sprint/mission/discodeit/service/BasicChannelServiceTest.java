package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.doThrow;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("BasicChannelService 단위 테스트")
class BasicChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private MessageRepository messageRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ChannelMapper channelMapper;
  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private BasicChannelService channelService;

  private PublicChannelCreateRequest publicChannelCreateRequest;
  private PrivateChannelCreateRequest privateChannelCreateRequest;
  private PublicChannelUpdateRequest channelUpdateRequest;

  private void setIdUsingReflection(Object entity, UUID id) {
    Class<?> clazz = entity.getClass();
    while (clazz != null) {
      try {
        Field f = clazz.getDeclaredField("id");
        f.setAccessible(true);
        f.set(entity, id);
        return;
      } catch (NoSuchFieldException ignored) {
        clazz = clazz.getSuperclass();
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
    throw new RuntimeException("Cannot find field 'id' on " + entity.getClass());
  }

  @BeforeEach
  void setUp() {
    publicChannelCreateRequest = new PublicChannelCreateRequest("pub-name", "pub-desc");
    privateChannelCreateRequest = new PrivateChannelCreateRequest(
        List.of(UUID.fromString("00000000-0000-0000-0000-000000000010")));
    channelUpdateRequest = new PublicChannelUpdateRequest("new-name", "new-desc");

    lenient().when(userMapper.toResponse(any(User.class)))
        .thenAnswer(inv -> {
          User u = inv.getArgument(0);
          return new UserResponse(u.getId(), u.getUsername(), u.getEmail(), null, true);
        });

    lenient().when(channelMapper.toResponse(any(Channel.class), anyList(), nullable(Instant.class)))
        .thenAnswer(inv -> {
          Channel ch = inv.getArgument(0);
          @SuppressWarnings("unchecked")
          List<UserResponse> parts = inv.getArgument(1);
          Instant last = inv.getArgument(2);
          return new ChannelResponse(
              ch.getId(), ch.getType(), ch.getName(), ch.getDescription(), parts, last);
        });
  }

  @DisplayName("공개 채널을 생성할 수 있다")
  @Test
  void create_public_channel_success() {
    // given
    UUID newId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    given(channelRepository.save(any(Channel.class)))
        .willAnswer(inv -> {
          Channel ch = inv.getArgument(0);
          setIdUsingReflection(ch, newId);
          return ch;
        });

    given(readStatusRepository.findUserIdsByChannel(any(Channel.class)))
        .willReturn(Collections.emptyList());
    given(messageRepository.findTop1ByChannelOrderByCreatedAtDesc(any(Channel.class)))
        .willReturn(Optional.empty());
    given(userRepository.findAllById(anyList()))
        .willReturn(Collections.emptyList());

    // when
    ChannelResponse response = channelService.createPublicChannel(publicChannelCreateRequest);

    // then
    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo(newId);
    assertThat(response.type()).isEqualTo(ChannelType.PUBLIC);
    assertThat(response.name()).isEqualTo("pub-name");
    assertThat(response.description()).isEqualTo("pub-desc");
    assertThat(response.participants()).isEmpty();
    assertThat(response.lastMessageAt()).isNull();
  }

  @DisplayName("비공개 채널을 생성할 수 있다")
  @Test
  void create_private_channel_success() {
    // given
    UUID newId = UUID.fromString("00000000-0000-0000-0000-000000000002");
    UUID participantId = privateChannelCreateRequest.participantIds().get(0);

    User participant = new User("u1", "u1@example.com", "pw", null);
    setIdUsingReflection(participant, participantId);

    given(userRepository.findAllById(privateChannelCreateRequest.participantIds()))
        .willReturn(List.of(participant));
    given(channelRepository.save(any(Channel.class)))
        .willAnswer(inv -> {
          Channel ch = inv.getArgument(0);
          setIdUsingReflection(ch, newId);
          return ch;
        });

    given(readStatusRepository.findUserIdsByChannel(any(Channel.class)))
        .willReturn(List.of(participantId));
    given(messageRepository.findTop1ByChannelOrderByCreatedAtDesc(any(Channel.class)))
        .willReturn(Optional.empty());

    // when
    ChannelResponse response = channelService.createPrivateChannel(privateChannelCreateRequest);

    // then
    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo(newId);
    assertThat(response.type()).isEqualTo(ChannelType.PRIVATE);
    assertThat(response.participants()).hasSize(1)
        .extracting(UserResponse::id)
        .containsExactly(participantId);
  }

  @DisplayName("채널 조회 성공")
  @Test
  void find_channel_success() {
    // given
    UUID id = UUID.fromString("00000000-0000-0000-0000-000000000003");
    Channel existing = new Channel(ChannelType.PUBLIC, "n", "d");
    setIdUsingReflection(existing, id);

    given(channelRepository.findById(eq(id)))
        .willReturn(Optional.of(existing));
    given(readStatusRepository.findUserIdsByChannel(existing))
        .willReturn(Collections.emptyList());
    given(messageRepository.findTop1ByChannelOrderByCreatedAtDesc(existing))
        .willReturn(Optional.empty());

    // when
    ChannelResponse response = channelService.find(id);

    // then
    assertThat(response.id()).isEqualTo(id);
  }

  @DisplayName("없는 채널 조회 시 예외를 던진다.")
  @Test
  void find_channel_not_found() {
    // given
    given(channelRepository.findById(any(UUID.class)))
        .willReturn(Optional.empty());

    // when/then
    assertThatThrownBy(() -> channelService.find(UUID.randomUUID()))
        .isInstanceOf(ChannelNotFoundException.class);
  }

  @DisplayName("공개 채널을 수정할 수 있다")
  @Test
  void update_public_channel_success() {
    // given
    UUID id = UUID.fromString("00000000-0000-0000-0000-000000000004");
    Channel existing = new Channel(ChannelType.PUBLIC, "old", "oldDesc");
    setIdUsingReflection(existing, id);

    given(channelRepository.findById(eq(id)))
        .willReturn(Optional.of(existing));
    given(channelRepository.save(any(Channel.class)))
        .willReturn(existing);
    given(readStatusRepository.findUserIdsByChannel(existing))
        .willReturn(Collections.emptyList());
    given(messageRepository.findTop1ByChannelOrderByCreatedAtDesc(existing))
        .willReturn(Optional.empty());

    // when
    ChannelResponse response = channelService.update(id, channelUpdateRequest);

    // then
    assertThat(response.name()).isEqualTo("new-name");
    assertThat(response.description()).isEqualTo("new-desc");
  }

  @DisplayName("비공개 채널 수정 시 예외를 던진다.")
  @Test
  void update_private_channel_should_throw() {
    // given
    UUID id = UUID.fromString("00000000-0000-0000-0000-000000000005");
    Channel existing = new Channel(ChannelType.PRIVATE);
    setIdUsingReflection(existing, id);

    given(channelRepository.findById(eq(id)))
        .willReturn(Optional.of(existing));

    // when/then
    assertThatThrownBy(() -> channelService.update(id, channelUpdateRequest))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("유효한 ID로 채널 삭제 성공")
  @Test
  void delete_channel_success() {
    // given
    UUID id = UUID.fromString("00000000-0000-0000-0000-000000000006");
    given(channelRepository.existsById(eq(id))).willReturn(true);

    // when/then
    assertThatCode(() -> channelService.delete(id))
        .doesNotThrowAnyException();
  }

  @DisplayName("없는 채널 삭제 시 ChannelNotFoundException")
  @Test
  void delete_channel_not_found_should_throw() {
    // given
    given(channelRepository.existsById(any(UUID.class))).willReturn(false);

    // when/then
    assertThatThrownBy(() -> channelService.delete(UUID.randomUUID()))
        .isInstanceOf(ChannelNotFoundException.class);
  }
}
