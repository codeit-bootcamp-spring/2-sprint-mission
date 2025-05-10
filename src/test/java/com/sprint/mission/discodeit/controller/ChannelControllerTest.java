package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.core.channel.controller.ChannelController;
import com.sprint.mission.discodeit.core.channel.usecase.BasicChannelService;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
public class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private BasicChannelService channelService;

}
