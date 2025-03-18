package com.sprint.discodeit.service.file;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.discodeit.domain.StatusType;
import com.sprint.discodeit.domain.dto.userDto.UserNameStatusResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserProfileImgResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserRequestDto;
import com.sprint.discodeit.domain.entity.BinaryContent;
import com.sprint.discodeit.domain.entity.User;
import com.sprint.discodeit.domain.entity.UserStatus;
import com.sprint.discodeit.repository.file.BaseUserStatusRepository;
import com.sprint.discodeit.repository.file.FileUserRepository;
import com.sprint.discodeit.service.util.UserStatusEvaluator;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class FileUserServiceTest {

    @Mock
    private FileUserRepository fileUserRepository; // User 저장소 Mock

    @Mock
    private BaseUserStatusRepository baseUserStatusRepository; // UserStatus 저장소 Mock

    @Mock
    private UserStatusEvaluator userStatusEvaluator; // 상태 판별 서비스 Mock

    @InjectMocks
    private FileUserService userService; //  테스트할 서비스

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //  create() 테스트
    @Test
    void create_ShouldCreateUserAndReturnUserNameStatusResponse() {
        // 가짜 UserRequestDto & ProfileImg DTO 생성
        UserRequestDto userRequestDto = new UserRequestDto("testUser", "test@example.com", "password123");
        UserProfileImgResponseDto profileImgDto = new UserProfileImgResponseDto("https://storage.com/profile.png");

        UUID profileId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        // 가짜 User 및 UserStatus 생성
        User mockUser = new User(userRequestDto.username(), userRequestDto.email(), userRequestDto.password());
        UserStatus mockUserStatus = new UserStatus(Instant.now(), StatusType.Active.toString());
        BinaryContent binaryContent = new BinaryContent("이미지", profileImgDto.imgUrl());
        mockUser.associateStatus(mockUserStatus);
        mockUser.associateProfileId(binaryContent);

        // Mock 리턴값 설정
        when(fileUserRepository.findByUsername(userRequestDto.username())).thenReturn(Optional.empty());
        when(fileUserRepository.findByEmail(userRequestDto.email())).thenReturn(Optional.empty());
        when(userStatusEvaluator.determineUserStatus(any())).thenReturn(StatusType.Active.getExplanation());

        // 실행
        UserNameStatusResponseDto result = userService.create(userRequestDto, profileImgDto);

        // 검증
        assertEquals("testUser", result.name());
        assertEquals(StatusType.Active.getExplanation(), result.status());
//        verify(fileUserRepository, times(1)).save(any(User.class));
//        verify(baseUserStatusRepository, times(1)).save(any(UserStatus.class));
    }

    //  find() 테스트
    @Test
    void find_ShouldReturnUserResponse() {
        UUID userId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        User mockUser = new User("testUser", "test@example.com", "asdefg");
        UserStatus mockUserStatus = new UserStatus(Instant.now(), StatusType.Active.toString());
        mockUser.associateStatus(mockUserStatus);

        // Mock 설정
        when(fileUserRepository.findById(userId.toString())).thenReturn(Optional.of(mockUser));
        when(baseUserStatusRepository.findById(userId.toString())).thenReturn(Optional.of(mockUserStatus));
        when(userStatusEvaluator.determineUserStatus(mockUserStatus.getLastLoginTime())).thenReturn(StatusType.Active.getExplanation());

        // 실행
        var result = userService.find(userId);

        // 검증
        assertEquals("testUser", result.username());
        assertEquals(StatusType.Active.getExplanation(), result.statusTye());
//        verify(fileUserRepository, times(1)).findById(userId.toString());
//        verify(baseUserStatusRepository, times(1)).findById(userId.toString());
    }

//    // findAll() 테스트
//    @Test
//    void findAll_ShouldReturnUserResponses() {
//        UUID userId1 = UUID.randomUUID();
//        UUID userId2 = UUID.randomUUID();
//        UUID statusId1 = UUID.randomUUID();
//        UUID statusId2 = UUID.randomUUID();
//
//        List<User> mockUsers = List.of(
//                new User("Alice", "alice@example.com", UUID.randomUUID()),
//                new User("Bob", "bob@example.com", UUID.randomUUID())
//        );
//
//        Map<UUID, UserStatus> mockStatuses = Map.of(
//                statusId1, new UserStatus(statusId1, userId1, Instant.now().minusSeconds(200)),
//                statusId2, new UserStatus(statusId2, userId2, Instant.now().minusSeconds(1000))
//        );
//
//        // Mock 설정
//        when(fileUserRepository.findByAll()).thenReturn(mockUsers);
//        when(baseUserStatusRepository.findByAllAndUser(mockUsers)).thenReturn(mockStatuses);
//        when(userStatusEvaluator.evaluateUserStatus(any())).thenReturn("Active", "Away");
//
//        // 실행
//        var result = userService.findAll();
//
//        // 검증
//        assertEquals(2, result.size());
//        assertEquals("Active", result.get(0).status());
//        assertEquals("Away", result.get(1).status());
//        verify(fileUserRepository, times(1)).findByAll();
//        verify(baseUserStatusRepository, times(1)).findByAllAndUser(mockUsers);
//    }
//
//    // delete() 테스트
//    @Test
//    void delete_ShouldRemoveUserAndRelatedData() {
//        UUID userId = UUID.randomUUID();
//
//        // Mock 설정
//        doNothing().when(fileUserRepository).delete(userId);
//        doNothing().when(baseUserStatusRepository).delete(userId);
//
//        // 실행
//        userService.delete(userId);
//
//        // 검증
//        verify(fileUserRepository, times(1)).delete(userId);
//        verify(baseUserStatusRepository, times(1)).delete(userId);
//    }

}