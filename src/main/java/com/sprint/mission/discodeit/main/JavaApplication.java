package com.sprint.mission.discodeit.main;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();

        User user1 = userService.createUser("Kim");
        User user2 = userService.createUser("Lee");

        System.out.println("=== 유저 등록 완료 ===");
        System.out.println("User 1: " + user1.getId() + " | Name: " + user1.getUsername());
        System.out.println("User 2: " + user2.getId() + " | Name: " + user2.getUsername());

        User fetchedUser = userService.getUser(user1.getId());
        System.out.println("=== 유저 조회 ===");
        System.out.println("Fetched User: " + fetchedUser.getId() + " | Name: " + fetchedUser.getUsername());

        System.out.println("=== 모든 유저 조회 ===");
        for (User u : userService.getAllUsers()) {
            System.out.println("ID: " + u.getId() + " | Name: " + u.getUsername());
        }

        userService.updateUser(user1.getId(), "Park");
        System.out.println("=== 유저 정보 수정 완료 ===");
        System.out.println("Updated User 1: " + userService.getUser(user1.getId()).getUsername());

        userService.deleteUser(user2.getId());
        System.out.println("=== 유저 삭제 완료 ===");
        System.out.println("User 2 Exists? " + (userService.getUser(user2.getId()) != null));

        System.out.println("=== 삭제 후 모든 유저 조회 ===");
        for (User u : userService.getAllUsers()) {
            System.out.println("ID: " + u.getId() + " | Name: " + u.getUsername());
        }

        System.out.println("-----");

        Channel channel1 = channelService.createChannel("Channel1");
        Channel channel2 = channelService.createChannel("Channel2");

        System.out.println("=== 채널 등록 완료 ===");
        System.out.println("Channel 1: " + channel1.getId() + " | Name: " + channel1.getChannelName());
        System.out.println("Channel 2: " + channel2.getId() + " | Name: " + channel2.getChannelName());

        Channel fetchedChannel1 = channelService.getChannel(channel1.getId());
        System.out.println("=== 채널 조회 ===");
        System.out.println("Fetched Channel1: " + fetchedChannel1.getId() + " | Name: " + fetchedChannel1.getChannelName());

        System.out.println("=== 모든 채널 조회 ===");
        for (Channel ch : channelService.getAllChannels()) {
            System.out.println("ID: " + ch.getId() + " | Name: " + ch.getChannelName());
        }

        channelService.updateChannel(channel1.getId(), "Announcements");
        System.out.println("=== 채널 정보 수정 완료 ===");
        System.out.println("Updated Channel 1: " + channelService.getChannel(channel1.getId()).getChannelName());

        channelService.deleteChannel(channel2.getId());
        System.out.println("=== 채널 삭제 완료 ===");
        System.out.println("Channel 2 Exists? " + (channelService.getChannel(channel2.getId()) != null));

        System.out.println("=== 삭제 후 모든 채널 조회 ===");
        for (Channel ch : channelService.getAllChannels()) {
            System.out.println("ID: " + ch.getId() + " | Name: " + ch.getChannelName());
        }
    }
}
