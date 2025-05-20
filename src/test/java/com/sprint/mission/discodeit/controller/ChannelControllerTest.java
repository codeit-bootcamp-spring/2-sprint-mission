package com.sprint.mission.discodeit.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.mapper.UserMapperImpl;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ChannelService channelService;

  @MockBean
  private UserService userService;

  @Test
  void channel_find_success() throws Exception {
    UUID userId = UUID.randomUUID();
    List<ChannelDto> channels = List.of(
        new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "공지방", "공지입니다.",
            userService.findAll(), Instant.now())
    );

    given(channelService.findAllByUserId(userId)).willReturn(channels);

    mockMvc.perform(get("/api/channels/user/" + userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("공지방"));
  }

  @Test
  void channel_find_fail() throws Exception {
    UUID userId = UUID.randomUUID();
    given(channelService.findAllByUserId(userId)).willThrow(new RuntimeException("DB 오류"));

    mockMvc.perform(get("/api/channels/user/" + userId))
        .andExpect(status().isInternalServerError());
  }
}

