package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.TestMultipartUtil;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserStatusService userStatusService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto userDto;
    private UserDto userDto2;
    private List<UserDto> userDtoList;
    private UserCreateRequest userCreateRequest;
    private UserUpdateRequest userUpdateRequest;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(UUID.randomUUID(), "user", "user@test.com", null, true);
        userDto2 = new UserDto(UUID.randomUUID(), "user2", "user2@test.com", null, true);
        userDtoList = Arrays.asList(userDto, userDto2);
    }

    @Test
    void 사용자_생성_테스트() throws Exception {
        userCreateRequest = new UserCreateRequest("user", "abcd1234@@",
            "user@test.com");

        MockMultipartFile userJson = TestMultipartUtil.jsonPart("userCreateRequest",
            userCreateRequest);

        MockMultipartFile profile = new MockMultipartFile(
            "profile", "test.png", "image/png", "image-bytes".getBytes()
        );
        given(userService.save(any(), any())).willReturn(userDto);

        mockMvc.perform(multipart("/api/users")
                .file(userJson)
                .file(profile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    void 사용자_생성_비밀번호_검증_실패_테스트() throws Exception {
        userCreateRequest = new UserCreateRequest("user", "abcd",
            "user@test.com");

        MockMultipartFile userJson = TestMultipartUtil.jsonPart("userCreateRequest",
            userCreateRequest);

        mockMvc.perform(multipart("/api/users")
                .file(userJson))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    void 사용자_수정_테스트() throws Exception {
        UUID userId = UUID.randomUUID();
        userUpdateRequest = new UserUpdateRequest("user", "abcd1234@@",
            "user@test.com");

        MockMultipartFile userJson = TestMultipartUtil.jsonPart("userUpdateRequest",
            userUpdateRequest);

        given(userService.update(userId, userUpdateRequest, null)).willReturn(userDto);

        mockMvc.perform(multipart("/api/users/{userId}", userId)
                .file(userJson)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(request -> {
                    request.setMethod("PATCH");
                    return request;
                })
            )
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void 사용자_수정_이메일_형식_실패_테스트() throws Exception {
        UUID userId = UUID.randomUUID();
        userUpdateRequest = new UserUpdateRequest("user", "abcd1234@@",
            "user");
        MockMultipartFile userJson = TestMultipartUtil.jsonPart("userUpdateRequest",
            userUpdateRequest);
        given(userService.update(userId, userUpdateRequest, null)).willReturn(userDto);

        mockMvc.perform(multipart("/api/users/{userId}", userId)
                .file(userJson)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(request -> {
                    request.setMethod("PATCH");
                    return request;
                }))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    void 사용자_삭제_테스트() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(delete("/api/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    void 사용자_삭제_시_사용자_없음_테스트() throws Exception {
        UUID userId = UUID.randomUUID();

        doThrow(UserNotFoundException.forId(userId.toString()))
            .when(userService).delete(userId);

        mockMvc.perform(delete("/api/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    void 사용자_목록_조회() throws Exception {
        given(userService.findAllUser()).willReturn(userDtoList);

        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }


}