package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) {
		// Spring에서 자체 실행되는 문장
		ConfigurableApplicationContext content = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = content.getBean(UserService.class);
		ChannelService channelService = content.getBean(ChannelService.class);
		MessageService messageService = content.getBean(MessageService.class);


	}
}
