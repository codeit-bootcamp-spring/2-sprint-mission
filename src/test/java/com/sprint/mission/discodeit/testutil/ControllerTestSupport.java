package com.sprint.mission.discodeit.testutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.auth.controller.AuthController;
import com.sprint.mission.discodeit.domain.auth.service.AuthService;
import com.sprint.mission.discodeit.domain.binarycontent.controller.BinaryContentController;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.message.controller.MessageController;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.domain.user.controller.UserController;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WithMockUser
@WebMvcTest(controllers = {
    UserController.class,
    MessageController.class,
    BinaryContentController.class,
    AuthController.class
})
public abstract class ControllerTestSupport {

  @MockitoBean
  private JpaMetamodelMappingContext jpaMetamodelMappingContext;

  @Autowired
  protected MockMvcTester mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;

  @MockitoBean
  protected MessageService messageService;
  @MockitoBean
  protected UserService userService;
  @MockitoBean
  protected BinaryContentService binaryContentService;
  @MockitoBean
  protected BinaryContentStorage binaryContentStorage;
  @MockitoBean
  protected AuthService authService;

}
