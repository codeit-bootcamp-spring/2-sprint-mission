package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.auth.AuthLoginResponseDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
        AuthService authService = context.getBean(AuthService.class);
        UserStatusService userStatusService = context.getBean(UserStatusService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);

        List<BinaryContent> profiles = setUpProfiles(binaryContentService);
        List<UserCreateResponseDto> userDtos = setupUsers(userService, profiles);
        printUser1(userService, userDtos.get(0));
        printUsers(userService, userDtos);
        deleteUser(userService, userDtos, binaryContentService, profiles);
        printUsers(userService, userDtos);
        updateUser1(userService, userDtos.get(0));
        printUser1(userService, userDtos.get(0));

        System.out.println();

        testSuccessfulLogin(authService, userStatusService, userDtos.get(0));
        testFailedLogin(authService);

        Channel privateChannel = createPrivateChannel(channelService, userDtos.get(0).id(), userDtos.get(1).id());
        List<Channel> publicChannels = createPublicChannels(channelService);

        System.out.println("===================== pulic 채널 1 find로 출력 =====================");
        printChannel(channelService, publicChannels.get(0).getId());
        System.out.println("===================== user1이 있는 모든 채널 출력: 3개=====================");
        printChannelsByUser(channelService, userDtos.get(0).id());


        List<BinaryContent> messageFiles = setUpMessageFiles(binaryContentService);
        List<Message> messages = setUpMessages(messageService, privateChannel, publicChannels, userDtos, messageFiles);

        System.out.println("===================== PrivateMessage1 find로 출력 =====================");
        printMessage(messageService, messages.get(0).getId());
        System.out.println("===================== PrivateChannel 메세지들 출력: 2개 =====================");
        printMessagesByChannel(messageService, privateChannel.getId());
        System.out.println("===================== PublicChannel2 메세지들 출력: 2개 =====================");
        printMessagesByChannel(messageService, publicChannels.get(1).getId());
        testMessageUpdate(messageService, messages.get(0).getId());
        System.out.println("===================== PrivateMessage1 find로 출력 =====================");
        printMessage(messageService, messages.get(0).getId());



        System.out.println("===================== PublicChannel2 삭제 =====================");
        channelService.delete(publicChannels.get(0).getId());
        System.out.println("===================== user1이 있는 모든 채널 출력: 2개여야함=====================");
        printChannelsByUser(channelService, userDtos.get(0).id());
    }

    private static void testMessageUpdate(MessageService messageService, UUID messageId) {
        System.out.println("===================== PrivateMessage1 내용 수정 테스트 =====================");
        Message updatedMessage = messageService.update(new MessageUpdateRequestDto(messageId, "프라이빗1 - 내용 교체"));
    }

    private static void printMessagesByChannel(MessageService messageService, UUID channelId) {
        messageService.findAllByChannelId(channelId).forEach(System.out::println);
    }

    private static void printMessage(MessageService messageService, UUID messageId) {
        System.out.println(messageService.find(messageId));
    }

    private static List<Message> setUpMessages(MessageService messageService, Channel privateChannel, List<Channel> publicChannels, List<UserCreateResponseDto> userDtos, List<BinaryContent> messageFiles) {
        Message privateChannelMessage1 = messageService.create(new MessageCreateRequestDto("프라이빗메세지1", privateChannel.getId(), userDtos.get(0).id(), List.of()));
        Message privateChannelMessage2 = messageService.create(new MessageCreateRequestDto("프라이빗메세지2", privateChannel.getId(), userDtos.get(1).id(), List.of()));

        Message publicChannel1Message1 = messageService.create(new MessageCreateRequestDto("퍼블릭1메세지1", publicChannels.get(0).getId(), userDtos.get(0).id(), List.of()));

        Message publicChannel2Message1 = messageService.create(new MessageCreateRequestDto("퍼블릭2메세지1", publicChannels.get(1).getId(), userDtos.get(0).id(), List.of(messageFiles.get(0).getId(), messageFiles.get(1).getId())));
        Message publicChannel2Message2 = messageService.create(new MessageCreateRequestDto("퍼블릭2메세지2", publicChannels.get(1).getId(), userDtos.get(0).id(), List.of(messageFiles.get(2).getId())));

        return List.of(privateChannelMessage1, privateChannelMessage2, publicChannel1Message1, publicChannel2Message1, publicChannel2Message2);
    }

    private static List<BinaryContent> setUpMessageFiles(BinaryContentService binaryContentService) {
        BinaryContent profile1 = binaryContentService.create(new BinaryContentCreateRequestDto("PublicChannel2-메세지1-사진1"));
        BinaryContent profile2 = binaryContentService.create(new BinaryContentCreateRequestDto("PublicChannel2-메세지1-사진2"));
        BinaryContent profile3 = binaryContentService.create(new BinaryContentCreateRequestDto("PublicChannel2-메세지2-사진1"));
        return List.of(profile1, profile2, profile3);
    }

    private static void printChannel(ChannelService channelService, UUID publicChannelId) {
        System.out.println(channelService.find(publicChannelId));
    }

    private static void printChannelsByUser(ChannelService channelService, UUID userId) {
        channelService.findAllByUserId(userId).forEach(System.out::println);
    }

    private static Channel createPrivateChannel(ChannelService channelService, UUID user1Id, UUID user2Id) {
        Channel privateChannel = channelService.createPrivateChannel(new PrivateChannelCreateRequestDto(List.of(user1Id, user2Id)));
        System.out.println("===================== Private Channel 생성- 멤버:user1, user2 =====================");
        System.out.println(privateChannel);
        return privateChannel;
    }

    private static List<Channel> createPublicChannels(ChannelService channelService) {
        Channel publicChannel1 = channelService.createPublicChannel(new PublicChannelCreateRequestDto("채널1", "채널1임"));
        Channel publicChannel2 = channelService.createPublicChannel(new PublicChannelCreateRequestDto("채널2", "삭제할거임"));

        System.out.println("===================== Public Channel 2개 생성 =====================");
        System.out.println(publicChannel1);
        System.out.println(publicChannel2);

        return List.of(publicChannel1, publicChannel2);
    }

    private static void testSuccessfulLogin(AuthService authService, UserStatusService userStatusService, UserCreateResponseDto user1Dto) {
        System.out.println("===================== 유저 1 로그인 - 성공 케이스 =====================");
        System.out.println("첫 로그인 이전 유저 1의 lastLoginAt: " + userStatusService.findByUserId(user1Dto.id()).getLastLoginAt());
        System.out.println("===================== 유저 1 로그인 시도 =====================");
        AuthLoginResponseDto authResponse = authService.login(new AuthLoginRequestDto("user1", "password1"));
        System.out.println("로그인 성공: " + authResponse);
        System.out.println("로그인 이후 유저 1의 lastLoginAt: " + userStatusService.findByUserId(authResponse.id()).getLastLoginAt());
    }

    private static void testFailedLogin(AuthService authService) {
        System.out.println("===================== 유저 1 로그인 - 실패 케이스 =====================");
        try {
            authService.login(new AuthLoginRequestDto("user1", "wrongpassword"));
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateUser1(UserService userService, UserCreateResponseDto user1Dto) {
        userService.update(new UserUpdateRequestDto(user1Dto.id(), "user1", user1Dto.email(), "password1", user1Dto.profileId()));
        System.out.println("===================== 유저 1 닉넴 영어로 바꿈 =====================");

    }

    private static void printUser1(UserService userService, UserCreateResponseDto user1Dto) {
        System.out.println("===================== 유저 1 find로 출력 =====================");
        System.out.println(userService.find(user1Dto.id()));
    }

    private static List<BinaryContent> setUpProfiles(BinaryContentService binaryContentService) {
        BinaryContent profile1 = binaryContentService.create(new BinaryContentCreateRequestDto("유저1 사진"));
        BinaryContent profile2 = binaryContentService.create(new BinaryContentCreateRequestDto("유저2 사진"));
        BinaryContent profile3 = binaryContentService.create(new BinaryContentCreateRequestDto("유저3 사진"));
        return List.of(profile1, profile2, profile3);
    }

    private static List<UserCreateResponseDto> setupUsers(UserService userService, List<BinaryContent> profiles) {
        UserCreateResponseDto user1Dto = userService.create(new UserCreateRequestDto("유저1", "user1@example.com", "password1", profiles.get(0).getId()));
        UserCreateResponseDto user2Dto = userService.create(new UserCreateRequestDto("유저2", "user2@example.com", "password2", profiles.get(1).getId()));
        UserCreateResponseDto user3Dto = userService.create(new UserCreateRequestDto("유저3", "user3@example.com", "password3", profiles.get(2).getId()));
        UserCreateResponseDto user4Dto = userService.create(new UserCreateRequestDto("유저4", "user4@example.com", "password4", null));

        System.out.println("===================== 유저 4명 생성 =====================");
        return List.of(user1Dto, user2Dto, user3Dto, user4Dto);
    }

    private static void printUsers(UserService userService, List<UserCreateResponseDto> userDtos) {
        System.out.println("===================== 유저 findAll로 출력 =====================");
        userService.findAll().forEach(System.out::println);
    }

    private static void deleteUser(UserService userService, List<UserCreateResponseDto> userDtos, BinaryContentService binaryContentService, List<BinaryContent> profiles) {
        userService.delete(userDtos.get(3).id());
        System.out.println("===================== 유저4 삭제 =====================");
        userService.delete(userDtos.get(2).id());
        System.out.println("===================== 유저3 삭제 =====================");
        System.out.println("===================== 유저3 사진 없는지 확인 =====================");
        try {
            binaryContentService.find(profiles.get(2).getId());
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("===================== 유저 2명 남음 =====================");

    }
}
