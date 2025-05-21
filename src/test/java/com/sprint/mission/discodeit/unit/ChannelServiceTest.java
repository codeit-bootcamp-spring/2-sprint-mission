package com.sprint.mission.discodeit.unit;

import com.sprint.mission.discodeit.Mapper.ChannelMapper;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFound;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdate;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

    @Mock
    ChannelRepository channelRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ReadStatusRepository readStatusRepository;

    @Mock
    MessageRepository messageRepository;

    @Mock
    ChannelMapper channelMapper;

    @InjectMocks
    BasicChannelService channelService;

    @Test
    @DisplayName("public채널 생성 성공")
    void createPublicChannel(){
        // given
        PublicChannelCreateRequest channelCreateRequest = new PublicChannelCreateRequest("Public채널", "Public내용");

        Channel channel = new Channel(ChannelType.PUBLIC, "Public채널", "Public내용");

        given(channelRepository.save(any(Channel.class))).willReturn(channel);

        ChannelDto channelDto = new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "Public채널", "Public내용", Collections.emptyList(), Instant.now());
        given(channelMapper.toDto(channel)).willReturn(channelDto);

        // when
        ChannelDto result = channelService.create(channelCreateRequest);

        // then
        then(channelRepository).should().save(any(Channel.class));
        then(channelMapper).should().toDto(channel);
        assertThat(result).isEqualTo(channelDto);
    }

    @Test
    @DisplayName("private채널 생성 성공 ")
    void createPrivateChannel(){
        // given
        List<UUID> participants = List.of(UUID.randomUUID());
        PrivateChannelCreateRequest req = new PrivateChannelCreateRequest(participants);

        Channel savedChannel = new Channel(ChannelType.PRIVATE, null, null);
        given(channelRepository.save(any(Channel.class))).willReturn(savedChannel);

        User mockUser = new User("u","u@u","p",null);
        given(userRepository.findById(participants.get(0))).willReturn(Optional.of(mockUser));

        ChannelDto channelDto = new ChannelDto(UUID.randomUUID(), ChannelType.PRIVATE, "Private채널", null, Collections.emptyList(), Instant.now());
        given(channelMapper.toDto(savedChannel)).willReturn(channelDto);

        // when
        ChannelDto result = channelService.create(req);

        // then
        then(channelRepository).should().save(any(Channel.class));
        then(channelMapper).should().toDto(savedChannel);
        assertThat(result).isEqualTo(channelDto);
    }

    @Test
    @DisplayName("public채널 업데이트")
    void updatePublicChannel(){
        // given
        UUID channelId = UUID.randomUUID();
        PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest("newName", "newDescription");

        Channel existing = new Channel(ChannelType.PUBLIC, "oldName", "oldDesc");
        given(channelRepository.findById(channelId)).willReturn(Optional.of(existing));

        Channel saved = new Channel(ChannelType.PUBLIC, "newName", "newDescription");
        given(channelRepository.save(existing)).willReturn(saved);

        ChannelDto expectedDto = new ChannelDto(channelId, ChannelType.PUBLIC, "newName", "newDescription", List.of(), saved.getCreatedAt());
        given(channelMapper.toDto(saved)).willReturn(expectedDto);

        // when
        ChannelDto channelDto = channelService.update(channelId, publicChannelUpdateRequest);

        // then
        then(channelRepository).should().findById(channelId);
        then(channelRepository).should().save(existing);
        then(channelMapper).should().toDto(saved);

        assertThat(channelDto).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("public채널 업데이트 실패")
    void updateFailPrivateChannel(){
        // given
        UUID channelId = UUID.randomUUID();
        PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest("newName", "newDescription");

        Channel existing = new Channel(ChannelType.PRIVATE, "oldName", "oldDesc");
        given(channelRepository.findById(channelId)).willReturn(Optional.of(existing));

        // when
        Throwable thrown = catchThrowable( () -> channelService.update(channelId, publicChannelUpdateRequest));

        // then
        assertThat(thrown).isInstanceOf(PrivateChannelUpdate.class);
    }

    @Test
    @DisplayName("채널 삭제 성공")
    void deleteSuccess(){
        // given
        UUID channelId = UUID.randomUUID();
        given(channelRepository.findById(channelId)).willReturn(Optional.of(new Channel()));

        // when
        assertDoesNotThrow( () -> channelService.delete(channelId));

        // then
        then(messageRepository).should().deleteAllByChannelId(channelId);
        then(readStatusRepository).should().deleteAllByChannelId(channelId);
        then(channelRepository).should().deleteById(channelId);

    }

    @Test
    @DisplayName("채널 삭제 실패")
    void deleteFailed(){
        // given
        UUID channelId = UUID.randomUUID();
        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable( () -> channelService.delete(channelId));

        // then
        assertThat(thrown).isInstanceOf(ChannelNotFound.class);
    }

}
