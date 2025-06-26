package com.sprint.mission.discodeit.security.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class JsonUsernamePasswordAuthenticationFilterTest {

  @Mock
  AuthenticationManager authenticationManager;

  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void attemptAuthentication() throws Exception {
    JsonUsernamePasswordAuthenticationFilter filter = new JsonUsernamePasswordAuthenticationFilter(
        objectMapper);

    filter.setAuthenticationManager(authenticationManager);
    String json = """
          { "username": "testUser", "password": "password" }
        """;

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setContentType("application/json");
    request.setMethod("POST");
    request.setContent(json.getBytes());

    MockHttpServletResponse response = new MockHttpServletResponse();

    when(authenticationManager.authenticate(any()))
        .thenReturn(new UsernamePasswordAuthenticationToken("testUser", "password"));

    Authentication authentication = filter.attemptAuthentication(request, response);

    assertNotNull(authentication);
    assertEquals("testUser", authentication.getPrincipal());
    assertEquals("password", authentication.getCredentials());

  }
}