package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.ErrorCode;
import com.sprint.mission.discodeit.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exceptions.channel.DuplicateChannelException;
import com.sprint.mission.discodeit.exceptions.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelJPARepository;
import com.sprint.mission.discodeit.repository.ReadStatusJPARepository;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.service.dto.response.UserResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceUnitTest {

    @Mock
    private ChannelJPARepository channelJPARepository;

    @Mock
    private ReadStatusJPARepository readStatusJPARepository;

    @Mock
    private UserJPARepository userJPARepository;

    @Mock
    private ChannelMapper channelMapper;

    @InjectMocks
    private BasicChannelService basicChannelService;


    @Test
    @DisplayName("[Channel][create_private] private channel 생성")
    public void testCreatePrivate() {
        // 유저 생성
        List<UUID> userIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        List<User> users = List.of(
                new User("Test", "Test@test.com", "12345", null),
                new User("Test2", "Test2@test.com", "12345", null));
        for (int i = 0; i < userIds.size(); i++) {
            ReflectionTestUtils.setField(users.get(i), "id", userIds.get(i));
        }
        List<UserResponseDto> responseUser = users.stream()
                .map(user -> new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                true))
                .toList();

        // 채널
        ChannelCreatePrivateDto privateRequest = new ChannelCreatePrivateDto(userIds);
        UUID channelId = UUID.randomUUID();
        Channel saveChannel = new Channel(ChannelType.PRIVATE, null, null);
        ReflectionTestUtils.setField(saveChannel, "id", channelId);

        ChannelResponseDto responseChannel = new ChannelResponseDto(
                channelId,
                saveChannel.getType(),
                saveChannel.getName(),
                saveChannel.getDescription(),
                responseUser,
                Instant.MIN
        );

        given(channelJPARepository.save(any(Channel.class))).willReturn(saveChannel);
        given(userJPARepository.findByIdIn(userIds)).willReturn(users);
        given(channelMapper.toDto(any(Channel.class))).willReturn(responseChannel);

        ChannelResponseDto result = basicChannelService.createPrivate(privateRequest);

        then(channelJPARepository).should().save(any(Channel.class));
        then(userJPARepository).should().findByIdIn(userIds);
        then(channelMapper).should().toDto(saveChannel);

        assertEquals(responseChannel, result);
        assertEquals(ChannelType.PRIVATE, saveChannel.getType());
    }

    @Test
    @DisplayName("[Channel][create_public] public channel 생성")
    public void testCreatePublic() {
        ChannelCreatePublicDto publicRequest = new ChannelCreatePublicDto("공개방", "공개방입니다");

        UUID channelId = UUID.randomUUID();
        Channel saveChannel = new Channel(ChannelType.PUBLIC, publicRequest.name(), publicRequest.description());
        ReflectionTestUtils.setField(saveChannel, "id", channelId);

        ChannelResponseDto responseChannel = new ChannelResponseDto(
                channelId,
                saveChannel.getType(),
                saveChannel.getName(),
                saveChannel.getDescription(),
                null,
                Instant.MIN
        );

        given(channelJPARepository.existsByTypeAndName(ChannelType.PUBLIC, publicRequest.name())).willReturn(false);
        given(channelJPARepository.save(any(Channel.class))).willReturn(saveChannel);
        given(channelMapper.toDto(any(Channel.class))).willReturn(responseChannel);

        ChannelResponseDto result = basicChannelService.createPublic(publicRequest);

        // ArgumentCaptor 검증(전달 값)
        ArgumentCaptor<Channel> channelCaptor = ArgumentCaptor.forClass(Channel.class);
        verify(channelJPARepository).save(channelCaptor.capture());

        then(channelJPARepository).should().existsByTypeAndName(ChannelType.PUBLIC, publicRequest.name());
        then(channelJPARepository).should().save(any(Channel.class));
        then(channelMapper).should().toDto(any(Channel.class));

        Channel captured = channelCaptor.getValue();
        assertEquals(publicRequest.name(), captured.getName());
        assertEquals(publicRequest.description(), captured.getDescription());
        assertEquals(ChannelType.PUBLIC, captured.getType());

        // 최종 결과 검증
        assertEquals("공개방", result.name());
        assertEquals("공개방입니다", result.description());
    }

    @Test
    @DisplayName("[Channel][create_public] channel 중복 생성 예외 테스트")
    public void testCreatePublicWithDuplicateChannelException() {
        ChannelCreatePublicDto publicRequest = new ChannelCreatePublicDto("공개방", "공개방입니다");
        Channel saveChannel = new Channel(ChannelType.PUBLIC, publicRequest.name(), publicRequest.description());

        given(channelJPARepository.existsByTypeAndName(ChannelType.PUBLIC, publicRequest.name())).willReturn(true);

        DuplicateChannelException exception = assertThrows(DuplicateChannelException.class,
                ()-> basicChannelService.createPublic(publicRequest));

        then(channelJPARepository).should(never()).save(any(Channel.class));

        assertEquals(ErrorCode.DUPLICATE_CHANNEL, exception.getErrorCode());
    }


    @Test
    @DisplayName("[Channel][update] channel 수정 테스트")
    public void testUpdateChannel() {
        ChannelUpdateDto updateRequest = new ChannelUpdateDto("제목변경", "제목변경합니다.");

        UUID channelId = UUID.randomUUID();
        Channel saveChannel = new Channel(ChannelType.PUBLIC, "공개방", "공개방입니다.");
        ReflectionTestUtils.setField(saveChannel, "id", channelId);

        ChannelResponseDto responseChannel = new ChannelResponseDto(
                channelId,
                saveChannel.getType(),
                updateRequest.newName(),
                updateRequest.newDescription(),
                null,
                Instant.MIN
        );

        given(channelJPARepository.findById(channelId)).willReturn(Optional.of(saveChannel));
        given(channelJPARepository.save(any(Channel.class))).willReturn(saveChannel);
        given(channelMapper.toDto(any(Channel.class))).willReturn(responseChannel);

        ChannelResponseDto result = basicChannelService.update(channelId, updateRequest);

        then(channelJPARepository).should().findById(channelId);
        then(channelJPARepository).should().save(any(Channel.class));
        then(channelMapper).should().toDto(any(Channel.class));

        assertEquals(responseChannel, result);
        assertEquals("제목변경", result.name());
        assertEquals("제목변경합니다.", result.description());
    }


    @Test
    @DisplayName("[Channel][update] 존재하지 않은 channel 수정 예외 테스트")
    public void testUpdateChannelWithChannelNotFoundException() {
        ChannelUpdateDto updateRequest = new ChannelUpdateDto("제목변경", "제목변경합니다.");
        UUID channelId = UUID.randomUUID();

        ChannelNotFoundException exception = assertThrows(ChannelNotFoundException.class,
                () -> basicChannelService.update(channelId, updateRequest));

        then(channelJPARepository).should().findById(channelId);
        then(channelJPARepository).should(never()).save(any(Channel.class));

        assertEquals(ErrorCode.CHANNEL_NOT_FOUND, exception.getErrorCode());

    }


    @Test
    @DisplayName("[Channel][update] private channel 수정 예외 테스트")
    public void testUpdateChannelWithPrivateChannelUpdateException() {
        ChannelUpdateDto updateRequest = new ChannelUpdateDto("제목변경", "제목변경합니다.");

        UUID channelId = UUID.randomUUID();
        Channel saveChannel = new Channel(ChannelType.PRIVATE, "공개방", "공개방입니다.");
        ReflectionTestUtils.setField(saveChannel, "id", channelId);

        given(channelJPARepository.findById(channelId)).willReturn(Optional.of(saveChannel));

        PrivateChannelUpdateException exception = assertThrows(PrivateChannelUpdateException.class,
                () -> basicChannelService.update(channelId, updateRequest));

        then(channelJPARepository).should().findById(channelId);
        then(channelJPARepository).should(never()).save(any(Channel.class));

        assertEquals(ErrorCode.PRIVATE_CHANNEL_UPDATE, exception.getErrorCode());

    }

    @Test
    @DisplayName("[Channel][delete] channel 삭제 테스트")
    public void testDeleteChannel() {
        UUID channelID = UUID.randomUUID();
        Channel saveChannel = new Channel(ChannelType.PUBLIC, "공개방", "공개방입니다.");
        ReflectionTestUtils.setField(saveChannel, "id", channelID);

        given(channelJPARepository.findById(channelID)).willReturn(Optional.of(saveChannel));

        basicChannelService.delete(channelID);

        then(channelJPARepository).should().delete(any(Channel.class));
    }

    @Test
    @DisplayName("[Channel][delete] 존재하지 않은 channel 삭제 예외 테스트")
    public void testDeleteChannelWithChannelNotFoundException() {
        UUID channelID = UUID.randomUUID();

        ChannelNotFoundException exception = assertThrows(ChannelNotFoundException.class,
                () -> basicChannelService.delete(channelID));

        then(channelJPARepository).should().findById(channelID);
        then(channelJPARepository).should(never()).delete(any(Channel.class));

        assertEquals(ErrorCode.CHANNEL_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("[Channel][findAll] 사용자 id로 channel 조회 테스트")
    public void testFindAllByUserId() {
        UUID userId = UUID.randomUUID();
        User user = new User("Test", "Test@test.com", "12345", null);
        ReflectionTestUtils.setField(user, "id", userId);
        UserResponseDto responseUser = new UserResponseDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        null,
                        true);

        List<UUID> channelIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        List<Channel> saveChannels = Arrays.asList(
                new Channel(ChannelType.PUBLIC, "공개방", "공개방입니다."),
                new Channel(ChannelType.PRIVATE, null, null)
        );
        for (int i =0; i<channelIds.size(); i++) {
            ReflectionTestUtils.setField(saveChannels.get(i), "id", channelIds.get(i));
        }
        List<ChannelResponseDto> responseChannels = saveChannels.stream()
                .map(channel -> new ChannelResponseDto(
                        channel.getId(),
                        channel.getType(),
                        channel.getName(),
                        channel.getDescription(),
                        List.of(responseUser),
                        Instant.MIN
                    ))
                .toList();

        List<ReadStatus> readStatus = Arrays.asList(
                new ReadStatus(user, saveChannels.get(0), Instant.now()),
                new ReadStatus(user, saveChannels.get(1), Instant.now())
        );

        given(readStatusJPARepository.findByUser_Id(userId)).willReturn(readStatus);
        given(channelJPARepository.findByTypeOrIdIn(ChannelType.PUBLIC, channelIds)).willReturn(saveChannels);
        given(channelMapper.toDto(any(Channel.class))).willAnswer(invocation -> {
            Channel channel = invocation.getArgument(0);
            return new ChannelResponseDto(
                    channel.getId(),
                    channel.getType(),
                    channel.getName(),
                    channel.getDescription(),
                    List.of(responseUser),
                    Instant.MIN
            );
        });

        List<ChannelResponseDto> result = basicChannelService.findAllByUserId(userId);

        then(readStatusJPARepository).should().findByUser_Id(userId);
        then(channelJPARepository).should().findByTypeOrIdIn(ChannelType.PUBLIC, channelIds);

        assertEquals(responseChannels, result);
    }
}
