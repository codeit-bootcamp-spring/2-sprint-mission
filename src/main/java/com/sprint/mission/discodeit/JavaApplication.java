package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class JavaApplication {
    public static void main(String[] args) {
        /*// 레포지토리 초기화
        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();

        // 서비스 초기화
        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);
*/
        // 스프링 컨텍스트 생성
        ApplicationContext context = new AnnotationConfigApplicationContext(DiscodeitApplication.class);

        // 필요한 Bean 가져오기
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

//        // DiscodeitApplication에 옮겨둔 static 메소드 호출
//        User user = DiscodeitApplication.setupUser(userService);
//        Channel channel = DiscodeitApplication.setupChannel(channelService);
//        DiscodeitApplication.messageCreateTest(messageService, channel, user);

        /*// 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, user);*/
    }
}
