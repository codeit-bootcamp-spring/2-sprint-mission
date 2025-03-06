package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelrepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.List;
import java.util.NoSuchElementException;


public class JavaApplication {
    static void userCRUDTest(UserService userService) {
        // 생성
        User user = userService.create("user1", "user1@codeit.com", "user1234");
        System.out.println("유저 생성: " + user.getId());
        // 조회
        User foundUser = userService.find(user.getId());
        System.out.println("유저 조회(단건): " + foundUser.getId());
        List<User> foundUsers = userService.findAll();
        System.out.println("유저 조회(다건): " + foundUsers.size());
        // 수정
        User updatedUser = userService.update(user.getId(), null, "updateUser1@codeit.com", null);
        System.out.println("유저 수정: " + String.join("/", updatedUser.getUsername(), updatedUser.getEmail()));
        // 삭제
        userService.delete(user.getId());
        List<User> foundUsersAfterDelete = userService.findAll();
        System.out.println("유저 삭제: " + foundUsersAfterDelete.size());
    }

    static void channelCRUDTest(ChannelService channelService) {
        // 생성
        Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        System.out.println("채널 생성: " + channel.getId());
        // 조회
        Channel foundChannel = channelService.find(channel.getId());
        System.out.println("채널 조회(단건): " + foundChannel.getId());
        List<Channel> foundChannels = channelService.findAll();
        System.out.println("채널 조회(다건): " + foundChannels.size());
        // 수정
        Channel updatedChannel = channelService.update(channel.getId(), "공지사항", null);
        System.out.println("채널 수정: " + String.join("/", updatedChannel.getName(), updatedChannel.getDescription()));
        // 삭제
        channelService.delete(channel.getId());
        List<Channel> foundChannelsAfterDelete = channelService.findAll();
        System.out.println("채널 삭제: " + foundChannelsAfterDelete.size());
    }

    static void messageCRUDTest(MessageService messageService, ChannelService channelService, UserService userService) {
        try{
            // 생성
            Channel channel = channelService.create(ChannelType.PUBLIC, "테스트용 채널", "테스트용 채널입니다");
            User user = userService.create("user1", "user1@codeit.com", "user1234");
            Message message = messageService.create("안녕하세요.", channel.getId(), user.getId());
            System.out.println("메시지 생성: " + message.getId());
            // 조회
            try {
                Message foundMessage = messageService.find(message.getId());
                System.out.println("메시지 조회(단건): " + foundMessage.getId());
            } catch (NoSuchElementException e) {
                System.out.println("메시지 조회 실패: " + e.getMessage());
            }
            List<Message> foundMessages = messageService.findAll();
            System.out.println("메시지 조회(다건): " + foundMessages.size());
            // 수정
            try{
                Message updatedMessage = messageService.update(message.getId(), "반갑습니다.");
                System.out.println("메시지 수정: " + updatedMessage.getContent());
            } catch (NoSuchElementException e) {
                System.out.println("메시지 수정 실패: " + e.getMessage());
            }
            //삭제
            try {
                messageService.delete(message.getId());
                System.out.println("메시지 삭제 성공");
            } catch (NoSuchElementException e) {
                System.out.println("메시지 삭제 실패: " + e.getMessage());
            }

            List<Message> foundMessagesAfterDelete = messageService.findAll();
            System.out.println("메시지 삭제 후 조회: " + foundMessagesAfterDelete.size());
            //테스트 후 삭제
            channelService.delete(channel.getId());
            userService.delete(user.getId());
        } catch (NoSuchElementException e) {
            System.out.println("메시지 CRUD 테스트 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

    }

    static User setupUser(UserService userService) {
        User user = userService.create("user1", "user1@codeit.com", "user1234");
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        String repositoryType = System.getenv("REPOSITORY_TYPE");
        if (repositoryType == null) {
            repositoryType = "jcf";
        }

        UserRepository userRepository;
        ChannelRepository channelRepository;
        MessageRepository messageRepository;

        if (repositoryType.equalsIgnoreCase("jcf")) {
            userRepository = new JCFUserRepository();
            channelRepository = new JCFChannelRepository();
            messageRepository = new FileMessageRepository();
        } else if (repositoryType.equalsIgnoreCase("file")) {
            userRepository = new FileUserRepository();
            channelRepository = new FileChannelrepository();
            messageRepository = new FileMessageRepository();
        } else{
            throw new IllegalArgumentException("Unsupported repository type: " + repositoryType);
        }

        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository, channelService, userService);

        //테스트
        userCRUDTest(userService);
        channelCRUDTest(channelService);
        messageCRUDTest(messageService, channelService, userService);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);

        // 테스트
        messageCreateTest(messageService, channel, user);
        userService.delete(user.getId());
        channelService.delete(channel.getId());
    }
}
