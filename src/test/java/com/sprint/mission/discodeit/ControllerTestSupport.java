package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.domain.user.controller.UserController;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(value = {UserController.class})
public abstract class ControllerTestSupport {

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

}
