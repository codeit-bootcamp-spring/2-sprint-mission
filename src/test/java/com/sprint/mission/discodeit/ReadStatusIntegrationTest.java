package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.basic.repositoryimpl.BasicReadStatusRepositoryImplement;
import com.sprint.mission.discodeit.basic.serviceimpl.BasicReadStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Disabled;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReadStatusIntegrationTest {

    @Spy
    private ReadStatusRepository readStatusRepository = new BasicReadStatusRepositoryImplement();

    @Mock
    private ReadStatusService readStatusService;

    private UUID userId;
    private UUID channelId;
    private UUID messageId;
    private UUID readStatusId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        channelId = UUID.randomUUID();
        messageId = UUID.randomUUID();
        
        ReadStatus readStatus = new ReadStatus(channelId, userId, messageId);
        readStatusRepository.register(readStatus);
        readStatusId = readStatus.getId();
    }

    @Test
    void testCreateReadStatus() {
        ReadStatusDto.Create createDto = new ReadStatusDto.Create();
        createDto.setChannelId(channelId);
        createDto.setUserId(userId);
        createDto.setLastReadMessageId(messageId);
        
        ReadStatusDto.ResponseReadStatus mockResponse = new ReadStatusDto.ResponseReadStatus();
        mockResponse.setId(readStatusId);
        mockResponse.setChannelId(channelId);
        mockResponse.setUserId(userId);
        mockResponse.setLastReadMessageId(messageId);
            
        when(readStatusService.create(any(ReadStatusDto.Create.class))).thenReturn(mockResponse);

        ReadStatusDto.ResponseReadStatus response = readStatusService.create(createDto);

        assertNotNull(response);
        assertEquals(readStatusId, response.getId());
        verify(readStatusService).create(any(ReadStatusDto.Create.class));
    }

    @Test
    void testFindById() {
        ReadStatusDto.ResponseReadStatus mockResponse = new ReadStatusDto.ResponseReadStatus();
        mockResponse.setId(readStatusId);
        mockResponse.setChannelId(channelId);
        mockResponse.setUserId(userId);
        mockResponse.setLastReadMessageId(messageId);
            
        when(readStatusService.find(readStatusId)).thenReturn(mockResponse);
        
        ReadStatusDto.ResponseReadStatus response = readStatusService.find(readStatusId);

        assertNotNull(response);
        assertEquals(readStatusId, response.getId());
        verify(readStatusService).find(readStatusId);
    }

    @Test
    void testFindAllByUserId() {
        ReadStatusDto.ResponseReadStatus mockResponse = new ReadStatusDto.ResponseReadStatus();
        mockResponse.setId(readStatusId);
        mockResponse.setChannelId(channelId);
        mockResponse.setUserId(userId);
        mockResponse.setLastReadMessageId(messageId);
            
        List<ReadStatusDto.ResponseReadStatus> responseList = List.of(mockResponse);
        when(readStatusService.findAllByUserId(userId)).thenReturn(responseList);
        
        List<ReadStatusDto.ResponseReadStatus> responses = readStatusService.findAllByUserId(userId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(readStatusService).findAllByUserId(userId);
    }

    @Test
    void testFindByUserIdAndChannelId() {
        ReadStatusDto.ResponseReadStatus mockResponse = new ReadStatusDto.ResponseReadStatus();
        mockResponse.setId(readStatusId);
        mockResponse.setChannelId(channelId);
        mockResponse.setUserId(userId);
        mockResponse.setLastReadMessageId(messageId);
            
        when(readStatusService.findByUserIdAndChannelId(userId, channelId)).thenReturn(mockResponse);
        
        ReadStatusDto.ResponseReadStatus response = readStatusService.findByUserIdAndChannelId(userId, channelId);

        assertNotNull(response);
        assertEquals(readStatusId, response.getId());
        verify(readStatusService).findByUserIdAndChannelId(userId, channelId);
    }

    @Test
    void testUpdateReadStatus() {
        UUID newMessageId = UUID.randomUUID();
        ReadStatusDto.Update updateDto = new ReadStatusDto.Update();
        updateDto.setId(readStatusId);
        updateDto.setLastReadMessageId(newMessageId);
            
        ReadStatusDto.ResponseReadStatus updatedResponse = new ReadStatusDto.ResponseReadStatus();
        updatedResponse.setId(readStatusId);
        updatedResponse.setChannelId(channelId);
        updatedResponse.setUserId(userId);
        updatedResponse.setLastReadMessageId(newMessageId);
            
        when(readStatusService.update(any(ReadStatusDto.Update.class))).thenReturn(updatedResponse);

        ReadStatusDto.ResponseReadStatus response = readStatusService.update(updateDto);

        assertNotNull(response);
        assertEquals(readStatusId, response.getId());
        assertEquals(newMessageId, response.getLastReadMessageId());
        verify(readStatusService).update(any(ReadStatusDto.Update.class));
    }

    @Test
    void testDeleteReadStatus() {
        when(readStatusService.delete(readStatusId)).thenReturn(true);
        
        boolean result = readStatusService.delete(readStatusId);

        assertTrue(result);
        verify(readStatusService).delete(readStatusId);
    }

    @Test
    void testDeleteAllByChannelId() {
        when(readStatusService.deleteAllByChannelId(channelId)).thenReturn(true);
        
        boolean result = readStatusService.deleteAllByChannelId(channelId);

        assertTrue(result);
        verify(readStatusService).deleteAllByChannelId(channelId);
    }

    @Test
    void testDeleteAllByUserId() {
        when(readStatusService.deleteAllByUserId(userId)).thenReturn(true);
        boolean result = readStatusService.deleteAllByUserId(userId);

        assertTrue(result);
        verify(readStatusService).deleteAllByUserId(userId);
    }
} 