package Service;

import com.sprint.mission.discodeit.dto.service.channel.ChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.CreateChannelParam;
import com.sprint.mission.discodeit.dto.service.channel.FindChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.UpdateChannelParam;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusParam;
import com.sprint.mission.discodeit.dto.service.readStatus.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BasicChannelServiceTest {
    @Mock
    ChannelRepository channelRepository;

    @Mock
    MessageRepository messageRepository;

    @Mock
    ReadStatusService readStatusService;

    @InjectMocks
    BasicChannelService basicChannelService;

    Channel mockPublicChannel = Channel.builder()
            .name("publicTest")
            .description("publicTesting")
            .type(ChannelType.PUBLIC)
            .build();

    Channel mockPrivatecChannel = Channel.builder()
            .name("privateTest")
            .description("privateTesting")
            .type(ChannelType.PRIVATE)
            .build();

    @Test
    void 공개_채널생성_성공() {
        CreateChannelParam createChannelParam = new CreateChannelParam(ChannelType.PUBLIC, "publicTest", "publicTesting");
        when(channelRepository.save(any(Channel.class))).thenReturn(mockPublicChannel);


        ChannelDTO channelDTO = basicChannelService.createPublicChannel(createChannelParam);

        assertEquals(channelDTO.name(), createChannelParam.name());
        assertEquals(channelDTO.type(), createChannelParam.type());
        assertEquals(channelDTO.description(), createChannelParam.description());

        verify(channelRepository, times(1)).save(any(Channel.class));
    }

    @Test
    void 공개_채널생성_필드없음_실패() {
        CreateChannelParam createChannelParam = new  CreateChannelParam(null, null, null);

        assertThatThrownBy(() -> basicChannelService.createPublicChannel(createChannelParam))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("BAD REQUEST");


        verify(channelRepository, never()).save(any(Channel.class));
    }

    @Test
    void 비밀_채널생성_성공() {
        List<UUID> userIds = List.of(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()
        );

        CreateChannelParam createChannelParam = new CreateChannelParam(ChannelType.PRIVATE, "privateTest", "privateTesting");

        ChannelDTO channelDTO = basicChannelService.createPrivateChannel(userIds, createChannelParam);

        assertNull(channelDTO.description());
        assertNull(channelDTO.name());
        assertEquals(channelDTO.type(), createChannelParam.type());

        verify(readStatusService, times(3)).create(any(CreateReadStatusParam.class));
        verify(channelRepository, times(1)).save(any(Channel.class));
    }

    @Test
    void 비밀_채널생성_필드없음_실패() {
        List<UUID> userIds = List.of(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()
        );

        CreateChannelParam createChannelParam = new CreateChannelParam(null, null, null);


        assertThatThrownBy(() -> basicChannelService.createPrivateChannel(userIds, createChannelParam))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("BAD REQUEST");

        verify(readStatusService, never()).create(any(CreateReadStatusParam.class));
        verify(channelRepository, never()).save(any(Channel.class));
    }

    @Test
    void 채널찾기_공개채널_성공() {
        when(channelRepository.findById(mockPublicChannel.getId())).thenReturn(Optional.of(mockPublicChannel));
        when(messageRepository.findLatestMessageTimeByChannelId(mockPublicChannel.getId())).thenReturn(Optional.of(Instant.now()));

        FindChannelDTO findChannelDTO = basicChannelService.find(mockPublicChannel.getId());

        assertEquals(findChannelDTO.id(), mockPublicChannel.getId());
        assertEquals(findChannelDTO.type(), mockPublicChannel.getType());
        assertEquals(findChannelDTO.name(), mockPublicChannel.getName());
        assertEquals(findChannelDTO.description(), mockPublicChannel.getDescription());

        verify(messageRepository,times(1)).findLatestMessageTimeByChannelId(any(UUID.class));
        verify(readStatusService, never()).findAllByChannelId(any(UUID.class));
    }

    @Test
    void 채널찾기_공개채널_실패() {
        CreateChannelParam createChannelParam = new CreateChannelParam(ChannelType.PRIVATE,null, null);

        assertThatThrownBy(() -> basicChannelService.createPublicChannel(createChannelParam))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("BAD REQUEST");

        verify(messageRepository,never()).findLatestMessageTimeByChannelId(any(UUID.class));
        verify(readStatusService, never()).findAllByChannelId(any(UUID.class));
        verify(channelRepository, never()).save(any(Channel.class));
    }

    @Test
    void 채널찾기_비밀채널_성공() {
        when(channelRepository.findById(mockPrivatecChannel.getId())).thenReturn(Optional.of(mockPrivatecChannel));
        when(messageRepository.findLatestMessageTimeByChannelId(mockPrivatecChannel.getId())).thenReturn(Optional.of(Instant.now()));

        FindChannelDTO findChannelDTO = basicChannelService.find(mockPrivatecChannel.getId());

        assertEquals(findChannelDTO.id(), mockPrivatecChannel.getId());
        assertEquals(findChannelDTO.type(), mockPrivatecChannel.getType());

        verify(messageRepository, times(1)).findLatestMessageTimeByChannelId(any(UUID.class));
        verify(readStatusService, times(1)).findAllByChannelId(any(UUID.class));
    }

    @Test
    void 채널찾기_비밀채널_실패() {
        CreateChannelParam createChannelParam = new CreateChannelParam(ChannelType.PRIVATE, null, null);
        List<UUID> userIds = List.of();

        assertThatThrownBy(() -> basicChannelService.createPrivateChannel(userIds, createChannelParam))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("BAD REQUEST");

        verify(messageRepository,never()).findLatestMessageTimeByChannelId(any(UUID.class));
        verify(readStatusService, never()).findAllByChannelId(any(UUID.class));
        verify(channelRepository, never()).save(any(Channel.class));
    }


    @Test
    void 채널수정_공개채널_성공() {
        UpdateChannelParam updateChannelParam = new UpdateChannelParam(mockPublicChannel.getId(), "updateTest", "updateTesting");

        when(channelRepository.findById(updateChannelParam.id())).thenReturn(Optional.of(mockPublicChannel));

        UUID id = basicChannelService.update(updateChannelParam);

        assertEquals(updateChannelParam.id(), id);

        verify(channelRepository, times(1)).findById(mockPublicChannel.getId());
        verify(channelRepository, times(1)).save(any(Channel.class));

    }

    @Test
    void 채널수정_비밀채널_실패() {
        UpdateChannelParam updateChannelParam = new UpdateChannelParam(mockPrivatecChannel.getId(), "updateTest", "updateTesting");

        when(channelRepository.findById(updateChannelParam.id())).thenReturn(Optional.of(mockPrivatecChannel));

        assertThatThrownBy(() -> basicChannelService.update(updateChannelParam))
                .isInstanceOf(RestException.class)
                        .hasMessageContaining("Private channel is unauthorized");

        verify(channelRepository, times(1)).findById(mockPrivatecChannel.getId());
        verify(channelRepository, never()).save(any(Channel.class));

    }

    @Test
    void 채널수정_채널없음_실패() {
        UUID fakeId = UUID.randomUUID();
        UpdateChannelParam updateParam = new UpdateChannelParam(fakeId, "test", "test desc");

        when(channelRepository.findById(fakeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> basicChannelService.update(updateParam))
                .isInstanceOf(RestException.class)
                .hasMessageContaining("Channel not found");

        verify(channelRepository, times(1)).findById(any(UUID.class));
        verify(channelRepository, never()).save(any(Channel.class));
    }

    @Test
    void 채널삭제_성공() {
        basicChannelService.delete(mockPublicChannel.getId());

        verify(readStatusService, times(1)).deleteByChannelId(mockPublicChannel.getId());
        verify(messageRepository, times(1)).deleteByChannelId(mockPublicChannel.getId());
        verify(channelRepository, times(1)).deleteById(mockPublicChannel.getId());
    }
}




