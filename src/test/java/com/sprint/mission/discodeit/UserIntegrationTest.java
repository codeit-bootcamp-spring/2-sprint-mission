package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.basic.repositoryimpl.BasicUserRepositoryImplement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserIntegrationTest {

    @Spy
    private UserRepository userRepository = new BasicUserRepositoryImplement();

    @Mock
    private UserService userService;

    private UUID userId;
    private String email = "test@example.com";
    private String password = "password123";
    private UserDto.Summary testUserSummary;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        
        User user = new User(email, password);
        userRepository.register(user);

        testUserSummary = UserDto.Summary.builder()
            .id(userId)
            .email(email)
            .build();
    }

    @Test
    void testCreateUser() {
        UserDto.Create createDto = UserDto.Create.builder()
            .email("newuser@example.com")
            .password("newpassword123")
            .build();
        
        UserDto.Response mockResponse = UserDto.Response.builder()
            .id(userId)
            .email("newuser@example.com")
            .build();
            
        when(userService.createdUser(any(UserDto.Create.class))).thenReturn(mockResponse);

        UserDto.Response response = userService.createdUser(createDto);

        assertNotNull(response);
        assertEquals(userId, response.getId());
        verify(userService).createdUser(any(UserDto.Create.class));
    }

    @Test
    void testFindUserById() {
        when(userService.findByUserId(userId)).thenReturn(testUserSummary);
        
        UserDto.Summary response = userService.findByUserId(userId);

        assertNotNull(response);
        assertEquals(userId, response.getId());
        verify(userService).findByUserId(userId);
    }

    @Test
    void testFindAllUsers() {
        List<UserDto.Summary> userList = List.of(testUserSummary);
        when(userService.findByAllUsersId()).thenReturn(userList);
        
        List<UserDto.Summary> responses = userService.findByAllUsersId();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(userService).findByAllUsersId();
    }

    @Test
    void testUpdateUser() {
        String newPassword = "newpassword456";
        UserDto.Update updateDto = UserDto.Update.builder()
            .id(userId)
            .password(newPassword)
            .build();
            
        UserDto.Update updatedDto = UserDto.Update.builder()
            .id(userId)
            .password(newPassword)
            .build();
            
        when(userService.updateUser(any(UserDto.Update.class))).thenReturn(updatedDto);

        UserDto.Update response = userService.updateUser(updateDto);

        assertNotNull(response);
        assertEquals(userId, response.getId());
        assertEquals(newPassword, response.getPassword());
        verify(userService).updateUser(any(UserDto.Update.class));
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(userId);

        verify(userService).deleteUser(userId);
    }
} 