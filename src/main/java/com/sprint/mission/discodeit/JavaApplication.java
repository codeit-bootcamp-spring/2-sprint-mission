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

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = JCFUserService.getInstance();
        ChannelService channelService = JCFChannelService.getInstance();
        MessageService messageService = JCFMessageService.getInstance(userService, channelService);

        System.out.println("========== 유저 등록 ==========");
        User user1 = new User("Alice");
        User user2 = new User("Minho");

        userService.create(user1);
        userService.create(user2);

        System.out.println("========== 유저 조회 ==========");
        System.out.println("단건 조회: " + userService.findById(user1.getId()));

        System.out.println("다건 조회:");
        userService.findAll().forEach(System.out::println);

        System.out.println("========== 유저 수정 ==========");
        userService.update(user1.getId(), "Tomas");
        System.out.println("수정된 데이터 조회: " + userService.findById(user1.getId()));

        System.out.println("========== 유저 삭제 ==========");
        userService.delete(user1.getId());
        System.out.println("삭제 후 유저 목록:");
        userService.findAll().forEach(System.out::println);

        System.out.println("========== 채널 등록 ==========");
        Channel channel1 = new Channel("코드잇 스프린트 1기");
        Channel channel2 = new Channel("코드잇 스프린트 2기");

        channelService.create(channel1);
        channelService.create(channel2);

        System.out.println("========== 채널 조회 ==========");
        System.out.println("단건 조회: " + channelService.findById(channel1.getId()));

        System.out.println("========== 채널 수정 ==========");
        channelService.update(channel2.getId(), "코드잇 스프린트 Spring 백엔드 2기");
        System.out.println("수정된 데이터 조회: " + channelService.findById(channel2.getId()));

        System.out.println("========== 채널 삭제 ==========");
        channelService.delete(channel1.getId());
        System.out.println("삭제 후 채널 목록:");
        channelService.findAll().forEach(System.out::println);

        System.out.println("========== 메시지 등록 ==========");
        Message message1 = new Message(user2.getId(), channel2.getId(), "안녕하세요");
        Message message2 = new Message(user2.getId(), channel2.getId(), "반갑습니다");

        messageService.create(message1);
        messageService.create(message2);

        System.out.println("========== 메시지 조회 ==========");
        System.out.println("단건 조회: " + messageService.findById(message1.getId()));

        System.out.println("다건 조회:");
        messageService.findAll().forEach(System.out::println);

        System.out.println("========== 메시지 수정 ==========");
        messageService.update(message1.getId(), "반갑습니다");
        System.out.println("수정된 데이터 조회: " + messageService.findById(message1.getId()));

        System.out.println("========== 메시지 삭제 ==========");
        messageService.delete(message2.getId());
        System.out.println("삭제 후 메시지 목록:");
        messageService.findAll().forEach(System.out::println);
    }
}