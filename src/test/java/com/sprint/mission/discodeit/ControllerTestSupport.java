package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.domain.auth.controller.AuthController;
import com.sprint.mission.discodeit.domain.user.controller.UserController;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.userstatus.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(value = {UserController.class, AuthController.class})
public abstract class ControllerTestSupport {

    @MockitoBean
    protected UserService userService;
    @MockitoBean
    private UserStatusService userStatusService;
//    @MockitoBean
//    private AuthService authService;
    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    protected MockMvcTester mockMvc;


}
