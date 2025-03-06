package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;


public class JavaApplication {
    static User setupUser(UserService userService) {
        User user = userService.create("codeit", "codeit@codeit.com", "codeit");
        System.out.println("유저 생성 : " + user.getId() + ", 이름 : " + user.getUserName() + ", 이메일 : " + user.getUserEmail());
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        System.out.println("채널 생성 : " + channel.getId() + ", 채널 타입 : " + channel.getType() + ", 채널 이름: " + channel.getChannelName() + ", 채널 설명 : " + channel.getDescription());
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User user) {
        Message message = messageService.create("안녕하세요.", channel.getId(), user.getId());
        System.out.println("메시지 생성: " + message.getId() + ", 내용: " + message.getContent());
    }

    public static void main(String[] args) {
        // Basic
        UserService basicUserService = BasicUserService.getInstance(new JCFUserRepository());
        ChannelService basicChannelService = BasicChannelService.getInstance(new JCFChannelRepository());
        MessageService basicMessageService = BasicMessageService.getInstance(new JCFMessageRepository(), basicChannelService, basicUserService);

        // File
        UserService fileUserService = FileUserService.getInstance(new FileUserRepository());
        ChannelService fileChannelService = FileChannelService.getInstance(new FileChannelRepository());
        MessageService fileMessageService = FileMessageService.getInstance(new FileMessageRepository(), fileChannelService, fileUserService);

        // Basic
        System.out.println("Basic Service 테스트 결과입니다.");
        User basicUser = setupUser(basicUserService);
        Channel basicChannel = setupChannel(basicChannelService);
        messageCreateTest(basicMessageService, basicChannel, basicUser);

        // File
        System.out.println("File Service 테스트 결과입니다.");
        User fileUser = setupUser(fileUserService);
        Channel fileChannel = setupChannel(fileChannelService);
        messageCreateTest(fileMessageService, fileChannel, fileUser);
    }
}

