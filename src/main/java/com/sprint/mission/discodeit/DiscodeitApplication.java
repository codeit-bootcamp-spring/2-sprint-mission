package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        BasicUserService userService = context.getBean(BasicUserService.class);
        BasicChannelService channelService = context.getBean(BasicChannelService.class);
        BasicMessageService messageService = context.getBean(BasicMessageService.class);

        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        messageCreateTest(messageService, channel, user);
    }

    static User setupUser(UserService userService) {
        User user = userService.create("woody", "woody@codeit.com", "woody1234");
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
        System.out.println("메시지 생성: " + message.getId());
    }

}
