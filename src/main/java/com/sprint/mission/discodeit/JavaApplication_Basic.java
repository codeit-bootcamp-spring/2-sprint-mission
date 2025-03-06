package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.RepositoryType;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;

public class JavaApplication_Basic {
    public static void main(String[] args) {
        UserService userService = new BasicUserService(RepositoryType.getUserRepository());
        ChannelService channelService = new BasicChannelService(RepositoryType.getChannelRepository());
        MessageService messageService = new BasicMessageService(RepositoryType.getMessageRepository());

        User user = new User("woody");
        userService.create(user);
        System.out.println("✅ 사용자 생성: " + user.getUserName() + "  ID: " + user.getId());

        Channel channel = new Channel("공지 채널입니다.");
        channelService.create(channel);
        System.out.println("✅ 채널 생성: " + channel.getName() + "  ID: " + channel.getId());

        Message message = new Message("안녕하세요.", channel.getId(), user.getId());
        messageService.create(message);
        System.out.println("✅ 메시지 생성: " + message.getContent() + "  ID: " + message.getId());

    }
}