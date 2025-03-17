package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ServiceFactory;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {
    static UUID setupUser(UserService userService) {
        UUID userId = userService.create("woody", "woody@codeit.com");
        System.out.println("유저 생성: " + userId);
        return userId;
    }

    static UUID setupChannel(ChannelService channelService) {
        UUID channelId = channelService.create("공지");
        System.out.println("채널 생성: " + channelId);
        return channelId;
    }

    static void messageCreateTest(MessageService messageService, UUID senderId, UUID channelId) {
        UUID message = messageService.create("안녕하세요.", senderId, channelId);
        System.out.println("메시지 생성: " + message);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // 서비스 빈 가져오기
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 셋업
        UUID user = setupUser(userService);
        UUID channel = setupChannel(channelService);

        // 테스트
        messageCreateTest(messageService, user, channel);
    }
}
