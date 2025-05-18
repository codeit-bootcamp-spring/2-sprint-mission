package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
@Import(GlobalExceptionHandler.class)
class ChannelControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper om;

  @MockBean
  private ChannelService channelService;

  @Test
  @DisplayName("POST /api/channels/public - 성공")
  void createPublicChannel_success() throws Exception {
    UUID id = UUID.randomUUID();
    Instant now = Instant.parse("2025-01-01T12:00:00Z");
    ChannelDto dto = new ChannelDto(
        id,
        ChannelType.PUBLIC,
        "general",
        "모두를 위한 채널",
        List.of(),
        now
    );

    PublicChannelCreateRequest req = new PublicChannelCreateRequest("general", "모두를 위한 채널");
    given(channelService.create(any(PublicChannelCreateRequest.class)))
        .willReturn(dto);

    mvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.type").value("PUBLIC"))
        .andExpect(jsonPath("$.name").value("general"))
        .andExpect(jsonPath("$.description").value("모두를 위한 채널"))
        .andExpect(jsonPath("$.participants").isArray())
        .andExpect(jsonPath("$.lastMessageAt").value(now.toString()));
  }

  @Test
  @DisplayName("POST /api/channels/public - 검증 실패 (name blank) → 400")
  void createPublicChannel_validationError() throws Exception {
    PublicChannelCreateRequest req = new PublicChannelCreateRequest("", null);

    mvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(req)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details.name").exists());
  }

  @Test
  @DisplayName("POST /api/channels/private - 성공")
  void createPrivateChannel_success() throws Exception {
    UUID id = UUID.randomUUID();
    ChannelDto dto = new ChannelDto(
        id,
        ChannelType.PRIVATE,
        null,
        null,
        List.of(),
        null
    );

    PrivateChannelCreateRequest req = new PrivateChannelCreateRequest(
        List.of(UUID.randomUUID(), UUID.randomUUID())
    );
    given(channelService.create(any(PrivateChannelCreateRequest.class)))
        .willReturn(dto);

    mvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.type").value("PRIVATE"))
        .andExpect(jsonPath("$.participants").isArray());
  }

  @Test
  @DisplayName("POST /api/channels/private - 검증 실패 (participants empty) → 400")
  void createPrivateChannel_validationError() throws Exception {
    PrivateChannelCreateRequest req = new PrivateChannelCreateRequest(List.of());

    mvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(req)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details.participantIds").exists());
  }

  @Test
  @DisplayName("PATCH /api/channels/{id} - 성공")
  void updateChannel_success() throws Exception {
    UUID id = UUID.randomUUID();
    PublicChannelUpdateRequest req = new PublicChannelUpdateRequest("newName", "newDesc");
    ChannelDto dto = new ChannelDto(
        id,
        ChannelType.PUBLIC,
        "newName",
        "newDesc",
        List.of(),
        null
    );

    given(channelService.update(id, req)).willReturn(dto);

    mvc.perform(patch("/api/channels/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("newName"))
        .andExpect(jsonPath("$.description").value("newDesc"));
  }

  @Test
  @DisplayName("PATCH /api/channels/{id} - 실패 (채널 없음) → 404")
  void updateChannel_notFound() throws Exception {
    UUID id = UUID.randomUUID();
    PublicChannelUpdateRequest req = new PublicChannelUpdateRequest("n", "d");

    given(channelService.update(id, req))
        .willThrow(new ChannelNotFoundException(Map.of("channelId", id)));

    mvc.perform(patch("/api/channels/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(req)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("C001"));
  }

  @Test
  @DisplayName("DELETE /api/channels/{id} - 성공")
  void deleteChannel_success() throws Exception {
    UUID id = UUID.randomUUID();

    mvc.perform(delete("/api/channels/{id}", id))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("DELETE /api/channels/{id} - 실패 (채널 없음) → 404")
  void deleteChannel_notFound() throws Exception {
    UUID id = UUID.randomUUID();
    doThrow(new ChannelNotFoundException(Map.of("channelId", id)))
        .when(channelService).delete(id);

    mvc.perform(delete("/api/channels/{id}", id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("C001"));
  }

  @Test
  @DisplayName("GET /api/channels?userId={userId} - 성공")
  void findAllChannels_success() throws Exception {
    UUID uid = UUID.randomUUID();
    ChannelDto c1 = new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "A", "D", List.of(),
        null);
    ChannelDto c2 = new ChannelDto(UUID.randomUUID(), ChannelType.PRIVATE, null, null, List.of(),
        null);

    given(channelService.findAllByUserId(uid)).willReturn(List.of(c1, c2));

    mvc.perform(get("/api/channels").param("userId", uid.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(c1.id().toString()))
        .andExpect(jsonPath("$[1].type").value("PRIVATE"));
  }

  @Test
  @DisplayName("GET /api/channels - userId 누락 → 400")
  void findAllChannels_missingParam() throws Exception {
    mvc.perform(get("/api/channels"))
        .andExpect(status().isBadRequest());
  }
}
