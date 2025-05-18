package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelExistsException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateNotSupportedException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
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
@DisplayName("BasicChannelService Test")
class BasicChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private ReadStatusService readStatusService;
    @Mock
    private ReadStatusRepository readStatusRepository;
    @Mock
    private ChannelMapper channelMapper;

    @InjectMocks
    private BasicChannelService channelService;

    @Test
    @DisplayName("프라이빗 채널 생성 성공 테스트")
    void createPrivateChannel_success() {
        // given
        UUID userId = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        Channel newChannel = Channel.createPrivate();
        ChannelDto expectedDto = new ChannelDto(newChannel.getId(), null, null, null, null, null);

        given(channelRepository.save(any())).willReturn(newChannel);
        given(channelMapper.toDto(any())).willReturn(expectedDto);

        // when
        ChannelDto result = channelService.createPrivate(new ChannelCreatePrivateDto(List.of(userId, userId2)));

        // then
        assertThat(result).isEqualTo(expectedDto);
        then(readStatusService).should(times(2)).create(any());
    }

    @Test
    @DisplayName("퍼블릭 채널 생성 성공 테스트")
    void createPublicChannel_success() {
        // given
        ChannelCreatePublicDto dto = new ChannelCreatePublicDto("notice", "공지사항 채널");
        ChannelDto expectedDto = new ChannelDto(UUID.randomUUID(), null, null, null, null, null);

        given(channelRepository.existsByTypeAndName(ChannelType.PUBLIC, dto.name())).willReturn(false);
        given(channelMapper.toDto(any(Channel.class))).willReturn(expectedDto);

        // when
        ChannelDto result = channelService.createPublic(dto);

        // then
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("퍼블릭 채널 생성 실패 - 중복된 이름")
    void createPublicChannel_fail_whenDuplicate() {
        // given
        ChannelCreatePublicDto dto = new ChannelCreatePublicDto("notice", "공지사항 채널");
        given(channelRepository.existsByTypeAndName(ChannelType.PUBLIC, dto.name())).willReturn(true);

        // when / then
        assertThatThrownBy(() -> channelService.createPublic(dto))
                .isInstanceOf(ChannelExistsException.class);
    }

    @Test
    @DisplayName("채널 수정 성공 테스트 (PUBLIC)")
    void updateChannel_success() {
        // given
        UUID channelId = UUID.randomUUID();
        ChannelUpdateDto updateDto = new ChannelUpdateDto("new-name", "new-desc");
        Channel channel = Channel.createPublic("old", "desc");
        ChannelDto expectedDto = new ChannelDto(channel.getId(), null, null, null, null, null);

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(channelMapper.toDto(channel)).willReturn(expectedDto);

        // when
        ChannelDto result = channelService.update(channelId, updateDto);

        // then
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("채널 수정 실패 - 존재하지 않는 채널")
    void updateChannel_fail_whenNotFound() {
        // given
        UUID channelId = UUID.randomUUID();
        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> channelService.update(channelId, new ChannelUpdateDto("a", "b")))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @Test
    @DisplayName("채널 수정 실패 - PRIVATE 채널 수정 불가")
    void updateChannel_fail_whenPrivate() {
        // given
        UUID channelId = UUID.randomUUID();
        Channel channel = Channel.createPrivate();
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

        // when / then
        assertThatThrownBy(() -> channelService.update(channelId, new ChannelUpdateDto("a", "b")))
                .isInstanceOf(PrivateChannelUpdateNotSupportedException.class);
    }

    @Test
    @DisplayName("채널 삭제 성공 테스트")
    void deleteChannel_success() {
        // given
        UUID channelId = UUID.randomUUID();
        Channel channel = Channel.createPublic("test", "desc");
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

        // when
        channelService.delete(channelId);

        // then
        then(channelRepository).should().delete(channel);
    }

    @Test
    @DisplayName("채널 삭제 실패 - 존재하지 않는 채널")
    void deleteChannel_fail_whenNotFound() {
        // given
        UUID channelId = UUID.randomUUID();
        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> channelService.delete(channelId))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @Test
    @DisplayName("사용자 기준 채널 조회 성공 테스트")
    void findAllByUserId_success() {
        // given
        UUID userId = UUID.randomUUID();
        UUID privateChannelId = UUID.randomUUID();
        UUID publicChannelId = UUID.randomUUID();

        Channel privateChannel = Channel.createPrivate();
        Channel publicChannel = Channel.createPublic("공지사항", "내용");

        ReadStatus readStatus = mock(ReadStatus.class);
        Channel subscribedChannel = mock(Channel.class);
        given(subscribedChannel.getId()).willReturn(privateChannelId);
        given(readStatus.getChannel()).willReturn(subscribedChannel);
        given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(readStatus));
        given(channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of(privateChannelId)))
                .willReturn(List.of(publicChannel, privateChannel));

        ChannelDto publicDto = new ChannelDto(publicChannelId, "공지사항", "내용", ChannelType.PUBLIC,
                publicChannel.getCreatedAt(), List.of());
        ChannelDto privateDto = new ChannelDto(privateChannelId, null, null, ChannelType.PRIVATE,
                privateChannel.getCreatedAt(), List.of());

        given(channelMapper.toDto(publicChannel)).willReturn(publicDto);
        given(channelMapper.toDto(privateChannel)).willReturn(privateDto);

        // when
        List<ChannelDto> result = channelService.findAllByUserId(userId);

        // then
        assertThat(result.stream().map(ChannelDto::id))
                .containsExactlyInAnyOrder(privateDto.id(), publicDto.id());
    }
}