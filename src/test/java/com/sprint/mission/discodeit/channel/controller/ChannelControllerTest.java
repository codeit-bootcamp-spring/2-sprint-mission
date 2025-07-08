package com.sprint.mission.discodeit.channel.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.core.channel.ChannelException;
import com.sprint.mission.discodeit.core.channel.controller.ChannelController;
import com.sprint.mission.discodeit.core.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.core.channel.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.core.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.service.BasicChannelService;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = ChannelController.class)
public class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BasicChannelService channelService;

  @Autowired
  private ObjectMapper objectMapper;

  UUID channelId;

  @BeforeEach
  void setUp() {
    channelId = UUID.randomUUID();
  }

  @Test
  void create_Public_Success() throws Exception {
    // given
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("test",
        "test");
    ChannelDto channelDto = new ChannelDto(channelId, ChannelType.PUBLIC, "test", "test", null,
        null);
    when(channelService.create(any(PublicChannelCreateRequest.class))).thenReturn(channelDto);
    // when & then
    mockMvc.perform(post("/api/channels/public")
            .with(csrf())
            .with(user("user").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("test"))
        .andExpect(jsonPath("$.description").value("test"))
        .andExpect(jsonPath("$.type").value("PUBLIC"));
  }

  @Test
  void create_Private_Success() throws Exception {
    // given
    UUID u1Id = UUID.randomUUID();
    UUID u2Id = UUID.randomUUID();
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(u1Id, u2Id));
    ChannelDto channelDto = new ChannelDto(channelId, ChannelType.PRIVATE, null, null, null,
        null);
    when(channelService.create(any(PrivateChannelCreateRequest.class))).thenReturn(channelDto);
    // when & then
    mockMvc.perform(post("/api/channels/private")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(request)))
        .andExpect(jsonPath("$.name").doesNotExist())
        .andExpect(jsonPath("$.description").doesNotExist());
//        .andExpect(jsonPath("$.type").value("PRIVATE"));
  }

  @Test
  void findAll_Success() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    ChannelDto channelDto1 = new ChannelDto(channelId, ChannelType.PUBLIC, "a", "test", null,
        null);
    ChannelDto channelDto2 = new ChannelDto(channelId, ChannelType.PUBLIC, "b", "test", null,
        null);
    List<ChannelDto> dtoList = List.of(channelDto1, channelDto2);
    when(channelService.findAllByUserId(userId)).thenReturn(dtoList);
    // when & then
    mockMvc.perform(get("/api/channels")
            .with(csrf())
            .with(user("user").roles("USER"))
            .param("userId", userId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name").value("a"))
        .andExpect(jsonPath("$[1].name").value("b"));
  }

  @Test
  void update_Success() throws Exception {
    // given
    ChannelUpdateRequest updateRequest = new ChannelUpdateRequest("abc", "aaa");
    ChannelDto channelDto = new ChannelDto(channelId, ChannelType.PUBLIC, "abc", "aaa", null,
        null);
    when(channelService.update(eq(channelId), any(ChannelUpdateRequest.class))).thenReturn(
        channelDto);
    // when & then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .with(csrf())
            .with(user("user").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.type").value("PUBLIC"))
        .andExpect(jsonPath("$.name").value("abc"))
        .andExpect(jsonPath("$.description").value("aaa"));
  }

  @Test
  void update_ChannelNotFound_Throw404() throws Exception {
    // given
    ChannelUpdateRequest updateRequest = new ChannelUpdateRequest("abc", "aaa");
    doThrow(new ChannelException(ErrorCode.CHANNEL_NOT_FOUND, channelId))
        .when(channelService).update(eq(channelId), any(ChannelUpdateRequest.class));
    // when & then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .with(csrf())
            .with(user("user").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ErrorCode.CHANNEL_NOT_FOUND.getCode()));
  }

  @Test
  void update_PrivateChannel_Throw400() throws Exception {
    // given
    ChannelUpdateRequest updateRequest = new ChannelUpdateRequest("abc", "aaa");
    doThrow(new ChannelException(ErrorCode.CHANNEL_INVALID_REQUEST, channelId))
        .when(channelService).update(eq(channelId), any(ChannelUpdateRequest.class));
    // when & then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .with(csrf())
            .with(user("user").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.CHANNEL_INVALID_REQUEST.getCode()));
  }

  @Test
  void delete_Success() throws Exception {
    // given
    // when & then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId)
            .with(csrf())
            .with(user("user").roles("USER"))
        )
        .andExpect(status().isNoContent());
  }

  @Test
  void delete_ChannelNotFound_Throw404() throws Exception {
    // given
    doThrow(new ChannelException(ErrorCode.CHANNEL_NOT_FOUND, channelId))
        .when(channelService).delete(channelId);
    // when & then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId)
            .with(csrf())
            .with(user("user").roles("USER")))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ErrorCode.CHANNEL_NOT_FOUND.getCode()));
  }
}
