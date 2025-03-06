package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.NoSuchElementException;

public class JavaApplication {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        UserService userService = appConfig.userService();
        ChannelService channelService = appConfig.channelService();
        MessageService messageService = appConfig.messageService();

        // 유저 등록
        User user1 = userService.saveUser("user1");
        User user2 = userService.saveUser("user2");
        User user3 = userService.saveUser("user3");

        // 유저 조회(전체 조회, 이름 조회)
        System.out.println("\n...User findAll.............");
        userService.findAll().forEach(System.out::println);

        System.out.println("\n...User findByName<user1>.............");
        userService.findByName("user1").forEach(System.out::println);

        // 유저 이름 수정 후 조회
        userService.update(user1.getId(), "user4");

        System.out.println("\n...User findAll after update<uesr1>.............");
        userService.findAll().forEach(System.out::println);


        // 삭제 후 조회
        userService.delete(user1.getId());
        System.out.println("\n...User findAll after delete<user1>............");
        userService.findAll().forEach(System.out::println);


        // 채널 등록
        Channel channel1 = channelService.saveChannel("channel1");
        Channel channel2 = channelService.saveChannel("channel2");
        Channel channel3 = channelService.saveChannel("channel3");

        // 채널 조회(전체 조회, 이름 조회)
        System.out.println("\n...Channel findAll.............");
        channelService.findAll().forEach(System.out::println);

        System.out.println("\n...Channel findByName<channel1>.............");
        channelService.findByName("channel1").forEach(System.out::println);

        // 유저 이름 수정 후 조회
        channelService.update(channel1.getId(), "channel4");
        System.out.println("\n...Channel findAll after update<channel1>.............");
        channelService.findAll().forEach(System.out::println);

        // 삭제 후 조회
        channelService.delete(channel1.getId());
        System.out.println("\n...Channel findAll after delete<channel1>............");
        channelService.findAll().forEach(System.out::println);


        // 메세지 등록
        Message message1 = messageService.saveMessage(channel2, user2, "Hello, message1");
        Message message2 = messageService.saveMessage(channel3, user3, "Hello, message2");

        // 메세지 조회(전체 조회, id조회)
        System.out.println("\n...Message findAll.............");
        messageService.findAll().forEach(System.out::println);

        System.out.println("\n...Message findById<message1>.............");
        messageService.findById(message1.getId()).
                ifPresentOrElse(
                        System.out::println,
                        () -> {
                            throw new NoSuchElementException("메시지가 없습니다.");
                        }
                );

        System.out.println("\n...no user, no channel test.............");
        //저장되지 않은 유저로 메시지 생성
        try {
            Message message = messageService.saveMessage(channel3, new User("anony user"), "Hello, message");
        } catch (NoSuchElementException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }

        //저장되지 않은 채널로 메시지 생성
        try {
            Message message = messageService.saveMessage(new Channel("anony channel"), user3, "Hello, message");
        } catch (NoSuchElementException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }

        // 메세지 text 수정 후 조회
        messageService.update(message1.getId(), "Welcome, codeit!");
        System.out.println("\n...Message findAll after update<message1>.............");
        messageService.findAll().forEach(System.out::println);


        // 삭제 후 조회
        messageService.delete(message1.getId());
        System.out.println("\n...Message findAll after delete<message1>............");
        messageService.findAll().forEach(System.out::println);


        // FileClear: 테스트 반복할 때 불필요한 데이터 삭제
        userService.delete(user2.getId());
        userService.delete(user3.getId());

        channelService.delete(channel2.getId());
        channelService.delete(channel3.getId());

        messageService.delete(message2.getId());
    }
}
