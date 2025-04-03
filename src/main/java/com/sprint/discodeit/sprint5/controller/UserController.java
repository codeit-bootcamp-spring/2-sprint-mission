package com.sprint.discodeit.sprint5.controller;

import com.sprint.discodeit.sprint5.domain.StatusType;
import com.sprint.discodeit.sprint5.domain.dto.userDto.UserLoginRequestDto;
import com.sprint.discodeit.sprint5.domain.dto.userDto.UserLoginResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.userDto.UserNameStatusResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.userDto.UserProfileImgResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.userDto.UserRequestDto;
import com.sprint.discodeit.sprint5.domain.dto.userDto.UserResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.userDto.UserUpdateRequestDto;
import com.sprint.discodeit.sprint5.domain.entity.User;
import com.sprint.discodeit.sprint5.service.basic.users.UserServiceV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "유저 관련 API입니다.")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceV1 basicUserService;

    @Operation(summary = "회원가입", description = "회원 정보를 입력받아 회원가입을 진행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 오류")
    })
    @PostMapping("/create")
    public ResponseEntity<UserNameStatusResponseDto> signup(
            @RequestBody UserRequestDto userRequestDto,
            @Parameter(description = "프로필 이미지 정보") UserProfileImgResponseDto userProfileImgResponseDto) {
        UserNameStatusResponseDto userNameStatusResponseDto = basicUserService.create(userRequestDto, userProfileImgResponseDto);
        return ResponseEntity.ok(userNameStatusResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        UserLoginResponseDto login = basicUserService.login(userLoginRequestDto);
        return ResponseEntity.ok(login);
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음")
    })
    @PutMapping("/update/{userId}")
    public ResponseEntity<UserResponseDto> update(
            @Parameter(description = "수정할 회원의 UUID") @PathVariable String userId,
            @RequestBody UserUpdateRequestDto updateRequestDto) {
        UserResponseDto userResponseDto = basicUserService.update(updateRequestDto, userId);
        return ResponseEntity.ok(userResponseDto);
    }

    @Operation(summary = "회원 단건 조회", description = "UUID를 기반으로 회원 정보를 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> get(
            @Parameter(description = "조회할 회원의 UUID") @PathVariable String userId) {
        UserResponseDto userResponseDto = basicUserService.find(UUID.fromString(userId));
        return ResponseEntity.ok(userResponseDto);
    }

    @Operation(summary = "모든 회원 조회", description = "가입된 모든 회원을 조회합니다.")
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAll() {
        List<User> all = basicUserService.findAll();
        return ResponseEntity.ok(all);
    }

    @Operation(summary = "회원 비활성화", description = "UUID를 기반으로 회원 상태를 Inactive로 변경합니다.")
    @PatchMapping("/{userId}/status")
    public ResponseEntity<String> delet(
            @Parameter(description = "비활성화할 회원의 UUID") @PathVariable String userId) {
        basicUserService.delete(UUID.fromString(userId));
        return ResponseEntity.ok(StatusType.Inactive.getExplanation());
    }
}
