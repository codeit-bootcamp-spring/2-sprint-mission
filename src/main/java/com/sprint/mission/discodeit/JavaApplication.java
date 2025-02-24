package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFMessageService messageService = new JCFMessageService();
        JCFChannelService channelService = new JCFChannelService(messageService);

        userSetting(userService);
        channelSetting(channelService, userService, messageService);
        messageSetting(channelService, userService, messageService);
    }

    public static void userSetting(JCFUserService userService) {
        // 사용자 등록
        User user1 = new User("codeit", "코드잇", "codeit@codeit.com", "123123");
        User user2 = new User("LYN", "이유나", "LYN@codeit.com", "456456");
        userService.create(user1);
        userService.create(user2);

        // 모든 사용자 조회
        System.out.println("* 등록된 사용자 리스트 *");
        List<User> users = userService.findAll();
        for (User u : users) {
            System.out.println("User ID: " + u.getUserId() + ", Name: " + u.getUserName() + ", Email: " + u.getUserEmail());
        }

        // 특정 데이터 조회
        User userFound = userService.find("codeit");
        if (userFound != null) {
            System.out.println("* 검색된 사용자 정보 *");
            System.out.println("User ID: " + userFound.getUserId());
            System.out.println("User Name: " + userFound.getUserName());
            System.out.println("User Email: " + userFound.getUserEmail());
        } else {
            System.out.println("* 검색된 사용자: 없음");
        }
        // 수정
        System.out.println("* 수정된 사용자 정보 *");
        user2.setUserEmail("yuna@codeit.com");
        try {
            User updatedUser = userService.update(user2.getUserId(), user2); // 수정된 사용자 정보
            if (updatedUser != null) {
                System.out.println("User ID: " + updatedUser.getUserId());
                System.out.println("User Name: " + updatedUser.getUserName());
                System.out.println("User Email: " + updatedUser.getUserEmail());
            } else {
                System.out.println("수정된 사용자가 없습니다.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("업데이트 중 오류 발생: " + e.getMessage());
        }
        // 삭제
        System.out.println("* 삭제된 후 남은 사용자 리스트 *");
        userService.delete("codeit");
        List<User> deleteUsers = userService.findAll();
        for (User u : deleteUsers) {
            System.out.println("User ID: " + u.getUserId() + ", Name: " + u.getUserName() + ", Email: " + u.getUserEmail());
        }
    }

    public static void channelSetting(JCFChannelService channelService, JCFUserService userService, JCFMessageService messageService) {
        // 채널 등록
        Channel channel1 = new Channel("질문방");
        Channel channel2 = new Channel("수다방");
        channelService.create(channel1);
        channelService.create(channel2);

        // 모든 채널 조회
        System.out.println("* 등록된 채널 리스트 *");
        List<Channel> Channels = channelService.findAll();
        for (Channel c : Channels) {
            System.out.println("채널명: " + c.getChannelName() + " (" + c.getUserCount() + "명)");
        }

        // 채널에 사용자 추가
        User user1 = userService.find("LYN");
        channelService.addUserToChannel("질문방", user1);

        // 특정 채팅방 조회
        System.out.println("* 검색된 채팅방 상세 정보 *");
        Channel channelFound = channelService.findByChannelName("질문방");
            if (channelFound != null) {
                System.out.print("채널명 : " + channelFound.getChannelName() + " (" + channelFound.getUserCount() + "명)");
                if (channelFound.getUsers() != null && !channelFound.getUsers().isEmpty()) {
                    for (User user : channelFound.getUsers()) {
                        System.out.println(" - 참여 사용자 : " + user.getUserName() + "(" + user.getUserId() + ")");
                    }
                } else {
                    System.out.println(" - 참여 사용자 : 없음");
                }
            } else {
                System.out.println("해당 채널은 존재하지 않습니다.");
            }

        // 사용자가 속한 채팅방 조회
        String user = "LYN";
        List<Channel> channelsForUser = channelService.find(user);
        System.out.println("* " + user + " 속한 채널 리스트 *");
        for (Channel channel : channelsForUser) {
            System.out.println("채널명 : " + channel.getChannelName() + " (" + channel.getUserCount() + "명)");
        }

        // 채널명 수정
        System.out.println("* 수정된 채널 리스트 *");
        channel1.setChannelName("공부방");
        try {
            channelService.update(channel2.getChannelName(), channel2);
        } catch (IllegalArgumentException e) {
            System.out.println("업데이트 중 오류 발생: " + e.getMessage());
        }
        List<Channel> updateChannels = channelService.findAll();
        for (Channel c : updateChannels) {
            System.out.println("채널명 : " + c.getChannelName() + " (" + c.getUserCount() + "명)");
        }

        // 채팅방 삭제
        System.out.println("* 삭제된 후 남은 채널 리스트 *");
        channelService.delete("수다방");
        List<Channel> deleteChannels = channelService.findAll();
        for (Channel c : deleteChannels) {
            System.out.println("채널명 : " + c.getChannelName() + " (" + c.getUserCount() + "명)");
        }

        messageSetting(channelService, userService, messageService);
    }

    public static void messageSetting(JCFChannelService channelService, JCFUserService userService, JCFMessageService messageService) {
        // 메시지 생성
        User user1 = userService.find("LYN");
        Channel channel = channelService.findByChannelName("공부방");

        if (channel != null && user1 != null) {
            Message message1 = new Message(user1, "안녕하세요, 질문 있어요!", channel);
            messageService.create(message1);
            System.out.println("메시지를 전송하였습니다.");
        }

        // 채널의 모든 메시지 출력
        Channel channelFound = channelService.findByChannelName("공부방");
        if (channelFound != null) {
            List<Message> messages = messageService.findByChannel(channelFound);
            System.out.println("* " + channelFound.getChannelName() + " 채널에 있는 메시지 *");
            for (Message message : messages) {
                System.out.println(message.getUser().getUserName() + " : " + message.getMessage());
            }
        }

        // 특정 메시지 삭제
        System.out.println("* 특정 메시지 삭제 *");
        messageService.delete("안녕하세요, 질문 있어요!");

        // 메시지 삭제 후 채널의 모든 메시지 출력
        System.out.println("* 메시지 삭제 후, " + channelFound.getChannelName() + " 채널에 있는 메시지 *");
        List<Message> messagesAfterDelete = messageService.findByChannel(channelFound);
        for (Message message : messagesAfterDelete) {
            System.out.println(message.getUser().getUserName() + " : " + message.getMessage());
        }
    }
}
