package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.StatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserRepository;
import com.sprint.mission.discodeit.service.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.basic.repositoryimpl.UserStatusRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserStatusIntegrationTest {

    @Spy
    private UserStatusRepository userStatusRepository = new UserStatusRepositoryImpl();

    @Mock
    private UserStatusService userStatusService;

    private UUID userId;
    private UUID userStatusId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userStatusId = UUID.randomUUID();

        UserStatus status = new UserStatus(userId);
        userStatusRepository.register(status);
    }

    @Test
    void testCreateUserStatus() {
        StatusDto.Create createDto = StatusDto.Create.builder()
            .userid(userId)
            .build();
            
        StatusDto.Summary mockResponse = StatusDto.Summary.builder()
            .id(userStatusId)
            .userid(userId)
            .lastSeenAt(ZonedDateTime.now())
            .updatedAt(ZonedDateTime.now())
            .build();
            
        when(userStatusService.createUserStatus(any(StatusDto.Create.class))).thenReturn(mockResponse);

        StatusDto.Summary response = userStatusService.createUserStatus(createDto);

        assertNotNull(response);
        assertEquals(userId, response.getUserid());
        verify(userStatusService).createUserStatus(any(StatusDto.Create.class));
    }

    @Test
    void testFindUserStatusById() {
        StatusDto.Summary mockResponse = StatusDto.Summary.builder()
            .id(userStatusId)
            .userid(userId)
            .lastSeenAt(ZonedDateTime.now())
            .updatedAt(ZonedDateTime.now())
            .build();
            
        when(userStatusService.findById(userStatusId)).thenReturn(mockResponse);
        
        StatusDto.Summary foundStatus = userStatusService.findById(userStatusId);

        assertNotNull(foundStatus);
        assertEquals(userStatusId, foundStatus.getId());
        assertEquals(userId, foundStatus.getUserid());
        verify(userStatusService).findById(userStatusId);
    }

    @Test
    void testFindAllUserStatuses() {
        StatusDto.Summary mockStatus1 = StatusDto.Summary.builder()
            .id(userStatusId)
            .userid(userId)
            .lastSeenAt(ZonedDateTime.now())
            .updatedAt(ZonedDateTime.now())
            .build();
            
        StatusDto.Summary mockStatus2 = StatusDto.Summary.builder()
            .id(UUID.randomUUID())
            .userid(UUID.randomUUID())
            .lastSeenAt(ZonedDateTime.now())
            .updatedAt(ZonedDateTime.now())
            .build();
            
        List<StatusDto.Summary> mockStatuses = List.of(mockStatus1, mockStatus2);
        when(userStatusService.findAllUsers()).thenReturn(mockStatuses);

        List<StatusDto.Summary> statuses = userStatusService.findAllUsers();

        assertNotNull(statuses);
        assertEquals(2, statuses.size());
    }

    @Test
    void testUpdateUserStatus() {
        StatusDto.Summary currentStatus = StatusDto.Summary.builder()
            .id(userStatusId)
            .userid(userId)
            .lastSeenAt(ZonedDateTime.now())
            .updatedAt(ZonedDateTime.now())
            .build();
        
        StatusDto.Summary updatedStatus = StatusDto.Summary.builder()
            .id(userStatusId)
            .userid(userId)
            .lastSeenAt(ZonedDateTime.now())
            .updatedAt(ZonedDateTime.now())
            .build();
        
        when(userStatusService.update(any(StatusDto.Summary.class))).thenReturn(updatedStatus);

        StatusDto.Summary response = userStatusService.update(currentStatus);

        assertNotNull(response);
        assertEquals(userStatusId, response.getId());
        assertEquals(userId, response.getUserid());
        verify(userStatusService).update(any(StatusDto.Summary.class));
    }

    @Test
    void testDeleteUserStatus() {
        StatusDto.ResponseDelete mockResponse = StatusDto.ResponseDelete.builder()
            .id(userStatusId)
            .message("성공적으로 삭제되었습니다")
            .build();
            
        when(userStatusService.deleteUserStatus(userStatusId)).thenReturn(mockResponse);
        
        StatusDto.ResponseDelete response = userStatusService.deleteUserStatus(userStatusId);

        assertNotNull(response);
        assertEquals(userStatusId, response.getId());
        verify(userStatusService).deleteUserStatus(userStatusId);
    }
} 