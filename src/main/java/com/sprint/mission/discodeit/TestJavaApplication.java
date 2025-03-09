package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;

public class TestJavaApplication {

    static User setupUser(UserService userService) {
        User user = userService.create("woody", "Woody", "woody1234", "woody@codeit.com", "010-1234-5678");
        System.out.println("유저 생성 완료 " + user);
        return user;
    }

    static Channel setupChannel(ChannelService channelService, User user) {
        Channel channel = channelService.create("공지", "공지 채널입니다.", "공지사항", user.getUuid(),user.getUuid());
        System.out.println("채널 생성 완료 " + channel);
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", author.getUuid(), channel.getUuid());
        System.out.println("메시지 생성: " + message);
    }

    public static void main(String[] args) {
        // TODO Basic*Service 구현체를 초기화하세요.
        UserService userService = new BasicUserService(new FileUserRepository());
        ChannelService channelService = new BasicChannelService(new FileChannelRepository(), userService);
        MessageService messageService = new BasicMessageService(new FileMessageRepository(), userService, channelService);

        // 셋업 및 테스트 실행
        try {
            User user = setupUser(userService);
            Channel channel = setupChannel(channelService, user);
            messageCreateTest(messageService, channel, user);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
