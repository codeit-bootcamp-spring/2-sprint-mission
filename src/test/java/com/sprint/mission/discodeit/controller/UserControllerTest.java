package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.core.user.controller.UserController;
import com.sprint.mission.discodeit.core.user.usecase.BasicUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private BasicUserService userService;

  @Test
  void CreateUser_Success() {
    // given

    // when

    // then

  }
}
