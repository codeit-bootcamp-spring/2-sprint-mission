package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.core.message.controller.MessageController;
import com.sprint.mission.discodeit.core.message.usecase.MessageService;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private MessageService messageService;

}
