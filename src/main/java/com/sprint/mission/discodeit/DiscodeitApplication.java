package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
//    static void userCRUDTest(UserService userService) {
//        // 생성
//        try {
//            UserCreateDto userCreateDto = new UserCreateDto("sang", "sang@codeit.com", "sang1234", null);
//            User user = userService.create(userCreateDto);
//            System.out.println("유저 생성: " + user.getId());
//        } catch (RuntimeException e) {
//            System.out.println("유저 생성 실패: " + e.getMessage());
//        }
//
//        // 동일한 유저 email로 생성
//        try {
//            UserCreateDto userCreateDto = new UserCreateDto("park", "sang@codeit.com", "park1234", null);
//            User user = userService.create(userCreateDto);
//            System.out.println("유저 생성: " + user.getId());
//        } catch (RuntimeException e) {
//            System.out.println("유저 생성 실패: " + e.getMessage());
//        }
//
//        // 조회
//        List<UserDto> foundUsers = userService.findAll();
//        System.out.println("유저 조회(다건): " + foundUsers.size());
//
//        try {
//            UserCreateDto userCreateDto = new UserCreateDto("findTest", "findTest@codeit.com", "test1234", null);
//            User user = userService.create(userCreateDto);
//            UserDto foundUser = userService.findById(user.getId());
//            System.out.println("유저 조회(단건): " + foundUser.id());
//        } catch (RuntimeException e) {
//            System.out.println("유저 조회(단건) 실패: " + e.getMessage());
//        }
//
//        // 존재하지 않는 ID 조회
//        try {
//            UserDto foundUser = userService.findById(UUID.randomUUID());
//            System.out.println("유저 조회(단건): " + foundUser.id());
//        } catch (RuntimeException e) {
//            System.out.println("유저 조회(단건) 실패: " + e.getMessage());
//        }
//
//        // 수정
//        try {
//            UserCreateDto userCreateDto = new UserCreateDto("updateTest", "updateTest@codeit.com", "updateTest1234",
//                    null);
//            User user = userService.create(userCreateDto);
//            UserUpdateDto userUpdateDto = new UserUpdateDto(user.getId(), null, null, "woody5678", null);
//            User updatedUser = userService.update(userUpdateDto);
//            System.out.println("유저 수정: " + String.join("/", updatedUser.getUsername(), updatedUser.getEmail(),
//                    updatedUser.getPassword(),
//                    updatedUser.getProfileId() == null ? "null" : updatedUser.getProfileId().toString()));
//        } catch (RuntimeException e) {
//            System.out.println("유저 수정 실패: " + e.getMessage());
//        }
//
//        // 존재하지 않는 ID로 수정
//        try {
//            UserUpdateDto userUpdateDto = new UserUpdateDto(UUID.randomUUID(), null, null, "woody5678", null);
//            User updatedUser = userService.update(userUpdateDto);
//            System.out.println("유저 수정: " + String.join("/", updatedUser.getUsername(), updatedUser.getEmail(),
//                    updatedUser.getPassword(),
//                    updatedUser.getProfileId() == null ? "null" : updatedUser.getProfileId().toString()));
//        } catch (RuntimeException e) {
//            System.out.println("유저 수정 실패: " + e.getMessage());
//        }
//
//        // 삭제
//        UserCreateDto userCreateDto = new UserCreateDto("deletedTest", "deletedTest@codeit.com", "deletedTest1234",
//                null);
//        User deletedUser = userService.create(userCreateDto);
//        List<UserDto> foundUsersBeforeDelete = userService.findAll();
//        System.out.println("유저 삭제 전: " + foundUsersBeforeDelete.size());
//        userService.delete(deletedUser.getId());
//        List<UserDto> foundUsersAfterDelete = userService.findAll();
//        System.out.println("유저 삭제 후: " + foundUsersAfterDelete.size());
//    }
//
//    static void channelCRUDTest(ChannelService channelService, UserService userService) {
//        // 생성
//
//        // public 채널 생성
//        try {
//            ChannelCreatePublicDto channelCreatePublicDto = new ChannelCreatePublicDto("Public 채널", "Public 채널입니다.");
//            Channel channel = channelService.createPublic(channelCreatePublicDto);
//            System.out.println("채널 생성: " + channel.getId());
//        } catch (RuntimeException e) {
//            System.out.println("채널 생성 실패: " + e.getMessage());
//        }
//
//        // private 채널 생성
//        try {
//            ChannelCreatePrivateDto channelCreatePrivateDto = new ChannelCreatePrivateDto(new ArrayList<>());
//            Channel channel = channelService.createPrivate(channelCreatePrivateDto);
//            System.out.println("채널 생성: " + channel.getId());
//        } catch (RuntimeException e) {
//            System.out.println("채널 생성 실패: " + e.getMessage());
//        }
//
//        // 동일한 채널 이름으로 생성
//        try {
//            ChannelCreatePublicDto channelCreatePublicDto = new ChannelCreatePublicDto("Public 채널",
//                    "Public 채널 또 만듭니다.");
//            Channel channel = channelService.createPublic(channelCreatePublicDto);
//            System.out.println("채널 생성: " + channel.getId());
//        } catch (RuntimeException e) {
//            System.out.println("채널 생성 실패: " + e.getMessage());
//        }
//
//        // 조회
//        UserCreateDto userCreateDto = new UserCreateDto("test", "test@codeit.com", "test", null);
//        User user = userService.create(userCreateDto);
//
//        ChannelCreatePrivateDto channelCreatePrivateDto = new ChannelCreatePrivateDto(List.of(user));
//        Channel privateChannel = channelService.createPrivate(channelCreatePrivateDto);
//        ChannelCreatePublicDto channelCreatePublicDto = new ChannelCreatePublicDto("testPublic", "testPublic");
//        Channel publicChannel = channelService.createPublic(channelCreatePublicDto);
//
//        List<ChannelDto> foundChannels = channelService.findAllByUserId(user.getId());
//        System.out.println("채널 조회(다건): " + foundChannels.size());
//
//        try {
//            ChannelDto foundChannel = channelService.findById(privateChannel.getId());
//            System.out.println("채널 조회(단건): " + foundChannel.id());
//        } catch (RuntimeException e) {
//            System.out.println("채널 조회(단건) 실패: " + e.getMessage());
//        }
//
//        // 존재하지 않는 ID 조회
//        try {
//            ChannelDto foundChannel = channelService.findById(UUID.randomUUID());
//            System.out.println("채널 조회(단건): " + foundChannel.id());
//        } catch (RuntimeException e) {
//            System.out.println("채널 조회(단건) 실패: " + e.getMessage());
//        }
//
//        // 수정
//        try {
//            ChannelUpdateDto channelUpdateDto = new ChannelUpdateDto(publicChannel.getId(), "수정 성공 테스트",
//                    "수정 성공 테스트 채널입니다.");
//            Channel updatedChannel = channelService.update(channelUpdateDto);
//            System.out.println("채널 수정: " + String.join("/", updatedChannel.getName(), updatedChannel.getDescription()));
//        } catch (RuntimeException e) {
//            System.out.println("채널 수정 실패: " + e.getMessage());
//        }
//
//        // 존재하지 않는 ID로 수정
//        try {
//            Channel updatedChannel = channelService.update(
//                    new ChannelUpdateDto(UUID.randomUUID(), "수정 실패 테스트", "수정 실패 테스트 채널입니다."));
//            System.out.println("채널 수정: " + String.join("/", updatedChannel.getName(), updatedChannel.getDescription()));
//        } catch (RuntimeException e) {
//            System.out.println("채널 수정 실패: " + e.getMessage());
//        }
//
//        // 삭제
//        List<ChannelDto> foundChannelsBeforeDelete = channelService.findAllByUserId(user.getId());
//        System.out.println("채널 삭제 전: " + foundChannelsBeforeDelete.size());
//        channelService.delete(publicChannel.getId());
//        List<ChannelDto> foundChannelsAfterDelete = channelService.findAllByUserId(user.getId());
//        System.out.println("채널 삭제: " + foundChannelsAfterDelete.size());
//    }
//
//    static void messageCRUDTest(MessageService messageService, UserService userService, ChannelService channelService) {
//        UserCreateDto userCreateDto = new UserCreateDto("MessageTester", "test@codeit.com", "MessageTester", null);
//        User user = userService.create(userCreateDto);
//
//        ChannelCreatePrivateDto channelCreatePrivateDto = new ChannelCreatePrivateDto(List.of(user));
//        Channel privateChannel = channelService.createPrivate(channelCreatePrivateDto);

    /// /        ChannelCreatePublicDto channelCreatePublicDto = new ChannelCreatePublicDto("testPublic", "testPublic");
    /// /        Channel publicChannel = channelService.createPublic(channelCreatePublicDto);
//        // 생성
//        try {
//            MessageCreateDto messageCreateDto = new MessageCreateDto(user.getId(), privateChannel.getId(), "안녕하세요.",
//                    null);
//            Message message = messageService.create(messageCreateDto);
//            System.out.println("메시지 생성: " + message.getId());
//        } catch (RuntimeException e) {
//            System.out.println("메시지 생성 실패: " + e.getMessage());
//        }
//
//        // 존재하지 않는 유저로 생성
//        try {
//            Message message = messageService.create(
//                    new MessageCreateDto(UUID.randomUUID(), privateChannel.getId(), "안녕하세요.", List.of()));
//            System.out.println("메시지 생성: " + message.getId());
//        } catch (RuntimeException e) {
//            System.out.println("메시지 생성 실패: " + e.getMessage());
//        }
//
//        // 존재하지 않는 채널로 생성
//        try {
//            Message message = messageService.create(
//                    new MessageCreateDto(user.getId(), UUID.randomUUID(), "안녕하세요.", List.of()));
//            System.out.println("메시지 생성: " + message.getId());
//        } catch (RuntimeException e) {
//            System.out.println("메시지 생성 실패: " + e.getMessage());
//        }
//
//        // 조회
//
//        // 채널 ID로 조회
//        List<Message> foundMessagesByChannelId = messageService.findAllByChannelId(privateChannel.getId());
//        System.out.println("채널 내 메시지 조회(다건): " + foundMessagesByChannelId.size());
//
//        // 유저 ID로 조회
//        List<Message> foundMessagesByAuthorId = messageService.findAllByAuthorId(user.getId());
//        System.out.println("유저가 쓴 메시지 조회(다건): " + foundMessagesByAuthorId.size());
//
//        try {
//            Message message = messageService.create(
//                    new MessageCreateDto(user.getId(), privateChannel.getId(), "메시지 조회 테스트", List.of()));
//            Message foundMessage = messageService.findById(message.getId());
//            System.out.println("메시지 조회(단건): " + foundMessage.getId());
//        } catch (RuntimeException e) {
//            System.out.println("메시지 조회(단건) 실패: " + e.getMessage());
//        }
//
//        // 존재하지 않는 ID 조회
//        try {
//            Message foundMessage = messageService.findById(UUID.randomUUID());
//            System.out.println("메시지 조회(단건): " + foundMessage.getId());
//        } catch (RuntimeException e) {
//            System.out.println("메시지 조회(단건) 실패: " + e.getMessage());
//        }
//
//        // 수정
//        try {
//            Message message = messageService.create(
//                    new MessageCreateDto(user.getId(), privateChannel.getId(), "메시지 수정 테스트", List.of()));
//            Message updatedMessage = messageService.update(new MessageUpdateDto(message.getId(), "메시지 수정 성공 테스트입니다."));
//            System.out.println("메시지 수정: " + updatedMessage.getContent());
//        } catch (RuntimeException e) {
//            System.out.println("메시지 수정 실패: " + e.getMessage());
//        }
//
//        // 존재하지 않는 ID로 수정
//        try {
//            Message updatedMessage = messageService.update(
//                    new MessageUpdateDto(UUID.randomUUID(), "메시지 수정 실패 테스트입니다."));
//            System.out.println("메시지 수정: " + updatedMessage.getContent());
//        } catch (RuntimeException e) {
//            System.out.println("메시지 수정 실패: " + e.getMessage());
//        }
//
//        // 삭제
//        Message deletedMessage = messageService.create(
//                new MessageCreateDto(user.getId(), privateChannel.getId(), "메시지 삭제 테스트", List.of()));
//        List<Message> foundMessagesBeforeDelete = messageService.findAllByChannelId(privateChannel.getId());
//        System.out.println("메시지 삭제 전: " + foundMessagesBeforeDelete.size());
//        messageService.delete(deletedMessage.getId());
//        List<Message> foundMessagesAfterDelete = messageService.findAllByChannelId(privateChannel.getId());
//        System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size());
//    }
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

//        // 서비스 초기화
//        UserService userService = context.getBean(BasicUserService.class);
//        ChannelService channelService = context.getBean(BasicChannelService.class);
//        MessageService messageService = context.getBean(BasicMessageService.class);
//
//        // 테스트
//        userCRUDTest(userService);
//        channelCRUDTest(channelService, userService);
//        messageCRUDTest(messageService, userService, channelService);
    }

}
