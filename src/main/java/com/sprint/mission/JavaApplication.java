package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        // 유저 등록
        User user1 = new User("user1");
        User user2 = new User("user2");
        User user3 = new User("user3");

        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);

        // 유저 조회(전체 조회, 이름 조회)
        System.out.println("...User findAll.............");
        userService.findAll();

        System.out.println("...User findByName<user1>.............");
        userService.findByName("user1");

        // 유저 이름 수정
        userService.update(user1.getId(), "user4");

        // 수정 후 조회
        System.out.println("...User findAll after update.............");
        userService.findAll();

        // 삭제
        userService.delete(user1.getId());

        // 삭제 후 조회
        System.out.println("...User findAll after delete............");
        userService.findAll();

//      ====================================================================
        // 채널 등록
        Channel channel1 = new Channel("channel1");
        Channel channel2 = new Channel("channel2");
        Channel channel3 = new Channel("channel3");

        channelService.saveChannel(channel1);
        channelService.saveChannel(channel2);
        channelService.saveChannel(channel3);

        // 채널 조회(전체 조회, 이름 조회)
        System.out.println("...Channel findAll.............");
        channelService.findAll();

        System.out.println("...Channel findByName<channel1>.............");
        channelService.findByName("channel1");

        // 유저 이름 수정
        channelService.update(channel1.getId(), "channel4");

        // 수정 후 조회
        System.out.println("...Channel findAll after update.............");
        channelService.findAll();

        // 삭제
        channelService.delete(user1.getId());

        // 삭제 후 조회
        System.out.println("...Channel findAll after delete............");
        channelService.findAll();


//        ===============================================================
        // 메세지 등록
        Message message1 = new Message("message1");
        Message message2 = new Message("message2");
        Message message3 = new Message("message3");

        messageService.saveMessage(message1);
        messageService.saveMessage(message2);
        messageService.saveMessage(message3);

        // 메세지 조회(전체 조회, 이름 조회)
        System.out.println("...Message findAll.............");
        messageService.findAll();

        System.out.println("...Message findById<message1>.............");
        messageService.findById(message1.getId());

        // 메세지 이름 수정
        messageService.update(message1.getId(), "message4");

        // 수정 후 조회
        System.out.println("...Message findAll after update.............");
        messageService.findAll();

        // 삭제
        messageService.delete(user1.getId());

        // 삭제 후 조회
        System.out.println("...Message findAll after delete............");
        messageService.findAll();

    }
}