package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
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
  @DisplayName("공개 채널 생성 성공")
  void createPublicChannel_success() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("공지방", "공지사항 모음");
    Channel channel = new Channel(ChannelType.PUBLIC, "공지방", "공지사항 모음");
    ChannelDto channelDto = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PUBLIC,
        "공지방",
        "공지사항 모음",
        List.of(),
        Instant.now()
    );

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    ChannelDto result = channelService.create(request);

    assertThat(result.name()).isEqualTo("공지방");
    assertThat(result.description()).isEqualTo("공지사항 모음");
    assertThat(result.type()).isEqualTo(ChannelType.PUBLIC);
    assertThat(result.participants()).isEmpty();
  }

  @Test
  @DisplayName("비공개 채널 생성 성공")
  void createPrivateChannel_success() {
    List<UUID> participantsIds = List.of(UUID.randomUUID(), UUID.randomUUID());
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantsIds);
    Channel privateChannel = new Channel(ChannelType.PRIVATE, null, null);
    ChannelDto channelDto = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PRIVATE,
        null,
        null,
        List.of(),
        Instant.now()
    );

    given(channelRepository.save(any(Channel.class))).willReturn(privateChannel);
    given(userRepository.findAllById(participantsIds)).willReturn(List.of());
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    ChannelDto result = channelService.create(request);

    assertThat(result.type()).isEqualTo(ChannelType.PRIVATE);
    assertThat(result.name()).isNull();
    assertThat(result.description()).isNull();
  }

  @Test
  @DisplayName("공개 채널 수정 성공")
  void updatePublicChannel_success() {
    UUID channelId = UUID.randomUUID();
    Channel existingChannel = new Channel(ChannelType.PUBLIC, "OldName", "OldDesc");
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("NewName", "NewDesc");
    ChannelDto updatedDto = new ChannelDto(
        channelId, ChannelType.PUBLIC, "NewName", "NewDesc", List.of(), Instant.now()
    );

    given(channelRepository.findById(channelId)).willReturn(Optional.of(existingChannel));
    given(channelMapper.toDto(any(Channel.class))).willReturn(updatedDto);

    ChannelDto result = channelService.update(channelId, request);

    assertThat(result.name()).isEqualTo("NewName");
    assertThat(result.description()).isEqualTo("NewDesc");
    assertThat(result.type()).isEqualTo(ChannelType.PUBLIC);
  }

  @Test
  @DisplayName("채널 삭제 성공")
  void deleteChannel_success() {
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(true);
    channelService.delete(channelId);
    verify(messageRepository).deleteAllByChannelId(channelId);
    verify(readStatusRepository).deleteAllByChannelId(channelId);
    verify(channelRepository).deleteById(channelId);
  }

  @Test
  @DisplayName("존재하지 않는 채널 삭제 시 예외 발생")
  void deleteChannel_notFound_fail() {
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(false);
    assertThatThrownBy(() -> channelService.delete(channelId))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("not found");

    verify(channelRepository, never()).deleteById(channelId);
  }

  @Test
  @DisplayName("사용자 ID로 채널 목록 조회 성공")
  void findAllByUserId_success() {
    UUID userId = UUID.randomUUID();
    UUID channelId1 = UUID.randomUUID();
    UUID channelId2 = UUID.randomUUID();

    Channel publicChannel = new Channel(ChannelType.PUBLIC, "공지", "공지사항 모음");
    Channel privateChannel = new Channel(ChannelType.PRIVATE, null, null);

    privateChannel.setIdForTest(channelId1);
    publicChannel.setIdForTest(channelId2);

    ReadStatus readStatus = mock(ReadStatus.class);
    Channel privateRef = mock(Channel.class);
    given(readStatus.getChannel()).willReturn(privateRef);
    given(privateRef.getId()).willReturn(channelId1);

    given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(readStatus));
    given(channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of(channelId1)))
        .willReturn(List.of(publicChannel, privateChannel));

    ChannelDto publicDto = new ChannelDto(channelId2, ChannelType.PUBLIC, "공지", "공지사항 모음", List.of(), Instant.now());
    ChannelDto privateDto = new ChannelDto(channelId1, ChannelType.PRIVATE, null, null, List.of(), Instant.now());

    given(channelMapper.toDto(publicChannel)).willReturn(publicDto);
    given(channelMapper.toDto(privateChannel)).willReturn(privateDto);

    List<ChannelDto> result = channelService.findAllByUserId(userId);

    assertThat(result).hasSize(2);
    assertThat(result).extracting("type").containsExactlyInAnyOrder(ChannelType.PUBLIC, ChannelType.PRIVATE);
  }
}
