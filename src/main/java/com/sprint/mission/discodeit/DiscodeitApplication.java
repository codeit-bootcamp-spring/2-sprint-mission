package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.dto.AuthServiceLoginDto;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.channeldto.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.service.dto.channeldto.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusFindDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.service.dto.userdto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusCreateDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication_Test.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        UserStatusService userStatusService = context.getBean(UserStatusService.class);
        BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
        AuthService authService = context.getBean(AuthService.class);
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);


        Path userProfilePath = Paths.get("C:/Users/TIGER/Pictures/Ubuntu.jpg");

        String userUUID1  = "d1c43164-95d5-43be-81ed-0313ee7f3eb1";
        UUID userUUID2 = UUID.fromString(userUUID1);
        String channelUUID1  = "4956f8ec-3a7c-4392-a1db-253be05daf86";
        UUID channelUUID2 = UUID.fromString(channelUUID1);


        String stringUUIDTest  = "20e04c9d-134a-41ac-9993-111111111111";
        UUID uuidTest2 = UUID.fromString(stringUUIDTest);


        // created user
        UserCreateDto userCreateDto1 = new UserCreateDto("kang", "homea90@naver.com", "12345", userProfilePath);
        UserCreateDto userCreateDto2 = new UserCreateDto("jo", "whrbfl@naver.com", "12345", userProfilePath);
        UserCreateDto userCreateDto3 = new UserCreateDto("dolce", "dolce@naver.com", "12345", userProfilePath);
        UserCreateDto userCreateDto4 = new UserCreateDto("latte", "latte@naver.com", "12345", userProfilePath);
        User user1 = userService.create(userCreateDto1);
        User user2 = userService.create(userCreateDto2);
        User user3 = userService.create(userCreateDto3);
        User user4 = userService.create(userCreateDto4);

        // created binary content
        BinaryContentCreateDto binaryContentCreateDto1 = new BinaryContentCreateDto(userProfilePath);
        BinaryContentCreateDto binaryContentCreateDto2 = new BinaryContentCreateDto(userProfilePath);
        BinaryContentCreateDto binaryContentCreateDto3 = new BinaryContentCreateDto(userProfilePath);
        BinaryContentCreateDto binaryContentCreateDto4 = new BinaryContentCreateDto(userProfilePath);
        BinaryContent binaryContent1 = binaryContentService.create(binaryContentCreateDto1);
        BinaryContent binaryContent2 = binaryContentService.create(binaryContentCreateDto2);
        BinaryContent binaryContent3 = binaryContentService.create(binaryContentCreateDto3);
        BinaryContent binaryContent4 = binaryContentService.create(binaryContentCreateDto4);

        // created user status
        UserStatusCreateDto userStatusCreateDto1 = new UserStatusCreateDto(user1.getId());
        UserStatusCreateDto userStatusCreateDto2 = new UserStatusCreateDto(user2.getId());
        UserStatusCreateDto userStatusCreateDto3 = new UserStatusCreateDto(user3.getId());
        UserStatusCreateDto userStatusCreateDto4 = new UserStatusCreateDto(user4.getId());
        UserStatus userStatus1 = userStatusService.create(userStatusCreateDto1);
        UserStatus userStatus2 = userStatusService.create(userStatusCreateDto2);
        UserStatus userStatus3 = userStatusService.create(userStatusCreateDto3);
        UserStatus userStatus4 = userStatusService.create(userStatusCreateDto4);

        // login
        AuthServiceLoginDto login1 = new AuthServiceLoginDto("kang", "12345");
        AuthServiceLoginDto login2 = new AuthServiceLoginDto("jo", "12345");
        AuthServiceLoginDto login3 = new AuthServiceLoginDto("dolce", "12345");
        AuthServiceLoginDto login4 = new AuthServiceLoginDto("latte", "12345");
        try {
            User userLogin1 = authService.login(login1);
        } catch(NoSuchElementException e){
            System.out.println(e.getMessage());
        }

        // 채널 생성
        ChannelCreatePublicDto channelCreatePublicDto1 = new ChannelCreatePublicDto("정식서버1", "서비스중 입니다.");
        ChannelCreatePublicDto channelCreatePublicDto2 = new ChannelCreatePublicDto("정식서버2", "서비스중 입니다.");
        ChannelCreatePublicDto channelCreatePublicDto3 = new ChannelCreatePublicDto("정식서버3", "서비스중 입니다.");
        ChannelCreatePublicDto channelCreatePublicDto4 = new ChannelCreatePublicDto("정식서버4", "서비스중 입니다.");
        ChannelCreatePrivateDto channelCreatePrivateDto1 = new ChannelCreatePrivateDto(user1.getId());
        ChannelCreatePrivateDto channelCreatePrivateDto2 = new ChannelCreatePrivateDto(user2.getId());
        ChannelCreatePrivateDto channelCreatePrivateDto3 = new ChannelCreatePrivateDto(user3.getId());
        ChannelCreatePrivateDto channelCreatePrivateDto4 = new ChannelCreatePrivateDto(user4.getId());
        Channel channel1 = channelService.createPublic(channelCreatePublicDto1);
        Channel channel2 = channelService.createPublic(channelCreatePublicDto2);
        Channel channel3 = channelService.createPublic(channelCreatePublicDto3);
        Channel channel4 = channelService.createPublic(channelCreatePublicDto4);
        Channel channel5 = channelService.createPrivate(channelCreatePrivateDto1);
        Channel channel6 = channelService.createPrivate(channelCreatePrivateDto1);
        Channel channel7 = channelService.createPrivate(channelCreatePrivateDto1);
        Channel channel8 = channelService.createPrivate(channelCreatePrivateDto1);


        // read status 생성
        ReadStatusCreateDto readStatusCreateDto1 = new ReadStatusCreateDto(user1.getId(), channel1.getId(), null);
        ReadStatusCreateDto readStatusCreateDto2 = new ReadStatusCreateDto(user1.getId(), channel2.getId(), null);
        ReadStatusCreateDto readStatusCreateDto3 = new ReadStatusCreateDto(user1.getId(), channel3.getId(), null);
        ReadStatusCreateDto readStatusCreateDto4 = new ReadStatusCreateDto(user1.getId(), channel4.getId(), null);
        ReadStatusCreateDto readStatusCreateDto5 = new ReadStatusCreateDto(user1.getId(), channel5.getId(), null);
        ReadStatusCreateDto readStatusCreateDto6 = new ReadStatusCreateDto(user1.getId(), channel6.getId(), null);
        ReadStatusCreateDto readStatusCreateDto7 = new ReadStatusCreateDto(user1.getId(), channel7.getId(), null);
        ReadStatusCreateDto readStatusCreateDto8 = new ReadStatusCreateDto(user1.getId(), channel8.getId(), null);
        ReadStatus readStatus1 = readStatusService.create(readStatusCreateDto1);
        ReadStatus readStatus2 = readStatusService.create(readStatusCreateDto2);
        ReadStatus readStatus3 = readStatusService.create(readStatusCreateDto3);
        ReadStatus readStatus4 = readStatusService.create(readStatusCreateDto4);
        ReadStatus readStatus5 = readStatusService.create(readStatusCreateDto5);
        ReadStatus readStatus6 = readStatusService.create(readStatusCreateDto6);
        ReadStatus readStatus7 = readStatusService.create(readStatusCreateDto7);
        ReadStatus readStatus8 = readStatusService.create(readStatusCreateDto8);



        // 조회
        // read status 조회
        ReadStatusFindDto readStatusFindDto1 = new ReadStatusFindDto(readStatus1.getId(), readStatus1.getUserId());
        ReadStatusFindDto readStatusFindDto2 = new ReadStatusFindDto(readStatus2.getId(), readStatus2.getUserId());
        ReadStatusFindDto readStatusFindDto3 = new ReadStatusFindDto(readStatus3.getId(), readStatus3.getUserId());
        ReadStatusFindDto readStatusFindDto4 = new ReadStatusFindDto(readStatus4.getId(), readStatus4.getUserId());
        ReadStatusFindDto readStatusFindDto5 = new ReadStatusFindDto(readStatus5.getId(), readStatus5.getUserId());
        ReadStatusFindDto readStatusFindDto6 = new ReadStatusFindDto(readStatus6.getId(), readStatus6.getUserId());
        ReadStatusFindDto readStatusFindDto7 = new ReadStatusFindDto(readStatus7.getId(), readStatus7.getUserId());
        ReadStatusFindDto readStatusFindDto8 = new ReadStatusFindDto(readStatus8.getId(), readStatus8.getUserId());
        ReadStatus print1 = readStatusService.find(readStatusFindDto1);
        List<ReadStatus> print2 = readStatusService.findAllByUserId(readStatusFindDto2);
        System.out.println("find: " + print1);
        System.out.println("findAllByUserId: " + print2);

        // read status 수정
//        ReadStatusUpdateDto readStatusUpdateDto1 = new ReadStatusUpdateDto(user1.getId(), channel1.getId());
//        ReadStatusUpdateDto readStatusUpdateDto2 = new ReadStatusUpdateDto(user2.getId(), channel2.getId());
//        ReadStatus update1 = readStatusService.update(readStatusUpdateDto1);
//        ReadStatus update2 = readStatusService.update(readStatusUpdateDto2);
//        System.out.println("1번 update: " + update1);
//        System.out.println("2번 update: " + update2);


//        UserFindDto userFindDto = new UserFindDto(user.getId(), null,null,null,false);
//        User userPrint = userService.getUser(userFindDto);
//        System.out.println(userPrint);
//        UserStatus userStatusPrint = userStatusService.getUser(userFindDto);
//        System.out.println(userStatusPrint);
//        UserDeleteDto userDeleteDto = new UserDeleteDto(user.getId(), null, null, null);
//        userService.delete(userDeleteDto);
//        System.out.println("User deleted");


    }
}
