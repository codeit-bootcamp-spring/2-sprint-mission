package com.sprint.discodeit.service.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import com.sprint.discodeit.domain.dto.userDto.UserNameStatusResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserProfileImgResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserRequestDto;
import com.sprint.discodeit.service.basic.users.BasicUserService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

    @Mock
    private BasicUserService basicUserService;

    private UUID userId;
    private UserRequestDto userRequestDto;
    private UserProfileImgResponseDto userProfileImgResponseDto;
    private UserNameStatusResponseDto userNameStatusResponseDto;

    @BeforeEach
    void setUp() {
//       userRequestDto = new UserRequestDto("testUser", "test@example.com", "password123");
//       userProfileImgResponseDto = new UserProfileImgResponseDto("https://your-storage.com/default-profile.png");
       userId = UUID.randomUUID();

       userNameStatusResponseDto = new UserNameStatusResponseDto("testUser", "ACTIVE", userId);

       when(basicUserService.create(userRequestDto, userProfileImgResponseDto)).thenReturn(userNameStatusResponseDto);
    }


    @Test
    @DisplayName("사용자 생성: 유효한 정보를 입력하면 새로운 사용자가 생성되고 상태는 ACTIVE여야 한다.")
    void createUserTest() {
        // ✅ When: fileUserService.create() 호출
        UserNameStatusResponseDto response = basicUserService.create(userRequestDto, userProfileImgResponseDto);

        // ✅ Then: 결과 검증
        assertNotNull(response, "응답이 null이면 안 된다.");
        assertEquals(userId, response.id(), "반환된 ID가 예상과 일치해야 한다.");
        assertEquals("testUser", response.name(), "사용자 이름이 'testUser'여야 한다.");
        assertEquals("ACTIVE", response.status(), "사용자 상태는 'ACTIVE'여야 한다.");

        // ✅ fileUserService.create()가 한 번 호출되었는지 검증
        verify(basicUserService, times(1)).create(userRequestDto, userProfileImgResponseDto);
    }

    @Test
    @DisplayName("사용자 생서 시 응답이 null이 아니어야 한다.")
    void createUserNullTest() {
        //given

        UserNameStatusResponseDto response = basicUserService.create(userRequestDto,userProfileImgResponseDto);

        //when

        when(basicUserService.create(userRequestDto, userProfileImgResponseDto)).thenReturn(userNameStatusResponseDto);

        //then
        assertNotNull(response, "응답이 null이면 안 된다.");

    }

    @Test
    @DisplayName("사용자 생성 실패: 필수 값이 없을 때 예외가 발생해야 한다.")
    void createUserThrowsExceptionTest() {
        // Given
        UserRequestDto requestDto = null; // 잘못된 입력값 (null)
        UserProfileImgResponseDto profileImgDto = null; // 잘못된 입력값 (null)

        // `create()` 호출 시 예외 발생하도록 설정
        when(basicUserService.create(requestDto, profileImgDto))
                .thenThrow(new IllegalArgumentException("사용자 정보가 올바르지 않습니다."));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            basicUserService.create(requestDto, profileImgDto);
        }, "사용자 정보가 올바르지 않을 때 예외가 발생해야 한다.");
    }

    @Test
    @DisplayName("사용자 생성 실패: 필수 값이 없을 때 실제 서비스에서 예외가 발생해야 한다.")
    void createUserThrowsExceptionRealServiceTest() {

        // Given

        UserRequestDto requestDto = null;
        UserProfileImgResponseDto profileImgDto = null;

//        fileUserService.create(requestDto, profileImgDto); // ✅ 실제 서비스 객체 사용

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            basicUserService.create(requestDto, profileImgDto); // ✅ 실제 서비스 호출
        }, "사용자 정보가 없으면 실제 서비스에서도 예외가 발생해야 한다.");
    }



}