package org.example;


import com.sprint.mission.discodeit.convertfile.entity.Channel;
import com.sprint.mission.discodeit.convertfile.fileservice.service.FileChannelService;
import com.sprint.mission.discodeit.convertfile.fileservice.service.FileMessageService;
import com.sprint.mission.discodeit.convertfile.fileservice.service.FileUserService;
import com.sprint.mission.discodeit.convertfile.entity.ChannelType;
import com.sprint.mission.discodeit.convertfile.entity.Message;
import com.sprint.mission.discodeit.convertfile.entity.User;
import com.sprint.mission.discodeit.convertfile.fileservice.ChannelService;
import com.sprint.mission.discodeit.convertfile.fileservice.MessageService;
import com.sprint.mission.discodeit.convertfile.fileservice.UserService;

import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = new FileUserService();
        ChannelService channelService = new FileChannelService();
        MessageService messageService = new FileMessageService(channelService, userService);
        User user = userService.create("woody", "woody@codeit.com", "woody1234");
        System.out.println("유저 생성: " + user.getId());
        Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        System.out.println("채널 생성: " + channel.getId());
        Message message = messageService.create("안녕하세요.", channel.getId(), user.getId());
        System.out.println("메시지 생성: " + message.getId());
        System.out.println("\n--- 저장된 데이터 목록 ---");
        System.out.println("유저 목록:");
        List<User> users = userService.findAll();
        for (User u : users) {
            System.out.println("- " + u.getUsername() + " (" + u.getEmail() + ")");
        }

        System.out.println("\n채널 목록:");
        List<Channel> channels = channelService.findAll();
        for (Channel c : channels) {
            System.out.println("- " + c.getName() + " (" + c.getType() + "): " + c.getDescription());
        }

        System.out.println("\n메시지 목록:");
        List<Message> messages = messageService.findAll();
        for (Message m : messages) {
            System.out.println("- " + m.getContent());
        }

        System.out.println("\n프로그램이 종료됩니다. 다음 실행 시 데이터가 파일에서 자동으로 로드됩니다.");
    }
}