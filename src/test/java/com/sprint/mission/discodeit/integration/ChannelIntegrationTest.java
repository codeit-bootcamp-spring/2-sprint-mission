package com.sprint.mission.discodeit.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.controller.channel.CreatePrivateChannelRequestDTO;
import com.sprint.mission.discodeit.dto.controller.channel.CreatePrivateChannelResponseDTO;
import com.sprint.mission.discodeit.dto.controller.channel.CreatePublicChannelRequestDTO;
import com.sprint.mission.discodeit.dto.service.channel.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.dto.service.channel.CreatePublicChannelResult;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChannelIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EntityManager entityManager;

  private String getBaseUrl() {
    return "http://localhost:" + port + "/api/channels";
  }

  @Test
  @DisplayName("공개 채널 생성 성공")
  void create_publicChannel_success() throws Exception {
    // given
    CreatePublicChannelRequestDTO createPublicChannelRequestDTO = new CreatePublicChannelRequestDTO(
        "testChannel", "channel Test");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(
        objectMapper.writeValueAsString(createPublicChannelRequestDTO), headers);

    // when
    ResponseEntity<CreatePublicChannelResult> response = restTemplate.postForEntity(
        getBaseUrl() + "/public",
        request,
        CreatePublicChannelResult.class
    );

    // then
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody().name()).isEqualTo("testChannel");
  }

  @Test
  @DisplayName("입력값 오류 발생 시, 공개 채널 생성 실패")
  void create_publicChannel_failed() throws Exception {
    // given
    CreatePublicChannelRequestDTO createPublicChannelRequestDTO = new CreatePublicChannelRequestDTO(
        "", "channel Test");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(
        objectMapper.writeValueAsString(createPublicChannelRequestDTO), headers);

    // when
    ResponseEntity<CreatePublicChannelResult> response = restTemplate.postForEntity(
        getBaseUrl() + "/public",
        request,
        CreatePublicChannelResult.class
    );

    // then
    assertThat(response.getStatusCode().is2xxSuccessful()).isFalse();
  }
}
