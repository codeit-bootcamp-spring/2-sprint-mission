package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class Main {
    public static void main(String[] args) {

        UserService userService = JCFUserService.getInstance(); // User 기능 객체 생성

        User user1 = userService.createUser("김현호");

        userService.getAllUserData();
        User user2 = userService.createUser("현호킴");
        userService.getAllUserData();

        userService.getUserInfo(user1.getUserName());
        userService.updateUserName(user1.getUserName(), "이현호");
        userService.updateUserName("김땡땡", "이현호");

        userService.deleteUserName(user1.getUserName());
        userService.getAllUserData();

        System.out.println("------");

        ChannelService channelService = JCFChannelService.getInstance();

        Channel channel1 = channelService.createChannel("백엔드 톡방");
        Channel channel2 = channelService.createChannel("자바스프링 톡방");

        channelService.getChannelInfo(channel1.getChatRoomName());
        channelService.getAllChannelData();

        channelService.updateChannelName(channel1.getChatRoomName(), "백엔드");
        channelService.getAllChannelData();

        channelService.deleteChannelName(channel2.getChatRoomName());
        channelService.getAllChannelData();

        channelService.createChannel("");

        System.out.println("------");

        MessageService messageService = JCFMessageService.getInstance(channelService, userService);

        Message message1 = messageService.createMessage(user1.getId(), channel1.getId(), "안녕하세요~! 잘부탁드립니다.");
        Message message2 = messageService.createMessage(user2.getId(), channel1.getId(), "네넹넹");
        Message message3 = messageService.createMessage(user1.getId(), channel2.getId(), "test test test");

        messageService.getMessageInfo(channel1.getId());
        messageService.getAllMessageData();

        messageService.updateMessage(message1.getId(), "테스트 변경 성공");
        messageService.deleteMessage(message1.getId());

        messageService.getAllMessageData();

    }
}
