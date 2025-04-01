import com.sprint.mission.discodeit.dto.ChannelFindDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @InjectMocks
    private BasicChannelService channelService;

    private UUID channelId;
    private UUID userId;

    @BeforeEach
    void setup() {
        channelId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    @Test
    void find_shouldReturnChannelFindDTO_whenPublicChannel() {
        Channel channel = new Channel(UUID.randomUUID(), userId, "general");
        ReflectionTestUtils.setField(channel, "channelId", channelId);
        when(channelRepository.find(channelId)).thenReturn(channel);
        when(messageRepository.findAllByChannelId(channelId)).thenReturn(List.of(
                new Message(userId,"general", channelId ,"hi"),
                new Message(userId,"general", channelId, "hello")
        ));

        ChannelFindDTO result = channelService.find(channelId);

        assertNotNull(result);
        assertEquals(channelId, result.channelId());
        assertEquals("general", result.name());
        assertTrue(result.userIdList().isEmpty()); // Public channel일 경우
    }

    @Test
    void find_shouldIncludeUserList_whenPrivateChannel() {
        Channel channel = new Channel(UUID.randomUUID(), userId, null);
        ReflectionTestUtils.setField(channel, "channelId", channelId);
        channel.setType(ChannelType.PRIVATE);
        when(channelRepository.find(channelId)).thenReturn(channel);
        when(messageRepository.findAllByChannelId(channelId)).thenReturn(Collections.emptyList());
        when(readStatusRepository.findAllByChannelId(channelId)).thenReturn(List.of(
                new ReadStatus(userId, channelId, Instant.now())
        ));

        ChannelFindDTO result = channelService.find(channelId);

        assertNotNull(result);
        assertEquals(1, result.userIdList().size());
        assertTrue(result.userIdList().contains(userId));
    }

    @Test
    void findAllByUserId_shouldReturnSubscribedAndPublicChannels() {
        UUID privateChannelId = UUID.randomUUID();
        UUID publicChannelId = UUID.randomUUID();

        Channel privateChannel = new Channel(UUID.randomUUID(), userId, null);
        ReflectionTestUtils.setField(privateChannel, "channelId", privateChannelId);
        privateChannel.setType(ChannelType.PRIVATE);

        Channel publicChannel = new Channel(UUID.randomUUID(), userId, "public");
        ReflectionTestUtils.setField(publicChannel, "channelId", publicChannelId);

        when(readStatusRepository.findAllByUserId(userId)).thenReturn(List.of(
                new ReadStatus(userId, privateChannelId, Instant.now())
        ));

        when(channelRepository.findAll()).thenReturn(List.of(privateChannel, publicChannel));
        when(channelRepository.find(privateChannelId)).thenReturn(privateChannel);
        when(channelRepository.find(publicChannelId)).thenReturn(publicChannel);
        when(messageRepository.findAllByChannelId(any())).thenReturn(Collections.emptyList());
        when(readStatusRepository.findAllByChannelId(any())).thenReturn(Collections.emptyList());

        List<ChannelFindDTO> results = channelService.findAllByUserId(userId);

        assertEquals(2, results.size());
    }
}