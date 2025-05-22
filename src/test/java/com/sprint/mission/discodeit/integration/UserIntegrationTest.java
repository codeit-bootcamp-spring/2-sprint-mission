package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.controller.user.CreateUserRequestDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  EntityManager entityManager;

  private String getBaseUrl() {
    return "http://localhost:" + port + "/api/users";
  }

  @Test
  @DisplayName("프로필이 있는 유저 생성 성공")
  void createUser_withProfile_success() throws Exception {
    // given
    CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO("testUser", "TestUser!23",
        "test@test.com");

    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> userPart = new HttpEntity<>(
        objectMapper.writeValueAsString(createUserRequestDTO), jsonHeaders);
    parts.add("userCreateRequest", userPart);

    byte[] fileContent = "profile image".getBytes(StandardCharsets.UTF_8);
    ByteArrayResource fileResource = new ByteArrayResource(fileContent);
    parts.add("profile", fileResource);
    // when
    ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), parts, String.class);

    // then
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
  }

  @Test
  @DisplayName("중복 username인 경우 유저 생성 실패")
  void createUser_duplicate_username_failed() throws Exception {
    // given
    CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO("testUser", "TestUser!23",
        "test@test.com");

    userRepository.save(new User("testUser", "testUser@test.com", "TestUser!23", null));
    entityManager.flush();

    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> userPart = new HttpEntity<>(
        objectMapper.writeValueAsString(createUserRequestDTO), jsonHeaders);
    parts.add("userCreateRequest", userPart);

    byte[] fileContent = "profile image".getBytes(StandardCharsets.UTF_8);
    ByteArrayResource fileResource = new ByteArrayResource(fileContent);
    parts.add("profile", fileResource);
    // when
    ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), parts, String.class);

    // then
    assertThat(response.getStatusCode().is2xxSuccessful()).isFalse();
  }

}
