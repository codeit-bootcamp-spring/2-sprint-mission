package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class JavaApplication {

    static UserRepository userRepository = new FileUserRepository();
    static ChannelRepository channelRepository = new FileChannelRepository();
    static MessageRepository messageRepository = new FileMessageRepository();

    static UserService userService = new BasicUserService(userRepository);
    static ChannelService channelService = new BasicChannelService(userService, channelRepository);
    static MessageService messageService = new BasicMessageService(userService, channelService, messageRepository);

    public static void main(String[] args) {

        //userService.clearUsers();

        //1. User 생성 및 조회
        createUser("hanna1@email.net", "12345*", "hanna1", UserStatus.OFFLINE, UserRole.ADMIN);
        createUser("dog1@email.net", "54321*", "dog1", UserStatus.OFFLINE, UserRole.USER);

        Optional<User> userOpt = userService.selectUserByEmail("hanna@email.net");
        UUID userId = userOpt.get().getId();

        //2. Channel 생성 및 조회
        createChannel(ChannelType.TEXT, "공지사항", "학습공지", userId, UserRole.ADMIN);

        //3. User 수정
        updateUser(userId, "newpassword*", "new_hanna", UserStatus.ONLINE, UserRole.ADMIN);

        //4. Channel 수정
        List<Channel> channels = channelService.selectAllChannels();
        Channel channel = channels.get(0);
        UUID channelId = channel.getId();
        updateChannel(channelId, "공지사항_v2", "학습공지_v2", ChannelType.TEXT, userId);

        //5. Message 생성
        createMessage(userId, channelId, "공지글입니다. 확인해주세요.");

        //6. Message 수정
        List<Message> messages = messageService.selectMessagesByChannel(channelId);
        Message message = messages.get(0);
        UUID messageId = message.getId();
        updateMessasge(messageId, "공지가 취소되었습니다.", userId, channelId);

        //7. Message 삭제
        deleteMessage(messageId, userId, channelId);

        //8. 예외 테스트
        Optional<User> userOpt2 = userService.selectUserByEmail("dog@email.net");
        UUID notAdminUserId = userOpt2.get().getId();
        exceptionTest(channelId, notAdminUserId);

        //9. Channel 삭제
        deleteChannel(channelId, userId);

        //8. User 삭제
        deleteUser(userId);
    }

    private static void createUser(String email, String password, String nickname, UserStatus status, UserRole role) {
        System.out.println("===================== User create Test =========================");
        User user = new User(email, password, nickname, status, role);
        userService.createUser(user);
        System.out.println("등록된 유저 조회: " + userService.selectAllUsers());
        System.out.println("=========================================================");
    }

    private static void createChannel(ChannelType type, String category, String name, UUID userId, UserRole userRole){
        System.out.println("===================== Channel create Test =========================");
        Channel channel = new Channel(type, category, name, userId, userRole);
        channelService.createChannel(channel);
        System.out.println("등록된 채널 조회: " + channelService.selectAllChannels());
        System.out.println("=========================================================");
    }

    private static void updateUser(UUID userId, String password, String nickname, UserStatus status, UserRole userRole) {
        System.out.println("===================== User update Test =========================");
        userService.updateUser(userId, password, nickname, status, userRole);
        System.out.println("수정된 유저 조회: " + userService.selectUserById(userId));
        System.out.println("=========================================================");
    }

    private static void updateChannel(UUID channelId, String name, String category, ChannelType type, UUID userId) {
        System.out.println("===================== Channel update Test =========================");
        channelService.updateChannel(channelId, name, category, type, userId);
        System.out.println("수정된 채널 조회: " + channelService.selectChannelById(channelId));
        System.out.println("=========================================================");
    }

    private static void createMessage(UUID userId, UUID channelId, String content){
        System.out.println("===================== Message create Test =========================");
        Message message = new Message(userId, channelId, content);
        messageService.createMessage(message);
        System.out.println("등록된 메시지 조회: " + messageService.selectMessagesByChannel(channelId));
        System.out.println("=========================================================");
    }

    private static void updateMessasge(UUID messageId, String content, UUID channelId, UUID userId){
        System.out.println("===================== Message update Test =========================");
        messageService.updateMessage(messageId, content, channelId, userId);
        System.out.println("수정된 메시지 조회: " + messageService.selectMessageById(messageId));
        System.out.println("=========================================================");
    }

    private static void deleteMessage(UUID messageId, UUID userId, UUID channelId) {
        System.out.println("===================== Message delete Test ========================");
        messageService.deleteMessage(messageId, userId, channelId);
        System.out.println("삭제된 메시지 조회: " + messageService.selectMessageById(messageId));
        System.out.println("=========================================================");
    }

    private static void deleteChannel(UUID channelId, UUID userId) {
        System.out.println("===================== Channel delete Test ========================");
        channelService.deleteChannel(channelId, userId);
        System.out.println("삭제된 채널 조회: " + channelService.selectChannelById(channelId));
        System.out.println("채널에서 삭제된 메시지 조회: " + messageService.selectMessagesByChannel(channelId));
        System.out.println("=========================================================");
    }
    
    private static void deleteUser(UUID userId) {
        System.out.println("===================== User delete Test ========================");
        userService.deleteUser(userId);
        System.out.println("삭제된 유저 조회: " + userService.selectUserById(userId));
        System.out.println("유저 삭제 후 유저 목록 조회: " + userService.selectAllUsers());
        System.out.println("=========================================================");
    }

    private static void exceptionTest(UUID channelId, UUID userId){
        System.out.println("===================== Exception Test ========================");
        try {
            channelService.addMembers(channelId, Set.of(userId), userId);
        } catch (RuntimeException e) {
            System.out.println("멤버 추가 시 예외 발생: " + e.getMessage());
        }
        
        try {
            Message messageForTest = new Message(userId, channelId, "일반 유저가 메시지를 작성합니다.");
            messageService.createMessage(messageForTest);
        } catch (RuntimeException e) {
            System.out.println("메시지 작성 시 예외 발생: " + e.getMessage());
        }

        System.out.println("=========================================================");
    }

}