package com.sprint.discodeit.service.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import com.sprint.discodeit.sprint.domain.dto.usersDto.usersNameStatusResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.usersProfileImgResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.usersRequestDto;
import com.sprint.discodeit.sprint.service.basic.users.BasicUsersService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicUsersServiceTest {

    @Mock
    private BasicUsersService basicusersService;

    private UUID usersId;
    private usersRequestDto usersRequestDto;
    private usersProfileImgResponseDto usersProfileImgResponseDto;
    private usersNameStatusResponseDto usersNameStatusResponseDto;

    @BeforeEach
    void setUp() {
//       usersRequestDto = new usersRequestDto("testusers", "test@example.com", "password123");
//       usersProfileImgResponseDto = new usersProfileImgResponseDto("https://your-storage.com/default-profile.png");
       usersId = UUID.randomUUID();

       usersNameStatusResponseDto = new usersNameStatusResponseDto("testusers", "ACTIVE", usersId);

       when(basicusersService.create(usersRequestDto, usersProfileImgResponseDto)).thenReturn(usersNameStatusResponseDto);
    }


    @Test
    @DisplayName("사용자 생성: 유효한 정보를 입력하면 새로운 사용자가 생성되고 상태는 ACTIVE여야 한다.")
    void createusersTest() {
        // ✅ When: fileusersService.create() 호출
        usersNameStatusResponseDto response = basicusersService.create(usersRequestDto, usersProfileImgResponseDto);

        // ✅ Then: 결과 검증
        assertNotNull(response, "응답이 null이면 안 된다.");
        assertEquals(usersId, response.id(), "반환된 ID가 예상과 일치해야 한다.");
        assertEquals("testusers", response.name(), "사용자 이름이 'testusers'여야 한다.");
        assertEquals("ACTIVE", response.status(), "사용자 상태는 'ACTIVE'여야 한다.");

        // ✅ fileusersService.create()가 한 번 호출되었는지 검증
        verify(basicusersService, times(1)).create(usersRequestDto, usersProfileImgResponseDto);
    }

    @Test
    @DisplayName("사용자 생서 시 응답이 null이 아니어야 한다.")
    void createusersNullTest() {
        //given

        usersNameStatusResponseDto response = basicusersService.create(usersRequestDto,usersProfileImgResponseDto);

        //when

        when(basicusersService.create(usersRequestDto, usersProfileImgResponseDto)).thenReturn(usersNameStatusResponseDto);

        //then
        assertNotNull(response, "응답이 null이면 안 된다.");

    }

    @Test
    @DisplayName("사용자 생성 실패: 필수 값이 없을 때 예외가 발생해야 한다.")
    void createusersThrowsExceptionTest() {
        // Given
        usersRequestDto requestDto = null; // 잘못된 입력값 (null)
        usersProfileImgResponseDto profileImgDto = null; // 잘못된 입력값 (null)

        // `create()` 호출 시 예외 발생하도록 설정
        when(basicusersService.create(requestDto, profileImgDto))
                .thenThrow(new IllegalArgumentException("사용자 정보가 올바르지 않습니다."));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            basicusersService.create(requestDto, profileImgDto);
        }, "사용자 정보가 올바르지 않을 때 예외가 발생해야 한다.");
    }

    @Test
    @DisplayName("사용자 생성 실패: 필수 값이 없을 때 실제 서비스에서 예외가 발생해야 한다.")
    void createusersThrowsExceptionRealServiceTest() {

        // Given

        usersRequestDto requestDto = null;
        usersProfileImgResponseDto profileImgDto = null;

//        fileusersService.create(requestDto, profileImgDto); // ✅ 실제 서비스 객체 사용

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            basicusersService.create(requestDto, profileImgDto); // ✅ 실제 서비스 호출
        }, "사용자 정보가 없으면 실제 서비스에서도 예외가 발생해야 한다.");
    }



}