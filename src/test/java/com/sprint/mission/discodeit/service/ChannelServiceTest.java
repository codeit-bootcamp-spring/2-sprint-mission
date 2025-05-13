package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ChannelServiceTest {
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
    private ChannelService channelService;

    @Test
    @DisplayName("정상적인 Public Channel 생성 테스트")
    void createPublicChannel_WithValidInput_Success() {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("공지 사항", "전체 공지 채널입니다.");

        Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
        ReflectionTestUtils.setField(channel, "id", UUID.randomUUID());

        ChannelDto expectedDto = new ChannelDto(
                channel.getId(),
                channel.getType(),
                "공지사항",
                "전체 공지 채널입니다.",
                null,
                null
        );

        given(channelMapper.toDto(any())).willReturn(expectedDto);

        ChannelDto channelDto = channelService.create(request);

        assertEquals("공지사항", channelDto.name());
        assertEquals("전체 공지 채널입니다.", channelDto.description());
        assertEquals(ChannelType.PUBLIC, channelDto.type());
        assertNull(channelDto.participants());
        assertNull(channelDto.lastMessageAt());

        verify(channelRepository).save(any());
    }

    @Test
    @DisplayName("정상적인 Private Channel 생성 테스트")
    void createPrivateChannel_WithValidInput_Success() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(userId1, userId2));

        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        ReflectionTestUtils.setField(channel, "id", UUID.randomUUID());

        User user1 = new User("user1", "user1@gmail.com", "1234", null);
        User user2 = new User("user2", "user2@gmail.com", "5678", null);
        ReflectionTestUtils.setField(user1, "id", userId1);
        ReflectionTestUtils.setField(user2, "id", userId2);

        ChannelDto expectedDto = new ChannelDto(
                channel.getId(),
                channel.getType(),
                null,
                null,
                null,
                null
        );

        List<User> users = List.of(user1, user2);
        given(userRepository.findAllById(request.participantIds())).willReturn(users);
        given(channelMapper.toDto(any())).willReturn(expectedDto);

        ChannelDto channelDto = channelService.create(request);

        assertEquals(ChannelType.PRIVATE, channelDto.type());
        assertNull(channelDto.name());
        assertNull(channelDto.description());
        assertNull(channelDto.participants());
        assertNull(channelDto.lastMessageAt());

        verify(channelRepository).save(any());
        verify(readStatusRepository).saveAll(any());
    }

    @Test
    @DisplayName("정상적인 Channel update 테스트")
    void updateChannel_WithValidInput_Success() {
        Channel channel = new Channel(ChannelType.PUBLIC, "공지 사항", "전체 공지 채널입니다.");
        ReflectionTestUtils.setField(channel, "id", UUID.randomUUID());
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("전체 공지", "전체 공지 채널입니다.");

        ChannelDto expectedDto = new ChannelDto(
                channel.getId(),
                channel.getType(),
                "전체 공지",
                "전체 공지 채널입니다.",
                null,
                null
        );

        given(channelRepository.findById(channel.getId())).willReturn(Optional.of(channel));
        given(channelMapper.toDto(any())).willReturn(expectedDto);

        ChannelDto channelDto = channelService.updateChannel(channel.getId(), request);

        assertEquals(ChannelType.PUBLIC, channelDto.type());
        assertEquals("전체 공지", channelDto.name());
        assertEquals("전체 공지 채널입니다.", channelDto.description());

        verify(channelMapper).toDto(channel);
    }
    @Test
    @DisplayName("존재하지 않는 channelId로 업데이트 시 예외 발생 테스트")
    void updateChannel_WithInvalidChannelId_ThrowException() {
        UUID noExistId = UUID.randomUUID();
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("전체 공지","전체 공지 채널입니다.");

        given(channelRepository.findById(noExistId)).willReturn(Optional.empty());

        assertThrows(ChannelNotFoundException.class, () -> channelService.updateChannel(noExistId, request));
    }
    @Test
    @DisplayName("Private 채널을 수정하려 할 때 예외 발생 테스트")
    void update_PrivateChannel_ThrowException() {
        Channel privateChannel = new Channel(ChannelType.PRIVATE, null, null);
        ReflectionTestUtils.setField(privateChannel, "id", UUID.randomUUID());

        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("개인 채널","개인 채널입니다.");

        given(channelRepository.findById(privateChannel.getId())).willReturn(Optional.of(privateChannel));

        assertThrows(PrivateChannelUpdateException.class, () -> channelService.updateChannel(privateChannel.getId(), request));
    }
}
