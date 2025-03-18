package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.UserStatusService;
import com.sprint.mission.discodeit.service.channelDto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.service.userDto.UserRequest;
import com.sprint.mission.discodeit.service.userDto.UserResponse;

import java.io.IOException;
import java.util.*;

public class JavaApplication {
    static User setupUser(UserService userService) {
        UserRequest req = new UserRequest("woody", "woody@codeit.com", "woody1234", "ex".getBytes());
        return userService.create(req);
    }

    static Channel setupChannel(ChannelService channelService) {
        PublicChannelCreateRequest publicChannelCreateRequest =
                new PublicChannelCreateRequest("공지", "공지 채널입니다.");
        Channel channel = channelService.createPublicChannel(publicChannelCreateRequest);
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        List<String> strs = new ArrayList<>();
        MessageCreateRequest messageCreateRequest =
                new MessageCreateRequest(channel.getId(), author.getId(), "안녕하세요.", strs);
        Message message = messageService.create(messageCreateRequest);
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) throws IOException {
        // 레포지토리 초기화
        UserRepository userRepository = new FileUserRepository(".discodeit");
        ChannelRepository channelRepository = new FileChannelRepository(".discodeit");
        MessageRepository messageRepository = new FileMessageRepository(".discodeit");
        UserStatusRepository userStatusRepository = new FileUserStatusRepository("discodeit");
        BinaryContentRepository binaryContentRepository =
                new FileBinaryContentRepository(".discodeit");
        ReadStatusRepository readStatusRepository =
                new FileReadStatusRepository(".discodeit");

        UserService userService = new BasicUserService(userRepository, userStatusRepository,
                binaryContentRepository);
        ChannelService channelService = new BasicChannelService(channelRepository,
                readStatusRepository,messageRepository);
        MessageService messageService = new BasicMessageService(messageRepository,
                channelRepository, userRepository, binaryContentRepository);


        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);

        messageCreateTest(messageService, channel, user);
    }
}
