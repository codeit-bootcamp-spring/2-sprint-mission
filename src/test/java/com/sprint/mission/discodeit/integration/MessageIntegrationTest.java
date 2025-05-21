package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.controller.message.CreateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.controller.message.CreateMessageResponseDTO;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MessageIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  private String getBaseUrl() {
    return "http://localhost:" + port + "/api/messages";
  }

  @Test
  @Sql(scripts = {
      "/insert_channel.sql", "/insert_user.sql"},
      config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
  @DisplayName("첨부파일 있는 메세지 생성 성공")
  void sendMessage_success() throws Exception {

    //given
    String channelId = "00000000-0000-0000-0000-000000000001";
    String authorId = "00000000-0000-0000-0000-000000000002";
    CreateMessageRequestDTO request = new CreateMessageRequestDTO(
        "content",
        UUID.fromString(channelId),
        UUID.fromString(authorId)
    );

    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> messagePart = new HttpEntity<>(objectMapper.writeValueAsString(request),
        jsonHeaders);
    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    parts.add("messageCreateRequest", messagePart);

    byte[] fileContent = "attachment image".getBytes(StandardCharsets.UTF_8);
    ByteArrayResource fileResource = new ByteArrayResource(fileContent);
    parts.add("attachments", fileResource);

    ResponseEntity<CreateMessageResponseDTO> response = restTemplate.postForEntity(getBaseUrl(),
        parts,
        CreateMessageResponseDTO.class);

    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody().content()).isEqualTo("content");
  }

  @Test
  @Sql(scripts = {
      "/insert_channel.sql", "/insert_user.sql"},
      config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
  @DisplayName("채널이 없는 경우, 메시지 생성 실패")
  void sendMessage_channelNotFound_failed() throws Exception {

    //given
    String channelId = "00000000-0000-0000-0000-00000003141";
    String authorId = "00000000-0000-0000-0000-000000000002";
    CreateMessageRequestDTO request = new CreateMessageRequestDTO(
        "content",
        UUID.fromString(channelId),
        UUID.fromString(authorId)
    );

    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> messagePart = new HttpEntity<>(objectMapper.writeValueAsString(request),
        jsonHeaders);
    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    parts.add("messageCreateRequest", messagePart);

    byte[] fileContent = "attachment image".getBytes(StandardCharsets.UTF_8);
    ByteArrayResource fileResource = new ByteArrayResource(fileContent);
    parts.add("attachments", fileResource);

    ResponseEntity<CreateMessageResponseDTO> response = restTemplate.postForEntity(getBaseUrl(),
        parts,
        CreateMessageResponseDTO.class);

    assertThat(response.getStatusCode().is2xxSuccessful()).isFalse();
  }

  @Test
  @Sql(scripts = {
      "/insert_channel.sql", "/insert_user.sql"},
      config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
  @DisplayName("유저가 없는 경우, 메시지 생성 실패")
  void sendMessage_userNotFound_failed() throws Exception {

    //given
    String channelId = "00000000-0000-0000-0000-00000000001";
    String authorId = "00000000-0000-0000-0000-000000003333";
    CreateMessageRequestDTO request = new CreateMessageRequestDTO(
        "content",
        UUID.fromString(channelId),
        UUID.fromString(authorId)
    );

    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> messagePart = new HttpEntity<>(objectMapper.writeValueAsString(request),
        jsonHeaders);
    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    parts.add("messageCreateRequest", messagePart);

    byte[] fileContent = "attachment image".getBytes(StandardCharsets.UTF_8);
    ByteArrayResource fileResource = new ByteArrayResource(fileContent);
    parts.add("attachments", fileResource);

    ResponseEntity<CreateMessageResponseDTO> response = restTemplate.postForEntity(getBaseUrl(),
        parts,
        CreateMessageResponseDTO.class);

    assertThat(response.getStatusCode().is2xxSuccessful()).isFalse();
  }


}
