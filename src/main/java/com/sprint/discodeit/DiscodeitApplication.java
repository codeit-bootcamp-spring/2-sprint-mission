package com.sprint.discodeit;

import com.sprint.discodeit.domain.dto.channelDto.ChannelCreateRequestDto;
import com.sprint.discodeit.domain.dto.channelDto.ChannelFindResponseDto;
import com.sprint.discodeit.domain.dto.channelDto.ChannelResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserNameStatusResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserProfileImgResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserRequestDto;
import com.sprint.discodeit.domain.ChannelType;
import com.sprint.discodeit.service.basic.chnnel.ChannelQueryService;
import com.sprint.discodeit.service.basic.chnnel.BasicChannelService;
import com.sprint.discodeit.service.basic.message.BasicMessageService;
import com.sprint.discodeit.service.basic.users.BasicUserService;
import com.sprint.discodeit.service.basic.util.UserStatusEvaluator;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // 서비스 Bean 가져오기
        BasicUserService userService = context.getBean(BasicUserService.class);
        BasicChannelService channelService = context.getBean(BasicChannelService.class);
        BasicMessageService messageService = context.getBean(BasicMessageService.class);
        UserStatusEvaluator userStatusEvaluator = context.getBean(UserStatusEvaluator.class);
        ChannelQueryService channelQueryService = context.getBean(ChannelQueryService.class);

        // **User 여러 명 생성**
        List<UserNameStatusResponseDto> users = setupUsers(userService, 3);
        System.out.println("\n[테스트] 생성된 사용자 목록: " + users);

        // User ID 리스트 추출
        List<UUID> userIds = users.stream().map(UserNameStatusResponseDto::id).toList();

        // **PRIVATE 채널 생성 (User1, User2만 추가)**
        ChannelResponseDto privateChannel = setupChannel(channelService, List.of(userIds.get(0), userIds.get(1)), ChannelType.PRIVATE);
        System.out.println(" PRIVATE 채널 생성 완료: " + privateChannel);

        // **PUBLIC 채널 생성 (모든 User 포함)**
        ChannelResponseDto publicChannel = setupChannel(channelService, userIds, ChannelType.PUBLIC);
        System.out.println(" PUBLIC 채널 생성 완료: " + publicChannel);

        // **테스트 1: PRIVATE 채널 단일 조회**
        System.out.println("\n[테스트 1] PRIVATE 채널 단일 조회");
        ChannelFindResponseDto foundPrivateChannel = channelQueryService.find(privateChannel.channelId());
        System.out.println(" Found PRIVATE Channel: " + foundPrivateChannel);

        // **테스트 2: PUBLIC 채널 단일 조회**
        System.out.println("\n[테스트 2] PUBLIC 채널 단일 조회");
        ChannelFindResponseDto foundPublicChannel = channelQueryService.find(publicChannel.channelId());
        System.out.println(" Found PUBLIC Channel: " + foundPublicChannel);

//        // **테스트 3: 사용자별 접근 가능한 채널 조회**
//        for (UserNameStatusResponseDto user : users) {
//            System.out.println("\n[테스트 3] 사용자 " + user.name() + "의 접근 가능한 채널 목록 조회");
//            List<ChannelFindResponseDto> allChannels = channelQueryService.findAllByUserId();
//            System.out.println(" " + user.name() + "이(가) 접근 가능한 채널 목록: " + allChannels);
//        }

//        // **메시지 생성 테스트**
//        messageCreateTest(messageService, privateChannel, users.get(0));
//        messageCreateTest(messageService, publicChannel, users.get(1));
    }
//    private static List<UserNameStatusResponseDto> setupUsers(BasicUserService userService, int count) {
//        List<UserNameStatusResponseDto> users = new ArrayList<>();
//
//        for (int i = 1; i <= count; i++) {
//            UserRequestDto userRequestDto = new UserRequestDto("user" + i, "user" + i + "@example.com", "password123");
//            UserProfileImgResponseDto profileImgDto = new UserProfileImgResponseDto("https://your-storage.com/default-profile" + i + ".png");
//
//            UserNameStatusResponseDto response = userService.create(userRequestDto, profileImgDto);
//            System.out.println(" Created User: " + response);
//            users.add(response);
//        }
//
//        return users;


    private static List<UserNameStatusResponseDto> setupUsers(BasicUserService userService, int count) {
        UserRequestDto userRequestDto = null;
        UserProfileImgResponseDto userProfileImgResponseDto = null;
        List<UserNameStatusResponseDto> userNameStatusResponseDto = Collections.singletonList(
                userService.create(userRequestDto,
                        userProfileImgResponseDto));
        System.out.println(userNameStatusResponseDto + "null user create 테스트");
        return userNameStatusResponseDto;
    }

    private static ChannelResponseDto setupChannel(BasicChannelService channelService, List<UUID> userIds, ChannelType channelType) {
        ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto(
                channelType, channelType == ChannelType.PRIVATE ? "PRIVATE 채널" : "PUBLIC 채널", "설명", userIds);
        return channelService.create(channelCreateRequestDto);
    }


}
