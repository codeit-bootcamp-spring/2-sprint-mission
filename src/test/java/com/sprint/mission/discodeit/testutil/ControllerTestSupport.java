package com.sprint.mission.discodeit.testutil;

import com.sprint.mission.discodeit.domain.user.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(value = {UserController.class})
public abstract class ControllerTestSupport {

  @MockitoBean
  private JpaMetamodelMappingContext jpaMetamodelMappingContext;

  @Autowired
  protected MockMvcTester mockMvc;

}
