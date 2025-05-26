package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
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
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BasicChannelServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ReadStatusRepository readStatusRepository;
    @Mock
    private ChannelMapper channelMapper;

    @InjectMocks
    private BasicChannelService channelService;

    private UUID channelId;
    private UUID userId;
    private String channelName;
    private String channelDescription;
    private Channel channel;
    private ChannelDto channelDto;
    private User user;

    @BeforeEach
    void setUp() {
        channelId = UUID.randomUUID();
        userId = UUID.randomUUID();
        channelName = "testChannel";
        channelDescription = "testDescription";

        channel = new Channel(ChannelType.PUBLIC, channelName, channelDescription);
        ReflectionTestUtils.setField(channel, "id", channelId);
        channelDto = new ChannelDto(channelId, ChannelType.PUBLIC, channelName, channelDescription,
                List.of(), Instant.now());
        user = new User("testUser", "test@example.com", "password", null);
    }

    @Test
    @DisplayName("정상적인 공개 Channel 생성 성공")
    void createPublicChannel_WithValidInput_Success() {
        //given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest(channelName, channelDescription);
        given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

        //when
        ChannelDto result = channelService.create(request);

        //then
        assertThat(result).isEqualTo(channelDto);
        verify(channelRepository).save(any(Channel.class));
    }

    @Test
    @DisplayName("정상적인 Private 채널 생성 성공")
    void createPrivateChannel_WithValidInput_Success() {
        //given
        List<UUID> participantIds = List.of(userId);
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);
        given(userRepository.findAllById(eq(participantIds))).willReturn(List.of(user));
        given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

        //when
        ChannelDto result = channelService.create(request);

        //then
        assertThat(result).isEqualTo(channelDto);
        verify(channelRepository).save(any(Channel.class));
        verify(readStatusRepository).<ReadStatus>saveAll(anyList());
    }

    @Test
    @DisplayName("채널 조회 성공")
    void findChannel_Success() {
        //given
        given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(channel));
        given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

        //when
        ChannelDto result = channelService.searchChannel(channelId);

        //then
        assertThat(result).isEqualTo(channelDto);
    }

    @Test
    @DisplayName("존재하지 않는 채널 조회 시 실패")
    void findChannel_WithInvalidChannelId_ThrowException() {
        //given
        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> channelService.searchChannel(channelId))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @Test
    @DisplayName("사용자별 채널 목록 조회 성공")
    void findAllChannels_ByUserId_Success() {
        //given
        List<ReadStatus> readStatuses = List.of(new ReadStatus(user, channel, Instant.now()));
        given(readStatusRepository.findAllByUserId(eq(userId))).willReturn(readStatuses);
        given(channelRepository.findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), eq(List.of(channel.getId()))))
                .willReturn(List.of(channel));
        given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

        //when
        List<ChannelDto> result = channelService.findAllChannelsByUserId(userId);

        //then
        assertThat(result).containsExactly(channelDto);
    }

    @Test
    @DisplayName("정상적인 Channel 업데이트 테스트")
    void updateChannel_WithValidInput_Success() {
        //given
        String newName = "newChannelName";
        String newDescription = "newDescription";
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(newName, newDescription);

        given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(channel));
        given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

        //when
        ChannelDto result = channelService.updateChannel(channelId, request);

        //then
        assertThat(result).isEqualTo(channelDto);
    }

    @Test
    @DisplayName("존재하지 않는 channelId로 업데이트 시 예외 발생")
    void updateChannel_WithInvalidChannelId_ThrowException() {
        //given
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName", "newDescription");
        given(channelRepository.findById(eq(channelId))).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> channelService.updateChannel(channelId, request))
                .isInstanceOf(ChannelNotFoundException.class);
    }

    @Test
    @DisplayName("Private 채널을 수정하려 할 때 예외 발생")
    void update_PrivateChannel_ThrowException() {
        //given
        Channel privateChannel = new Channel(ChannelType.PRIVATE, null, null);
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName", "newDescription");
        given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(privateChannel));

        //when & then
        assertThatThrownBy(() -> channelService.updateChannel(channelId, request))
                .isInstanceOf(PrivateChannelUpdateException.class);
    }

    @Test
    @DisplayName("정상적인 Channel 삭제 테스트")
    void deleteChannel_WithValidInput_Success() {
        //given
        given(channelRepository.existsById(channelId)).willReturn(true);

        //when
        channelService.deleteChannel(channelId);

        //then
        verify(messageRepository).deleteAllByChannelId(channelId);
        verify(readStatusRepository).deleteAllByChannelId(channelId);
        verify(channelRepository).deleteById(channelId);
    }

    @Test
    @DisplayName("존재하지 않는 channelId로 삭제 시 예외 발생")
    void deleteChannel_WithInvalidChannelId_ThrowException() {
        //given
        given(channelRepository.existsById(eq(channelId))).willReturn(false);

        //when & then
        assertThatThrownBy(() -> channelService.deleteChannel(channelId))
                .isInstanceOf(ChannelNotFoundException.class);
    }
}