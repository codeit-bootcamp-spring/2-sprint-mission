package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DiscodeitApplicationTest {
    @Autowired
    private UserController userController;

    @Autowired
    private ChannelController channelController;


}