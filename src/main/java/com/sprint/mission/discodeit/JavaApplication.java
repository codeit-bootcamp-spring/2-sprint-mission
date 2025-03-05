package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
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
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import java.nio.file.Path;

public class JavaApplication {
    public static void main(String[] args) {
        // 파일 저장 경로 설정
        Path userDirectory = Path.of("data/users");
        Path channelDirectory = Path.of("data/channels");
        Path messageDirectory = Path.of("data/messages");

//        // File 기반 서비스 사용
//        UserService userService = FileUserService.getInstance(userDirectory);
//        ChannelService channelService = FileChannelService.getInstance(channelDirectory);
//        MessageService messageService = FileMessageService.getInstance(messageDirectory, userService, channelService);

        UserRepository jcfUserRepository = JCFUserRepository.getInstance();
        UserRepository fileUserRepository = FileUserRepository.getInstance(userDirectory);


        ChannelRepository jcfChannelRepository = JCFChannelRepository.getInstance();
        ChannelRepository fileChannelRepository = FileChannelRepository.getInstance(channelDirectory);

        MessageRepository jcfMessageRepository = JCFMessageRepository.getInstance();
        MessageRepository fileMessageRepository = FileMessageRepository.getInstance(messageDirectory);


        UserService userService = BasicUserService.getInstance(fileUserRepository);
        ChannelService channelService = BasicChannelService.getInstance(fileChannelRepository);
        MessageService messageService = BasicMessageService.getInstance(fileMessageRepository, userService, channelService);

        System.out.println("========== 유저 등록 ==========");
        User user1 = new User("Alice", "alice@gmail.com", "12345");
        User user2 = new User("Minho", "minho@gmail.com", "12346");

        userService.create(user1);
        userService.create(user2);

        System.out.println("========== 유저 조회 ==========");
        System.out.println("단건 조회: " + userService.findById(user1.getId()));

        System.out.println("다건 조회:");
        userService.findAll().forEach(System.out::println);

        System.out.println("========== 유저 수정 ==========");
        userService.update(user1.getId(), "Tomas", "tomas@gmail.com", "12345");
        System.out.println("수정된 데이터 조회: " + userService.findById(user1.getId()));

        System.out.println("========== 유저 삭제 ==========");
        userService.delete(user1.getId());
        System.out.println("삭제 후 유저 목록:");
        userService.findAll().forEach(System.out::println);

        System.out.println("========== 채널 등록 ==========");
        Channel channel1 = new Channel(ChannelType.PUBLIC, "코드잇 스프린트 1기", "스프린트 1기입니다.");
        Channel channel2 = new Channel(ChannelType.PUBLIC, "코드잇 스프린트 2기", "스프린트 2기입니다.");

        channelService.create(channel1);
        channelService.create(channel2);

        System.out.println("========== 채널 조회 ==========");
        System.out.println("단건 조회: " + channelService.findById(channel1.getId()));

        System.out.println("다건 조회:");
        channelService.findAll().forEach(System.out::println);

        System.out.println("========== 채널 수정 ==========");
        channelService.update(channel2.getId(), "코드잇 스프린트 Spring 백엔드 2기", "스프린트 2기입니다.");
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
