package com.sprint.mission.discodeit.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.core.auth.controller.AuthController;
import com.sprint.mission.discodeit.core.auth.entity.CustomUserDetails;
import com.sprint.mission.discodeit.core.auth.service.CustomUserDetailsService;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.entity.Role;
import com.sprint.mission.discodeit.security.config.SecurityConfig;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@Disabled
class AuthControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  CustomUserDetailsService userDetailsService;

  @MockitoBean
  BCryptPasswordEncoder passwordEncoder;

  @Test
  void getCsrfToken() throws Exception {
    mockMvc.perform(get("/api/auth/csrf-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").isNotEmpty())
        .andExpect(jsonPath("$.parameterName").value("_csrf"))
        .andExpect(jsonPath("$.headerName").value("X-CSRF-TOKEN"));
  }

  @Test
  void me() throws Exception {
    // 테스트용 UserDto
    UserDto dto = new UserDto(
        UUID.randomUUID(),
        "alice",
        "alice@example.com",
        null,
        Role.USER,
        false
    );
    // CustomUserDetails를 스프링 시큐리티에 등록
    CustomUserDetails principal = new CustomUserDetails(dto, "irrelevant");

    mockMvc.perform(get("/api/auth/me")
            .with(user(principal))              // 인증 컨텍스트에 CustomUserDetails 주입
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        // body.userDto.username == "alice"
        .andExpect(jsonPath("$.username").value("alice"))
        .andExpect(jsonPath("$.email").value("alice@example.com"));
  }
}