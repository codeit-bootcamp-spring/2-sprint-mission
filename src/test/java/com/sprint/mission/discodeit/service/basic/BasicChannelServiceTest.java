package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class BasicChannelServiceTest {

    @Mock private ChannelRepository channelRepository;
    @Mock private ReadStatusRepository readStatusRepository;
    @Mock private MessageRepository messageRepository;
    @Mock private UserRepository userRepository;
    @Mock private ChannelMapper channelMapper;

    @InjectMocks
    private BasicChannelService basicChannelService;

    private User user;
    private Channel publicChannel;
    private Channel privateChannel;
    private ChannelDto publicChannelDto;
    private ChannelDto privateChannelDto;


    @BeforeEach
    void setUp() {
        // 사용자 및 채널 초기화
        user = User.builder().username("user1").email("user1@test.com").build();
        setId(user, UUID.randomUUID());

        publicChannel = Channel.builder()
                .name("public-channel")
                .description("public-desc")
                .type(ChannelType.PUBLIC)
                .build();
        setId(publicChannel, UUID.randomUUID());

        privateChannel = Channel.builder()
                .type(ChannelType.PRIVATE)
                .build();
        setId(privateChannel, UUID.randomUUID());

        UserDto participantDto = new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,      // BinaryContentDto profile (테스트에서는 null)
                true       // online (테스트에서는 true)
        );

        publicChannelDto = new ChannelDto(
                publicChannel.getId(),
                ChannelType.PUBLIC,
                "public-channel",
                "public-desc",
                Collections.emptyList(),
                Instant.parse("2025-05-12T00:00:00Z")
        );

        privateChannelDto = new ChannelDto(
                privateChannel.getId(),
                ChannelType.PRIVATE,
                null,
                null,
                List.of(participantDto),
                Instant.parse("2025-05-12T00:00:00Z")
        );
    }

    @Test
    @DisplayName("공개 채널 생성 성공")
    void createPublicChannel_Success() {
        // Given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest(
                "public-channel", "public-desc"
        );
        given(channelRepository.save(any())).willReturn(publicChannel);
        given(channelMapper.toDto(publicChannel)).willReturn(publicChannelDto);

        // When
        Channel result = basicChannelService.create(request);

        // Then
        then(channelRepository).should().save(any());
        assertThat(result.getType()).isEqualTo(ChannelType.PUBLIC);
    }

    @Test
    @DisplayName("비공개 채널 생성 성공 - 참여자 2명")
    void createPrivateChannel_Success() {
        // Given
        UUID user1Id = user.getId();
        UUID user2Id = UUID.randomUUID();
        User user2 = User.builder().username("user2").email("user2@test.com").build();
        setId(user2, user2Id);

        UserDto participantDto1 = new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                true
        );
        UserDto participantDto2 = new UserDto(
                user2.getId(),
                user2.getUsername(),
                user2.getEmail(),
                null,
                true
        );

        ChannelDto privateChannelDtoWithTwo = new ChannelDto(
                privateChannel.getId(),
                ChannelType.PRIVATE,
                null,
                null,
                List.of(participantDto1, participantDto2),
                Instant.parse("2025-05-12T00:00:00Z")
        );

        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
                List.of(user1Id, user2Id)
        );

        given(channelRepository.save(any())).willReturn(privateChannel);
        given(userRepository.findById(user1Id)).willReturn(Optional.of(user));
        given(userRepository.findById(user2Id)).willReturn(Optional.of(user2));
        given(readStatusRepository.save(any())).willReturn(new ReadStatus());
        given(channelMapper.toDto(privateChannel)).willReturn(privateChannelDtoWithTwo);

        // When
        Channel result = basicChannelService.create(request);

        // Then
        then(userRepository).should(times(2)).findById(any());
        then(readStatusRepository).should(times(2)).save(any());
        assertThat(result.getType()).isEqualTo(ChannelType.PRIVATE);
    }

    @Test
    @DisplayName("채널 조회 성공 - 모든 필드 포함")
    void findChannel_Success() {
        // Given
        UUID channelId = publicChannel.getId();
        given(channelRepository.findById(channelId)).willReturn(Optional.of(publicChannel));
        given(channelMapper.toDto(publicChannel)).willReturn(publicChannelDto);

        // When
        ChannelDto result = basicChannelService.find(channelId);

        // Then
        assertThat(result.id()).isEqualTo(channelId);
        assertThat(result.participants()).isEmpty();
        assertThat(result.lastMessageAt()).isEqualTo(Instant.parse("2025-05-12T00:00:00Z"));
    }

    @Test
    @DisplayName("사용자 채널 목록 조회 - 공개 및 비공개 채널 포함")
    void findAllByUserId_Success() {
        // Given
        UUID userId = user.getId();
        ReadStatus readStatus = ReadStatus.builder()
                .user(user)
                .channel(privateChannel)
                .lastReadAt(Instant.now())
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(readStatusRepository.findAllByUser(user)).willReturn(List.of(readStatus));
        given(channelRepository.findAll()).willReturn(List.of(publicChannel, privateChannel));
        given(channelMapper.toDto(publicChannel)).willReturn(publicChannelDto);
        given(channelMapper.toDto(privateChannel)).willReturn(privateChannelDto);

        // When
        List<ChannelDto> result = basicChannelService.findAllByUserId(userId);

        // Then
        assertThat(result)
                .hasSize(2)
                .extracting(ChannelDto::type)
                .containsExactly(ChannelType.PUBLIC, ChannelType.PRIVATE);

        assertThat(result.get(1).participants())
                .extracting(UserDto::username)
                .containsExactly("user1");
    }

    @Test
    @DisplayName("공개 채널 업데이트 - 이름 및 설명 변경")
    void updatePublicChannel_Success() {
        // Given
        UUID channelId = publicChannel.getId();
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(
                "new-name", "new-desc"
        );
        ChannelDto updatedChannelDto = new ChannelDto(
                channelId,
                ChannelType.PUBLIC,
                "new-name",
                "new-desc",
                Collections.emptyList(),
                Instant.parse("2025-05-12T00:00:00Z")
        );
        given(channelRepository.findById(channelId)).willReturn(Optional.of(publicChannel));
        given(channelRepository.save(publicChannel)).willReturn(publicChannel);
        given(channelMapper.toDto(publicChannel)).willReturn(updatedChannelDto);

        // When
        Channel result = basicChannelService.update(channelId, request);

        // Then
        assertThat(result.getName()).isEqualTo("new-name");
        assertThat(result.getDescription()).isEqualTo("new-desc");
    }

    @Test
    @DisplayName("채널 삭제 - 관련 메시지 및 읽기 상태 삭제")
    void deleteChannel_Success() {
        // Given
        UUID channelId = publicChannel.getId();
        given(channelRepository.findById(channelId)).willReturn(Optional.of(publicChannel));
        willDoNothing().given(messageRepository).deleteAllByChannel(publicChannel);
        willDoNothing().given(readStatusRepository).deleteAllByChannel(publicChannel);
        willDoNothing().given(channelRepository).deleteById(channelId);

        // When
        basicChannelService.delete(channelId);

        // Then
        then(messageRepository).should().deleteAllByChannel(publicChannel);
        then(readStatusRepository).should().deleteAllByChannel(publicChannel);
        then(channelRepository).should().deleteById(channelId);
    }

    // Reflection을 이용한 ID 설정 메서드
    private void setId(Object entity, UUID id) {
        try {
            Field idField = entity.getClass().getSuperclass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException("ID 설정 실패", e);
        }
    }
}
