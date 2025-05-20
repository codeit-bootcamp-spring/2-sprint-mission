package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {
    @Mock
    ChannelRepository channelRepository;
    @Mock
    ChannelMapper channelMapper;
    @Mock
    ReadStatusRepository readStatusRepository;
    @Mock
    MessageRepository messageRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    BasicChannelService channelService;

    @Test
    void createPublicChannel_success() {
        // given
        String name = "공개채널";
        String desc = "소개";
        PublicChannelCreateRequest request = new PublicChannelCreateRequest(name, desc);

        Channel channel = new Channel(ChannelType.PUBLIC, name, desc);
        UUID channelId = UUID.randomUUID();

        ChannelDto expectedDto = new ChannelDto(
                channelId,
                ChannelType.PUBLIC,
                name,
                desc,
                List.of(),
                null
        );

        given(channelRepository.save(any(Channel.class))).willReturn(channel);
        given(channelMapper.toDto(any(Channel.class))).willReturn(expectedDto);

        // when
        ChannelDto dto = channelService.create(request);

        // then
        assertEquals(name, dto.name());
        assertEquals(ChannelType.PUBLIC, dto.type());
        assertEquals(desc, dto.description());
    }

    @Test
    void createPrivateChannel_success() {
        // given
        UUID userId = UUID.randomUUID();
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(userId));
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);

        given(channelRepository.save(any(Channel.class))).willReturn(channel);
        given(userRepository.findAllById(any())).willReturn(List.of(mock(User.class)));
        given(readStatusRepository.saveAll(any())).willReturn(List.of(mock(ReadStatus.class)));

        // when
        ChannelDto dto = channelService.create(request);

        // then
        then(channelRepository).should().save(any(Channel.class));
        then(userRepository).should().findAllById(any());
        then(readStatusRepository).should().saveAll(any());
    }



}
