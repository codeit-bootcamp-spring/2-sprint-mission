package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChannelMapper channelMapper;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private BasicChannelService channelService;

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_EMAIL = "test@naver.com";
    private static final String TEST_PASSWORD = "123";
    private static final String TEST_USERNAME2 = "secondUser";
    private static final String TEST_EMAIL2 = "second@naver.com";
    private static final String TEST_PASSWORD2 = "123";
    private static final String PUBLIC_CHANNEL_NAME = "Public Channel";
    private static final String PUBLIC_CHANNEL_DESC = "Public Channel Description";
    private static final String PRIVATE_CHANNEL_NAME = "Private Test Channel";
    private static final String UPDATED_CHANNEL_NAME = "Updated Channel Name";
    private static final String UPDATED_CHANNEL_DESC = "Updated Description";

    private User testUser;
    private User secondUser;
    private Channel publicChannel;
    private Channel privateChannelEntity;
    private PublicChannelCreateRequest publicChannelCreateRequest;
    private PrivateChannelCreateRequest privateChannelCreateRequest;
    private PublicChannelUpdateRequest publicChannelUpdateRequest;
    private ChannelDto mockPublicChannelDto;
    private ChannelDto mockPrivateChannelDto;

    private UUID testUserId;
    private UUID secondUserId;
    private UUID publicChannelId;
    private UUID privateChannelId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        secondUserId = UUID.randomUUID();
        publicChannelId = UUID.randomUUID();
        privateChannelId = UUID.randomUUID();

        testUser = mock(User.class);
        secondUser = mock(User.class);

        when(testUser.getId()).thenReturn(testUserId);
        when(testUser.getUsername()).thenReturn(TEST_USERNAME);
        when(testUser.getEmail()).thenReturn(TEST_EMAIL);

        when(secondUser.getId()).thenReturn(secondUserId);
        when(secondUser.getUsername()).thenReturn(TEST_USERNAME2);
        when(secondUser.getEmail()).thenReturn(TEST_EMAIL2);

        publicChannel = new Channel(ChannelType.PUBLIC, PUBLIC_CHANNEL_NAME, PUBLIC_CHANNEL_DESC);

        privateChannelEntity = mock(Channel.class);
        when(privateChannelEntity.getId()).thenReturn(privateChannelId);

        publicChannelCreateRequest = new PublicChannelCreateRequest(PUBLIC_CHANNEL_NAME,
            PUBLIC_CHANNEL_DESC);
        privateChannelCreateRequest = new PrivateChannelCreateRequest(
            Arrays.asList(testUserId, secondUserId), PRIVATE_CHANNEL_NAME);
        publicChannelUpdateRequest = new PublicChannelUpdateRequest(UPDATED_CHANNEL_NAME,
            UPDATED_CHANNEL_DESC);

        mockPublicChannelDto = new ChannelDto(publicChannelId, ChannelType.PUBLIC,
            PUBLIC_CHANNEL_NAME,
            PUBLIC_CHANNEL_DESC, Collections.emptySet(), Instant.now());
        UserDto testUserDto = new UserDto(testUserId, TEST_USERNAME, TEST_EMAIL, null, true);
        UserDto secondUserDto = new UserDto(secondUserId, TEST_USERNAME2, TEST_EMAIL2, null, true);
        mockPrivateChannelDto = new ChannelDto(privateChannelId, ChannelType.PRIVATE,
            PRIVATE_CHANNEL_NAME, null, Set.of(testUserDto, secondUserDto), Instant.now());
    }

    @Test
    @DisplayName("공개 채널 생성 성공")
    void createPublicChannel_Success() {
        Channel savedChannel = new Channel(ChannelType.PUBLIC, publicChannelCreateRequest.name(),
            publicChannelCreateRequest.description());
        given(channelRepository.existsByName(publicChannelCreateRequest.name())).willReturn(false);
        given(channelRepository.save(any(Channel.class))).willReturn(savedChannel);
        given(channelMapper.toDto(savedChannel)).willReturn(mockPublicChannelDto);

        ChannelDto createdChannel = channelService.create(publicChannelCreateRequest);

        assertThat(createdChannel).isNotNull();
        assertThat(createdChannel.name()).isEqualTo(publicChannelCreateRequest.name());
        assertThat(createdChannel.type()).isEqualTo(ChannelType.PUBLIC);
        verify(channelRepository).save(any(Channel.class));
    }

    @Test
    @DisplayName("비공개 채널 생성 성공")
    void createPrivateChannel_Success() {
        Channel newPrivateChannel = new Channel(ChannelType.PRIVATE, null, null);

        given(userRepository.existsById(testUserId)).willReturn(true);
        given(userRepository.existsById(secondUserId)).willReturn(true);

        given(userRepository.findAllById(privateChannelCreateRequest.participantIds()))
            .willReturn(Arrays.asList(testUser, secondUser));

        given(channelRepository.save(any(Channel.class))).willReturn(newPrivateChannel);
        given(readStatusRepository.saveAll(anyList())).willReturn(Collections.emptyList());
        given(channelMapper.toDto(any(Channel.class))).willReturn(mockPrivateChannelDto);

        ChannelDto createdChannel = channelService.create(privateChannelCreateRequest);

        assertThat(createdChannel).isNotNull();
        assertThat(createdChannel.type()).isEqualTo(ChannelType.PRIVATE);
        assertThat(createdChannel.name()).isEqualTo(mockPrivateChannelDto.name());
        verify(channelRepository).save(any(Channel.class));
        verify(readStatusRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("비공개 채널 생성 실패 - 존재하지 않는 사용자 포함")
    void createPrivateChannel_Failure_UserNotFound() {
        UUID nonExistentUserId = UUID.randomUUID();
        PrivateChannelCreateRequest requestWithInvalidUser = new PrivateChannelCreateRequest(
            Arrays.asList(testUserId, nonExistentUserId),
            // testUser는 존재, nonExistentUserId는 존재하지 않음
            "Private Chat With Invalid User"
        );

        given(userRepository.existsById(testUserId)).willReturn(true); // testUserId는 존재
        given(userRepository.existsById(nonExistentUserId)).willReturn(
            false); // nonExistentUserId는 존재하지 않음

        // UserNotFoundException이 발생하고, details에 존재하지 않는 ID가 포함되는지 확인
        UserNotFoundException thrownException = catchThrowableOfType(
            () -> channelService.create(requestWithInvalidUser),
            UserNotFoundException.class
        );

        assertThat(thrownException).isNotNull();
        Map<String, Object> actualDetails = thrownException.getDetails();
        assertThat(actualDetails).isNotNull();
        assertThat(actualDetails)
            .containsEntry("userId", nonExistentUserId.toString())
            .hasSize(1); // Expecting only userId in details
    }

    @Test
    @DisplayName("공개 채널 생성 실패 (이름 중복)")
    void createPublicChannel_Failure_NameExists() {
        given(channelRepository.existsByName(publicChannelCreateRequest.name())).willReturn(true);

        Map<String, Object> expectedDetails = Map.of("channelName",
            publicChannelCreateRequest.name());
        assertThatThrownBy(() -> channelService.create(publicChannelCreateRequest))
            .isInstanceOf(ChannelException.class)
            .hasFieldOrPropertyWithValue("details", expectedDetails);
    }

    @Test
    @DisplayName("채널 정보 수정 성공 (공개 채널)")
    void updatePublicChannel_Success() {
        Channel existingPublicChannel = new Channel(ChannelType.PUBLIC, "Old Name", "Old Desc");
        given(channelRepository.findById(publicChannelId)).willReturn(
            Optional.of(existingPublicChannel));
        given(channelRepository.save(any(Channel.class))).willAnswer(
            invocation -> invocation.getArgument(0));
        given(channelMapper.toDto(any(Channel.class))).willReturn(
            new ChannelDto(publicChannelId, ChannelType.PUBLIC,
                publicChannelUpdateRequest.newName(), publicChannelUpdateRequest.newDescription(),
                Collections.emptySet(), Instant.now())
        );

        ChannelDto updatedChannel = channelService.update(publicChannelId,
            publicChannelUpdateRequest);

        assertThat(updatedChannel).isNotNull();
        assertThat(updatedChannel.name()).isEqualTo(publicChannelUpdateRequest.newName());
        assertThat(updatedChannel.description()).isEqualTo(
            publicChannelUpdateRequest.newDescription());
        verify(channelRepository).save(any(Channel.class));
    }

    @Test
    @DisplayName("채널 수정 실패 (존재하지 않는 채널)")
    void updateChannel_Failure_ChannelNotFound() {
        given(channelRepository.findById(publicChannelId)).willReturn(Optional.empty());

        Map<String, Object> expectedDetails = Map.of("channelId", publicChannelId.toString());
        assertThatThrownBy(() -> channelService.update(publicChannelId, publicChannelUpdateRequest))
            .isInstanceOf(ChannelNotFoundException.class)
            .hasFieldOrPropertyWithValue("details", expectedDetails);
    }

    @Test
    @DisplayName("채널 수정 실패 (비공개 채널)")
    void updateChannel_Failure_PrivateChannelUpdateDenied() {
        Channel existingPrivateChannel = new Channel(ChannelType.PRIVATE, "Private", null);
        given(channelRepository.findById(privateChannelId)).willReturn(
            Optional.of(existingPrivateChannel));

        Map<String, Object> expectedDetails = Map.of("channelId", privateChannelId.toString());
        assertThatThrownBy(
            () -> channelService.update(privateChannelId, publicChannelUpdateRequest))
            .isInstanceOf(ChannelException.class)
            .hasFieldOrPropertyWithValue("details", expectedDetails);
    }

    @Test
    @DisplayName("채널 삭제 성공")
    void deleteChannel_Success() {
        given(channelRepository.existsById(publicChannelId)).willReturn(true);
        willDoNothing().given(messageRepository).deleteAllByChannelId(publicChannelId);
        willDoNothing().given(readStatusRepository).deleteAllByChannelId(publicChannelId);
        willDoNothing().given(channelRepository).deleteById(publicChannelId);

        channelService.delete(publicChannelId);

        verify(messageRepository).deleteAllByChannelId(publicChannelId);
        verify(readStatusRepository).deleteAllByChannelId(publicChannelId);
        verify(channelRepository).deleteById(publicChannelId);
    }

    @Test
    @DisplayName("채널 삭제 실패 - 존재하지 않는 채널")
    void deleteChannel_Failure_ChannelNotFound() {
        given(channelRepository.findById(publicChannelId)).willReturn(Optional.empty());

        Map<String, Object> expectedDetails = Map.of("channelId", publicChannelId.toString());
        assertThatThrownBy(() -> channelService.delete(publicChannelId))
            .isInstanceOf(ChannelNotFoundException.class)
            .hasFieldOrPropertyWithValue("details", expectedDetails);
    }

    @Test
    @DisplayName("사용자 ID로 모든 채널 조회 성공 (구독 채널 없음, 공개 채널만)")
    void findAllByUserId_Success_NoSubscribed_PublicOnly() {
        given(userRepository.existsById(testUserId)).willReturn(true);
        given(readStatusRepository.findAllByUserId(testUserId)).willReturn(Collections.emptyList());
        Channel public1 = new Channel(ChannelType.PUBLIC, "Public 1", "Desc1");
        Channel public2 = new Channel(ChannelType.PUBLIC, "Public 2", "Desc2");
        List<Channel> publicChannels = Arrays.asList(public1, public2);
        given(channelRepository.findAllByType(ChannelType.PUBLIC)).willReturn(publicChannels);

        when(channelMapper.toDto(public1)).thenReturn(
            new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "Public 1", "Desc1",
                Collections.emptySet(), Instant.now()));
        when(channelMapper.toDto(public2)).thenReturn(
            new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "Public 2", "Desc2",
                Collections.emptySet(), Instant.now()));

        List<ChannelDto> channels = channelService.findAllByUserId(testUserId);

        assertThat(channels).isNotNull();
        assertThat(channels).hasSize(2);
        assertThat(channels.stream().allMatch(c -> c.type() == ChannelType.PUBLIC)).isTrue();
    }

    @Test
    @DisplayName("사용자 ID로 모든 채널 조회 성공 (구독한 비공개 채널 포함)")
    void findAllByUserId_Success_WithSubscribedPrivateChannel() {
        given(userRepository.existsById(testUserId)).willReturn(true);

        UUID privateChannelId = UUID.randomUUID();
        UUID publicChannelId = UUID.randomUUID();

        Channel mockPrivateChannel = mock(Channel.class);
        Channel mockPublicChannel = mock(Channel.class);

        when(mockPrivateChannel.getId()).thenReturn(privateChannelId);
        when(mockPrivateChannel.getType()).thenReturn(ChannelType.PRIVATE);
        when(mockPrivateChannel.getName()).thenReturn("Private Channel");

        when(mockPublicChannel.getId()).thenReturn(publicChannelId);
        when(mockPublicChannel.getType()).thenReturn(ChannelType.PUBLIC);
        when(mockPublicChannel.getName()).thenReturn("Public Channel");

        ReadStatus privateReadStatus = mock(ReadStatus.class);
        when(privateReadStatus.getChannel()).thenReturn(mockPrivateChannel);

        List<ReadStatus> userReadStatuses = new ArrayList<>();
        userReadStatuses.add(privateReadStatus);
        given(readStatusRepository.findAllByUserId(testUserId)).willReturn(userReadStatuses);

        List<Channel> publicChannels = new ArrayList<>();
        publicChannels.add(mockPublicChannel);
        given(channelRepository.findAllByType(ChannelType.PUBLIC)).willReturn(publicChannels);

        List<UUID> privateChannelIds = Arrays.asList(privateChannelId);
        List<Channel> privateChannels = new ArrayList<>();
        privateChannels.add(mockPrivateChannel);
        given(
            channelRepository.findAllByIdInAndType(eq(privateChannelIds), eq(ChannelType.PRIVATE)))
            .willReturn(privateChannels);

        ChannelDto privateChannelDto = new ChannelDto(
            privateChannelId,
            ChannelType.PRIVATE,
            "Private Channel",
            null,
            Collections.emptySet(),
            Instant.now()
        );

        ChannelDto publicChannelDto = new ChannelDto(
            publicChannelId,
            ChannelType.PUBLIC,
            "Public Channel",
            "Description",
            Collections.emptySet(),
            Instant.now()
        );

        when(channelMapper.toDto(mockPrivateChannel)).thenReturn(privateChannelDto);
        when(channelMapper.toDto(mockPublicChannel)).thenReturn(publicChannelDto);

        List<ChannelDto> result = channelService.findAllByUserId(testUserId);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.stream().filter(c -> c.type() == ChannelType.PRIVATE).count())
            .isEqualTo(1);
        assertThat(result.stream().filter(c -> c.type() == ChannelType.PUBLIC).count())
            .isEqualTo(1);
    }

    @Test
    @DisplayName("사용자 ID로 모든 채널 조회 실패 - 사용자 없음")
    void findAllByUserId_Failure_UserNotFound() {
        given(userRepository.existsById(testUserId)).willReturn(false);

        Map<String, Object> expectedDetails = Map.of("userId", testUserId.toString());
        assertThatThrownBy(() -> channelService.findAllByUserId(testUserId))
            .isInstanceOf(UserNotFoundException.class)
            .hasFieldOrPropertyWithValue("details", expectedDetails);
    }
} 