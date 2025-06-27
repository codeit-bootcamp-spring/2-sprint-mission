package com.sprint.mission.discodeit.security.filter;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.core.user.entity.Role;
import com.sprint.mission.discodeit.core.user.dto.response.UserDto;
import com.sprint.mission.discodeit.core.auth.dto.LoginRequest;
import com.sprint.mission.discodeit.core.auth.entity.CustomUserDetails;
import com.sprint.mission.discodeit.core.auth.service.CustomUserDetailsService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class JsonUsernamePasswordAuthenticationFilterMVCTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  CustomUserDetailsService userDetailsService;

  @MockitoBean
  PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    UserDto userDto = new UserDto(UUID.randomUUID(), "testUser", "testEmail@test.com", null,
        Role.USER, false);
    CustomUserDetails custom = new CustomUserDetails(userDto, "encodePwd");
    when(userDetailsService.loadUserByUsername("testUser"))
        .thenReturn(custom);

    // 2) 비밀번호 매칭은 항상 true
    when(passwordEncoder.matches("password", custom.getPassword()))
        .thenReturn(true);
  }

  @Test
  void attemptAuthentication() throws Exception {
    LoginRequest loginRequest = new LoginRequest("testUser", "password");

    mockMvc.perform(post("/api/auth/login")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(cookie().exists("JSESSIONID"))
        .andDo(print());
  }
}
