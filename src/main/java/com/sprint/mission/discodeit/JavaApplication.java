package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.jcf.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args){

        // user service 테스트
        UserService userService = new JCFUserService();

        // 등록
        System.out.println("\n\n=== User 등록 테스트 ===");
        System.out.println("등록 결과:");
        User user1 = userService.createUser("박상혁");
        User user2 = userService.createUser("정종현");
        User user3 = userService.createUser("김세은");
        User user4 = userService.createUser("박상혁");
        userService.getAllUsers().forEach(System.out::println);

        // 조회 (단건)
        System.out.println("\n=== User 조회(단건) 테스트: user1 id로 조회, 없는 id로 조회 ===");
        Optional<User> foundUser = userService.getUserById(user1.getId());
        Optional<User> notFoundUser = userService.getUserById(UUID.randomUUID());
        foundUser.ifPresentOrElse(System.out::println, () -> System.out.println("해당 User를 찾을 수 없습니다."));
        notFoundUser.ifPresentOrElse(System.out::println, () -> System.out.println("해당 User를 찾을 수 없습니다."));

        // 조회 (다건)
        System.out.println("\n=== User 조회(다건) 테스트: 이름이 \"박상혁\"인 유저 조회 ===");
        List<User> users = userService.getUsersByName("박상혁");
        for(User u : users){
            System.out.println(u);
        }

        // 수정
        System.out.println("\n=== User 수정 테스트: user4 이름을 \"최성현\"으로 수정 ===");
        System.out.println("수정 전: " + user4);
        userService.updateUserName(user4.getId(), "최성현");
        // 수정된 데이터 조회
        userService.getUserById(user4.getId()).ifPresent(user -> System.out.println("수정 후: " + user));

        // 삭제
        System.out.println("\n=== User 삭제 테스트: user3 삭제 ===");
        userService.deleteUser(user3.getId());
        // 조회를 통해 삭제되었는지 확인
        userService.getAllUsers().forEach(System.out::println);
        

        // channel service 테스트 (UserService와 동일한 방식으로 테스트)
        ChannelService channelService = new JCFChannelService();

        // 등록
        System.out.println("\n\n=== Channel 등록 테스트 ===");
        System.out.println("등록 결과:");
        Channel channel1 = channelService.createChannel("channel1");
        Channel channel2 = channelService.createChannel("channel2");
        Channel channel3 = channelService.createChannel("channel3");
        channelService.getAllChannels().forEach(System.out::println);

        // 조회 (단건)
        System.out.println("\n=== Channel 조회(단건) 테스트 ===");
        Optional<Channel> foundChannel = channelService.getChannelById(channel1.getId());
        Optional<Channel> notFoundChannel = channelService.getChannelById(UUID.randomUUID());

        System.out.print("channel1 id로 조회: ");
        foundChannel.ifPresentOrElse(System.out::println, () -> System.out.println("해당 Channel을 찾을 수 없습니다."));
        System.out.print("없는 id로 조회: ");
        notFoundChannel.ifPresentOrElse(System.out::println, () -> System.out.println("해당 Channel을 찾을 수 없습니다."));
        
        foundChannel = channelService.getChannelByName("channel1");
        notFoundChannel = channelService.getChannelByName("notExistName");

        System.out.println("channel1 이름으로 조회: ");
        foundChannel.ifPresentOrElse(System.out::println, () -> System.out.println("해당 Channel을 찾을 수 없습니다."));
        System.out.print("없는 이름으로 조회: ");
        notFoundChannel.ifPresentOrElse(System.out::println, () -> System.out.println("해당 Channel을 찾을 수 없습니다."));
        
        // 조회 (다건)
        System.out.println("\n=== Channel 조회(다건) 테스트: 모든 채널 조회 ===");
        channelService.getAllChannels().forEach(System.out::println);

        // 수정
        System.out.println("\n=== Channel 수정 테스트: channel2 이름을 channel123으로 수정 ===");
        System.out.println("수정 전: " + channel2);
        channelService.updateChannelName(channel2.getId(), "channel123");
        // 수정된 데이터 조회
        channelService.getChannelById(channel2.getId()).ifPresent(channel -> System.out.println("수정 후: " + channel));

        // 삭제
        System.out.println("\n=== Channel 삭제 테스트: channel3 삭제 ===");
        channelService.deleteChannel(channel3.getId());
        // 조회를 통해 삭제되었는지 확인
        channelService.getAllChannels().forEach(System.out::println);


        // message service 테스트 (UserService와 동일한 방식으로 테스트)
        MessageService messageService = new JCFMessageService();

        // 등록
        System.out.println("\n\n=== Message 등록 테스트 ===");
        System.out.println("등록 결과:");
        Message message1 = messageService.createMessage(user1.getId(), channel1.getId(), user1.getName() + "님이 " + channel1.getName() + " 채널에서 메세지를 작성했습니다.");
        Message message2 = messageService.createMessage(user2.getId(), channel2.getId(), user2.getName() + "님이 " + channel2.getName() + " 채널에서 메세지를 작성했습니다.");
        Message message3 = messageService.createMessage(user3.getId(), channel2.getId(), user3.getName() + "님이 " + channel2.getName() + " 채널에서 메세지를 작성했습니다.");
        messageService.getAllMessages().forEach(System.out::println);

        // 조회 (단건)
        System.out.println("\n=== Message 조회(단건) 테스트: message1 id로 조회, 없는 id로 조회 ===");
        Optional<Message> foundMessage = messageService.getMessageById(message1.getId());
        Optional<Message> notFoundMessage = messageService.getMessageById(UUID.randomUUID());
        foundMessage.ifPresentOrElse(System.out::println, () -> System.out.println("해당 Message를 찾을 수 없습니다."));
        notFoundMessage.ifPresentOrElse(System.out::println, () -> System.out.println("헤딩 Message를 찾을 수 없습니다."));

        // 조회 (다건)
        System.out.println("\n=== Message 조회(다건) 테스트: 모든 메세지 조회 ===");
        messageService.getAllMessages().forEach(System.out::println);

        // 수정
        System.out.println("\n=== Message 수정 테스트: message2의 content를 \"수정된 메세지입니다.\"라고 수정 ===");
        System.out.println("수정 전: " + message2);
        messageService.updateMessageContent(message2.getId(), "수정된 메세지입니다.");
        // 수정된 데이터 조회
        messageService.getMessageById(message2.getId()).ifPresent(message -> System.out.println("수정 후: " + message));

        // 삭제
        System.out.println("\n=== Message 삭제 테스트: message3 삭제 ===");
        messageService.deleteMessage(message3.getId());
        // 조회를 통해 삭제되었는지 확인
        messageService.getAllMessages().forEach(System.out::println);
    }
}
