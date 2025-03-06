package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class JavaApplication {
    static User setupUser1(UserService userService) {
        User user = new User("세종대왕");
        userService.create(user);
        return user;
    }

    static User setupUser2(UserService userService) {
        User user = new User("이순신");
        userService.create(user);
        return user;
    }

    static Channel setupChannel(ChannelService channelService, User user) {
        Channel channel = new Channel("과제");
        channelService.create(channel);
        channelService.addMember(channel.getId(), user);
        channelService.findMembers(channel.getId());
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = new Message(author, "안녕하세요", channel);
        messageService.create(message);
    }

    public static void main(String[] args) {
        UserRepository userRepository = new JCFUserRepository();
        ChannelRepository channelRepository = new JCFChannelRepository();
        MessageRepository messageRepository = new JCFMessageRepository();

        UserService userService = BasicUserService.getInstance(userRepository);
        ChannelService channelService = BasicChannelService.getInstance(channelRepository);
        MessageService messageService = BasicMessageService.getInstance(userService, channelService, messageRepository);

        // 셋업
        User user1 = setupUser1(userService);
        User user2 = setupUser2(userService);
        Channel channel = setupChannel(channelService, user1);
        // 테스트
        messageCreateTest(messageService, channel, user1);
        messageCreateTest(messageService, channel, user2);
    }
}