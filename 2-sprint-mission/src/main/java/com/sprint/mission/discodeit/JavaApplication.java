package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.jcf.JCFChannelService;
import com.sprint.mission.discodeit.jcf.JCFMessageService;
import com.sprint.mission.discodeit.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JavaApplication {
    public static void main(String[] args) {

        UserService userService = JCFUserService.getInstance();
        ChannelService channelService = JCFChannelService.getInstance(userService);
        MessageService messageService = JCFMessageService.getInstance(userService, channelService);

        //1. User 생성 및 조회
        System.out.println("===================== User Test =========================");
        User user1 = new User("hanna@email.net", "12345*", "hanna", UserStatus.OFFLINE, UserRole.ADMIN);
        User user2 = new User("egg@email.com", "egg12312", "egg", UserStatus.OFFLINE, UserRole.USER);

        userService.createUser(user1);
        userService.createUser(user2);
        System.out.println("등록된 유저 조회: " + userService.selectAllUsers());

        System.out.println("=========================================================");

        //2. Channel 생성 및 조회
        System.out.println("===================== Channel Test ======================");
        Channel channel1 = new Channel(ChannelType.TEXT, "공지사항", "학습공지", user1.getId(), UserRole.ADMIN);

        channelService.createChannel(channel1);
        System.out.println("등록된 채널 조회: " + channelService.selectAllChannels());

        System.out.println("=========================================================");

        //3. User 수정
        System.out.println("===================== User 수정 Test ======================");
        userService.updateUser(user1.getId(), "newpass*", "new_hanna", UserStatus.ONLINE, UserRole.ADMIN);
        System.out.println("User1 수정 후 조회: " + userService.selectUserById(user1.getId()));

        System.out.println("=========================================================");

        //4. Channel 수정
        System.out.println("===================== Channel 수정 Test ======================");
        channelService.updateChannel(channel1.getId(), "공지사항_v2", "학습공지_v2", null, user1.getId());
        System.out.println("Channel 수정 후 조회: " + channelService.selectChannelById(channel1.getId()));

        System.out.println("=========================================================");

        //5. Message 생성, 조회, 수정, 삭제
        System.out.println("===================== Message Test ======================");
        Message message1 = new Message(user1.getId(), channel1.getId(), "공지글입니다. 확인해주세요.");

        messageService.createMessage(message1);
        System.out.println("등록된 메시지 조회: " + messageService.selectMessagesByChannel(channel1.getId()));

        messageService.updateMessage(message1.getId(), "공지가 변경되었습니다.", user1.getId(), channel1.getId());
        System.out.println("수정 후 메시지 조회: " + messageService.selectMessageById(message1.getId()));

        messageService.deleteMessage(message1.getId(), user1.getId(), channel1.getId());
        System.out.println("삭제 후 메시지 조회: " + messageService.selectMessageById(message1.getId()));

        System.out.println("=========================================================");

        //6. 예외 테스트 (일반 유저가 관리 기능을 시도할 때)
        System.out.println("===================== Exception Test ======================");
        try {
            Message message2 = new Message(user2.getId(), channel1.getId(), "일반 유저가 메시지를 작성합니다.");
            messageService.createMessage(message2);
        } catch (RuntimeException e) {
            System.out.println("예외 발생: " + e.getMessage());
        }

        try {
            channelService.addMembers(channel1.getId(), Set.of(user2.getId()), user2.getId());
        } catch (RuntimeException e) {
            System.out.println("예외 발생: " + e.getMessage());
        }

        System.out.println("=========================================================");

        //7. Channel 삭제
        System.out.println("===================== Channel 삭제 Test ======================");
        channelService.deleteChannel(channel1.getId(), user1.getId());
        System.out.println("채널 삭제 후 조회: " + channelService.selectChannelById(channel1.getId()));
        System.out.println("채널 삭제 후 메시지 조회: " + messageService.selectMessagesByChannel(channel1.getId()));

        System.out.println("=========================================================");

        //8. User 삭제
        System.out.println("===================== User 삭제 Test ======================");
        userService.deleteUser(user1.getId());
        System.out.println("User1 삭제 후 조회: " + userService.selectUserById(user1.getId()));

        userService.deleteUser(user2.getId());
        System.out.println("User2 삭제 후 조회: " + userService.selectUserById(user2.getId()));

        System.out.println("=========================================================");

    }
}
