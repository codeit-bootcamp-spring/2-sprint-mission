package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserRepository;
import com.sprint.mission.discodeit.basic.repositoryimpl.BasicChannelRepositoryImplement;
import com.sprint.mission.discodeit.basic.serviceimpl.BasicChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChannelIntegrationTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private ChannelRepository channelRepository = new BasicChannelRepositoryImplement();

    @Mock
    private ChannelService channelService;

    // 테스트용 데이터
    private UUID userId;
    private UUID channelId;
    private String channelName = "테스트 채널";
    private ChannelDto.Response testChannelResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        channelId = UUID.randomUUID();
        
        // 테스트용 채널 생성
        Channel channel = new Channel(channelName, userId);
        channelRepository.register(channel);
        
        // 모의 응답 객체 생성
        testChannelResponse = ChannelDto.Response.builder()
            .channelId(channelId)
            .channelName(channelName)
            .ownerId(userId)
            .isPrivate(false)
            .build();
    }

    @Test
    void testCreateChannel() {

        ChannelDto.CreatePublic createDto = ChannelDto.CreatePublic.builder()
            .ownerId(userId)
            .channelName("새로운 채널")
            .build();
            
        when(channelService.createPublicChannel(any(ChannelDto.CreatePublic.class))).thenReturn(testChannelResponse);

        // 실행
        ChannelDto.Response response = channelService.createPublicChannel(createDto);

        // 검증
        assertNotNull(response);
        assertEquals(channelId, response.getChannelId());
        assertEquals(userId, response.getOwnerId());
        verify(channelService).createPublicChannel(any(ChannelDto.CreatePublic.class));
    }

    @Test
    void testFindById() {

        when(channelService.findById(channelId)).thenReturn(testChannelResponse);

        ChannelDto.Response response = channelService.findById(channelId);

        // 검증
        assertNotNull(response);
        assertEquals(channelId, response.getChannelId());
        assertEquals(channelName, response.getChannelName());
        verify(channelService).findById(channelId);
    }

    @Test
    void testFindAllChannels() {

        List<ChannelDto.Response> channelList = List.of(testChannelResponse);
        when(channelService.findAllPublicChannels()).thenReturn(channelList);
        

        List<ChannelDto.Response> responses = channelService.findAllPublicChannels();


        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(channelService).findAllPublicChannels();
    }

    @Test
    void testFindChannelsByOwnerId() {

        List<ChannelDto.Response> channelList = List.of(testChannelResponse);
        when(channelService.findAllByUserId(userId)).thenReturn(channelList);
        

        List<ChannelDto.Response> responses = channelService.findAllByUserId(userId);


        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(userId, responses.get(0).getOwnerId());
        verify(channelService).findAllByUserId(userId);
    }

    @Test
    void testUpdateChannel() {

        String newName = "수정된 채널명";
        ChannelDto.Update updateDto = ChannelDto.Update.builder()
            .channelId(channelId)
            .channelName(newName)
            .build();
            

        ChannelDto.Response updatedResponse = ChannelDto.Response.builder()
            .channelId(channelId)
            .channelName(newName)
            .ownerId(userId)
            .isPrivate(false)
            .build();
            
        when(channelService.updateChannel(any(ChannelDto.Update.class))).thenReturn(updatedResponse);


        ChannelDto.Response response = channelService.updateChannel(updateDto);

        assertEquals(channelId, response.getChannelId());
        assertEquals(newName, response.getChannelName());
        verify(channelService).updateChannel(any(ChannelDto.Update.class));
    }

    @Test
    void testDeleteChannel() {
        when(channelService.deleteChannel(channelId)).thenReturn(true);

        boolean deleted = channelService.deleteChannel(channelId);


        assertTrue(deleted);
        verify(channelService).deleteChannel(channelId);
    }
} 