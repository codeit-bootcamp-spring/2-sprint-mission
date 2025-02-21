package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        // 유저 등록
        User user1 = userService.saveUser("user1");
        User user2 = userService.saveUser("user1");
        User user3 = userService.saveUser("user3");

        // 유저 조회(전체 조회, 이름 조회)
        System.out.println("\n...User findAll.............");
        userService.findAll();

        System.out.println("\n...User findByName<user1>.............");
        userService.findByName("user1");

        // 유저 이름 수정 후 조회
        userService.update(user1.getId(), "user4");
        System.out.println("\n...User findAll after update<uesr1>.............");
        userService.findAll();

        // 삭제 후 조회
        userService.delete(user1.getId());
        System.out.println("\n...User findAll after delete<user1>............");
        userService.findAll();


        // 채널 등록
        Channel channel1 = channelService.saveChannel("channel1");
        Channel channel2 = channelService.saveChannel("channel1");
        Channel channel3 = channelService.saveChannel("channel3");

        // 채널 조회(전체 조회, 이름 조회)
        System.out.println("\n...Channel findAll.............");
        channelService.findAll();

        System.out.println("\n...Channel findByName<channel1>.............");
        channelService.findByName("channel1");

        // 유저 이름 수정 후 조회
        channelService.update(channel1.getId(), "channel4");
        System.out.println("\n...Channel findAll after update<channel1>.............");
        channelService.findAll();

        // 삭제 후 조회
        channelService.delete(channel1.getId());
        System.out.println("\n...Channel findAll after delete<channel1>............");
        channelService.findAll();


        // 메세지 등록
        Message message1 = messageService.saveMessage("Hello, message1");
        Message message2 = messageService.saveMessage("Hello, message1");
        Message message3 = messageService.saveMessage("Hello, message3");

        // 메세지 조회(전체 조회, id조회)
        System.out.println("\n...Message findAll.............");
        messageService.findAll();

        System.out.println("\n...Message findById<message1>.............");
        messageService.findById(message1.getId());
        messageService.findById(UUID.randomUUID()); //등록 안 된 랜덤 id 조회

        // 메세지 text 수정 후 조회
        messageService.update(message1.getId(), "Hello, codeit!");
        System.out.println("\n...Message findAll after update<message1>.............");
        messageService.findAll();

        // 삭제 후 조회
        messageService.delete(message1.getId());
        System.out.println("\n...Message findAll after delete<message1>............");
        messageService.findAll();

    }
}