package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private BasicChannelService basicChannelService;

    @Test
    @DisplayName("readStatusRepository 의 findByUserId 메서드 테스트")
    void findByUser() {
        UUID userId = UUID.randomUUID();
        UUID channelId1 = UUID.randomUUID();
        UUID channelId2 = UUID.randomUUID();

        Instant now = Instant.now();
        ReadStatus readStatus1 = new ReadStatus(channelId1, userId, now);
        ReadStatus readStatus2 = new ReadStatus(channelId2, userId, now);

        List<ReadStatus> readStatusList = List.of(readStatus1, readStatus2);
        when(readStatusRepository.findAllByUserId(userId)).thenReturn(readStatusList);

        // when
        List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId)
            .stream()
            .map(ReadStatus::getChannelId)
            .toList();

        // then
        assertEquals(2, mySubscribedChannelIds.size());
        assertTrue(mySubscribedChannelIds.contains(channelId1));
        assertTrue(mySubscribedChannelIds.contains(channelId2));
    }
}
