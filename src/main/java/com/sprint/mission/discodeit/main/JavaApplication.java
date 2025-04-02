//package com.sprint.mission.discodeit.main;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.UserService;
//import com.sprint.mission.discodeit.service.file.FileChannelService;
//import com.sprint.mission.discodeit.service.file.FileMessageService;
//import com.sprint.mission.discodeit.service.file.FileUserService;
//import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
//
//public class JavaApplication {
//    public static void main(String[] args) {
//        UserService userService = new FileUserService();
//        ChannelService channelService = new FileChannelService();
//        MessageService messageService = new FileMessageService();
//
//        User user1 = userService.createUser("Kim");
//        User user2 = userService.createUser("Lee");
//        User user3 = userService.createUser("Park");
//
//        System.out.println("=== 유저 등록 완료 ===");
//        System.out.println("User 1: " + user1.getId() + " | Name: " + user1.getUsername());
//        System.out.println("User 2: " + user2.getId() + " | Name: " + user2.getUsername());
//        System.out.println("User 3: " + user3.getId() + " | Name: " + user3.getUsername());
//
//        User fetchedUser = userService.getUser(user1.getId());
//        System.out.println("=== 유저 조회 ===");
//        System.out.println("Fetched User: " + fetchedUser.getId() + " | Name: " + fetchedUser.getUsername());
//
//        System.out.println("=== 모든 유저 조회 ===");
//        for (User u : userService.getAllUsers()) {
//            System.out.println("ID: " + u.getId() + " | Name: " + u.getUsername());
//        }
//
//        userService.updateUser(user3.getId(), "Ha");
//        System.out.println("=== 유저 정보 수정 완료 ===");
//        System.out.println("Updated User 3: " + userService.getUser(user3.getId()).getUsername());
//
//
//        System.out.println("=== 유저 삭제 완료 ===");
//
//        try {
//            userService.deleteUser(user2.getId());
//            System.out.println("유저 삭제 완료: " + user2.getId());
//        } catch (IllegalArgumentException e) {
//            System.err.println("에러 발생: " + e.getMessage());
//        }
//
//        System.out.println("=== 삭제 후 모든 유저 조회 ===");
//        for (User u : userService.getAllUsers()) {
//            System.out.println("ID: " + u.getId() + " | Name: " + u.getUsername());
//        }
//
//        System.out.println("-----");
//
//        JCFChannelService channelService1 = JCFChannelService.getInstance();
//        JCFChannelService channelService2 = JCFChannelService.getInstance();
//
//        System.out.println(channelService1);
//        System.out.println(channelService2);
//
//        if (channelService1 == channelService2) {
//            System.out.println("같은 인스턴스입니다! (싱글톤 확인 완료)");
//        } else {
//            System.out.println("다른 인스턴스입니다! (싱글톤 적용 실패)");
//        }
//
//        Channel channel1 = channelService.createChannel("Channel1");
//        Channel channel2 = channelService.createChannel("Channel2");
//        Channel channel3 = channelService.createChannel("Channel3");
//
//        System.out.println("=== 채널 등록 완료 ===");
//        System.out.println("Channel 1: " + channel1.getId() + " | Name: " + channel1.getChannelName());
//        System.out.println("Channel 2: " + channel2.getId() + " | Name: " + channel2.getChannelName());
//        System.out.println("Channel 3: " + channel3.getId() + " | Name: " + channel3.getChannelName());
//
//        Channel fetchedChannel = channelService.getChannel(channel1.getId());
//        System.out.println("=== 채널 조회 ===");
//        System.out.println("Fetched Channel: " + fetchedChannel.getId() + " | Name: " + fetchedChannel.getChannelName());
//
//        System.out.println("=== 모든 채널 조회 ===");
//        for (Channel c : channelService.getAllChannels()) {
//            System.out.println("ID: " + c.getId() + " | Name: " + c.getChannelName());
//        }
//
//        channelService.updateChannel(channel1.getId(), "Announcements");
//        System.out.println("=== 채널 정보 수정 완료 ===");
//        System.out.println("Updated Channel 1: " + channelService.getChannel(channel1.getId()).getChannelName());
//
//        System.out.println("=== 채널 삭제 완료 ===");
//
//        try {
//            channelService.deleteChannel(channel2.getId());
//            System.out.println("채널 삭제 완료: " + channel2.getId());
//        } catch (IllegalArgumentException e) {
//            System.err.println("에러 발생: " + e.getMessage());
//        }
//
//        System.out.println("=== 삭제 후 모든 채널 조회 ===");
//        for (Channel c : channelService.getAllChannels()) {
//            System.out.println("ID: " + c.getId() + " | Name: " + c.getChannelName());
//        }
//
//        System.out.println("-----");
//
//        Message message1 = messageService.createMessage(channel1.getId(), user1.getId(), "Hello World");
//        Message message2 = messageService.createMessage(channel1.getId(), user3.getId(), "Hi Kim!");
//        Message message3 = messageService.createMessage(channel3.getId(), user1.getId(), "Java");
//
//        System.out.println("=== 메세지 목록 ===");
//        messageService.getAllMessages().forEach(msg ->
//                System.out.println("[" + msg.getChannelId() + "] " + msg.getContent()));
//
//        messageService.updateMessage(message1.getId(), "Hello CodeIt! (edited)");
//        System.out.println("메세지 변경: " + messageService.getMessage(message1.getId()).getContent());
//
//        System.out.println("=== 메세지 삭제 ===");
//        try {
//            messageService.deleteMessage(message2.getId());
//            System.out.println("메세지 삭제 완료: " + message2.getId());
//        } catch (IllegalArgumentException e) {
//            System.err.println("에러 발생: " + e.getMessage());
//        }
//
//        System.out.println("=== 메세지 목록 (삭제 후) ===");
//        messageService.getAllMessages().stream()
//                .map(msg -> "[" + msg.getChannelId() + "] " + msg.getUserId() + ": " + msg.getContent())
//                .forEach(System.out::println);
//    }
//}
