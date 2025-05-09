package com.sprint.mission.discodeit.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class) // 예외 매핑 클래스 수동 등록
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  void user_find_success() throws Exception {
    UUID id = UUID.randomUUID();
    UserDto userDto = new UserDto(id, "john", "john@email.com", null, true);

    given(userService.find(id)).willReturn(userDto);

    mockMvc.perform(get("/api/users/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("john"));
  }

  @Test
  void user_find_fail() throws Exception {
    UUID id = UUID.randomUUID();
    given(userService.find(id)).willThrow(new UserNotFoundException(id));

    mockMvc.perform(get("/api/users/" + id))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));
  }
}

