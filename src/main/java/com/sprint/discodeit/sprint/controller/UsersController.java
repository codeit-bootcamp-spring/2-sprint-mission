package com.sprint.discodeit.sprint.controller;

import com.sprint.discodeit.sprint.domain.StatusType;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersLoginRequestDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersLoginResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersNameStatusResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersProfileImgResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersRequestDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.entity.Users;
import com.sprint.discodeit.sprint.service.basic.users.UsersService;
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

@Tag(name = "Users API", description = "유저 관련 API입니다.")
@RestController
@RequestMapping("/api/userss")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @Operation(summary = "회원가입", description = "회원 정보를 입력받아 회원가입을 진행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 오류")
    })
    @PostMapping("/create")
    public ResponseEntity<UsersNameStatusResponseDto> signup(
            @RequestBody UsersRequestDto usersRequestDto,
            @Parameter(description = "프로필 이미지 정보") UsersProfileImgResponseDto usersProfileImgResponseDto) {
        UsersNameStatusResponseDto usersNameStatusResponseDto = usersService.create(usersRequestDto, usersProfileImgResponseDto);
        return ResponseEntity.ok(usersNameStatusResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UsersLoginResponseDto> login(@RequestBody UsersLoginRequestDto usersLoginRequestDto) {
        UsersLoginResponseDto login = usersService.login(usersLoginRequestDto);
        return ResponseEntity.ok(login);
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음")
    })
    @PutMapping("/update/{usersId}")
    public ResponseEntity<UsersResponseDto> update(
            @Parameter(description = "수정할 회원의 UUID") @PathVariable Long usersId,
            @RequestBody UsersUpdateRequestDto updateRequestDto) {
        UsersResponseDto usersResponseDto = usersService.update(updateRequestDto, usersId);
        return ResponseEntity.ok(usersResponseDto);
    }

    @Operation(summary = "회원 단건 조회", description = "UUID를 기반으로 회원 정보를 조회합니다.")
    @GetMapping("/{usersId}")
    public ResponseEntity<UsersResponseDto> get(
            @Parameter(description = "조회할 회원의 UUID") @PathVariable Long usersId) {
        UsersResponseDto usersResponseDto = usersService.find(usersId);
        return ResponseEntity.ok(usersResponseDto);
    }

    @Operation(summary = "모든 회원 조회", description = "가입된 모든 회원을 조회합니다.")
    @GetMapping("/getAll")
    public ResponseEntity<List<Long>> getAll() {
        List<Long> all = usersService.findAll().id();
        return ResponseEntity.ok(all);
    }

    @Operation(summary = "회원 비활성화", description = "UUID를 기반으로 회원 상태를 Inactive로 변경합니다.")
    @PatchMapping("/{usersId}/status")
    public ResponseEntity<String> delet(
            @Parameter(description = "비활성화할 회원의 UUID") @PathVariable Long usersId) {
        usersService.delete(usersId);
        return ResponseEntity.ok(StatusType.Inactive.getExplanation());
    }
}
