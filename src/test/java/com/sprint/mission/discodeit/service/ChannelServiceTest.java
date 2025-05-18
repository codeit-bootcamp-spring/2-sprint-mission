package com.sprint.mission.discodeit.service.test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

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
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

/*
ChannelService : create(Public, Private), update, delete, findByUserId 의 성공, 실패 testCase
 */
@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

    @Mock
    ChannelRepository channelRepository;
    @Mock
    ReadStatusRepository readStatusRepository;
    @Mock
    MessageRepository messageRepository;
    @Mock
    ChannelMapper channelMapper;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    BasicChannelService channelService;

    @Test
    @DisplayName("PublicChannelCreateTest_success")
    public void PublicChannelCreateTest_success() {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("test", "testChannel");
        Channel channel = new Channel(ChannelType.PUBLIC, "test", "testChannel");
        UUID uuid = UUID.randomUUID();
        // test 시에 자동으로 생성되는 id값을 직접 만든 id값으로 강제 할당
        ReflectionTestUtils.setField(channel, "id", uuid);

        // 반환해야할 Dto
        ChannelDto expected = new ChannelDto(
            uuid, ChannelType.PUBLIC, "test", "testChannel", null, Instant.now());

        // given
        given(channelRepository.save(any(Channel.class))).willAnswer(invocationOnMock -> {
            Channel saved = invocationOnMock.getArgument(0);
            ReflectionTestUtils.setField(saved, "id", uuid);
            return saved;
        });

        // when
        ChannelDto result = channelService.create(request);

        // then
        assertThat(result).isEqualTo(expected);
        then(channelRepository).should().save(any(Channel.class));
    }

    @Test
    @DisplayName("PrivateCreateChannelTest_success")
    public void PrivateCreateChannelTest_success() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        List<UUID> participantIds = List.of(userId1, userId2);
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);

        // 초기 setting user, channel
        User user1 = new User("user1", "user1@naver.com", "password1", null);
        User user2 = new User("user2", "user2@naver.com", "password2", null);
        ReflectionTestUtils.setField(user1, "id", userId1);
        ReflectionTestUtils.setField(user2, "id", userId2);

        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        UUID channelId = UUID.randomUUID();
        ReflectionTestUtils.setField(channel, "id", channelId);

        ChannelDto expectedDto = new ChannelDto(
            channelId, ChannelType.PRIVATE, null, null, null, Instant.now());

        // given
        given(readStatusRepository.saveAll(any())).willReturn(null);
        given(channelRepository.save(any(Channel.class))).willAnswer(invocationOnMock -> {
            Channel saved = invocationOnMock.getArgument(0);
            ReflectionTestUtils.setField(saved, "id", channelId);
            return saved;
        });
        given(readStatusRepository.saveAll(any())).willReturn(null);
        given(channelMapper.toDto(channel)).willReturn(expectedDto);

        // when
        ChannelDto result = channelService.create(request);

        // then
        // 실행 후 Dto결과와 동일한지 판단
        assertThat(result).isEqualTo(expectedDto);
        // 실제 로직에서 method가 호출되었는지 확인하는 것, 인자도 제대로 전달되었는지 확인함.
        // then(mock).should().methodCall(...)
        then(userRepository).should().findAllById(participantIds);
        // any는 인자 상관없이 호출만 확인하고 싶을 때 넣어줌
        then(readStatusRepository).should().saveAll(any());
        then(channelRepository).should().save(any(Channel.class));
    }

    @Test
    @DisplayName("UpdateChannelTest_success")
    public void UpdateChannelTest_success() {
        UUID channelId = UUID.randomUUID();
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("test", "testChannel");

        Channel original = new Channel(ChannelType.PUBLIC, "origin", "OriginChannel");
        ReflectionTestUtils.setField(original, "id", channelId);
        Channel update = new Channel(ChannelType.PUBLIC, "test", "testChannel");
        ReflectionTestUtils.setField(update, "id", channelId);

        ChannelDto expected = new ChannelDto(
            channelId, ChannelType.PUBLIC, "test", "testChannel", null, null);

        given(channelRepository.findById(channelId)).willReturn(Optional.of(original));
        given(channelMapper.toDto(original)).willReturn(expected);

        ChannelDto result = channelService.update(channelId, request);

        assertThat(result).isEqualTo(expected);
        then(channelRepository).should().findById(channelId);
    }

    @Test
    @DisplayName("UpdateChannelTest_fail_private")
    public void UpdateChannelTest_fail_private() {
        UUID channelId = UUID.randomUUID();
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        ReflectionTestUtils.setField(channel, "id", channelId);
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("test", "testChannel");

        // given
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        // assertThat 실패했을 때 상황
        // when / then
        assertThatThrownBy(() -> channelService.update(channelId, request))
            .isInstanceOf(PrivateChannelUpdateException.class);
    }

    @Test
    @DisplayName("DeleteChannelTest_success")
    void DeleteChannelTest_success() {
        UUID channelId = UUID.randomUUID();

        // given 주어지는 정보 -> channelId에 해당하는 정보가 저장되어있음
        given(channelRepository.existsById(channelId)).willReturn(true);

        // when
        channelService.delete(channelId);
        // then
        // 채널삭제시 message, readStatus 삭제 후 channel 삭제하는 메서드 호출
        then(channelRepository).should().existsById(channelId);
        then(messageRepository).should().deleteAllByChannelId(channelId);
        then(readStatusRepository).should().deleteAllByChannelId(channelId);
        then(channelRepository).should().deleteById(channelId);
    }

    @Test
    @DisplayName("DeleteChannelTest_fail_notFound")
    void DeleteChannelTest_fail_notFound() {
        UUID channelId = UUID.randomUUID();
        // given
        given(channelRepository.existsById(channelId)).willReturn(false);

        assertThatThrownBy(() -> channelService.delete(channelId))
            .isInstanceOf(ChannelNotFoundException.class);

        then(channelRepository).should().existsById(channelId);
        then(messageRepository).should(never()).deleteAllByChannelId(any());
        then(readStatusRepository).should(never()).deleteAllByChannelId(any());
        then(channelRepository).should(never()).deleteById(any());
    }

    @Test
    @DisplayName("FindAllChannelByUserIdTest_success")
    void findAllChannelByUserIdTest_success() {
        UUID userId = UUID.randomUUID();
        UUID channelId1 = UUID.randomUUID();
        UUID channelId2 = UUID.randomUUID();
        Channel channel1 = new Channel(ChannelType.PUBLIC, "test1", "testDescription1");
        Channel channel2 = new Channel(ChannelType.PRIVATE, null, null);
        ReflectionTestUtils.setField(channel1, "id", channelId1);
        ReflectionTestUtils.setField(channel2, "id", channelId2);

        //
        ReadStatus readStatus = new ReadStatus(null, channel1, Instant.now());
        // given -> willReturn의 내용들을 미리 정의해놓는다.
        given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(readStatus));
        // 하나의 given, verify 문 안에서 인자를 섞어 쓰면 안된다.
        // ex) (x, anyList()) -> (eq(x), anyList())
        given(channelRepository.findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), anyList()))
            .willReturn(List.of(channel1, channel2));

        ChannelDto channelDto1 = new ChannelDto(channelId1, ChannelType.PUBLIC, "test1",
            "testDescription1", null, Instant.now());
        ChannelDto channelDto2 = new ChannelDto(channelId2, ChannelType.PRIVATE, null, null, null,
            Instant.now());
        given(channelMapper.toDto(channel1)).willReturn(channelDto1);
        given(channelMapper.toDto(channel2)).willReturn(channelDto2);

        // when
        List<ChannelDto> result = channelService.findAllByUserId(userId);

        // then
        assertThat(result).isEqualTo(List.of(channelDto1, channelDto2));
    }
}
