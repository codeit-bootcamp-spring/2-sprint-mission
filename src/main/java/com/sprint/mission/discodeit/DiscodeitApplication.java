package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.CreateMessageDto;
import com.sprint.mission.discodeit.dto.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.CreateUserDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // 서비스 주입
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);

        try {
            // 1. 프로필 이미지 생성
            byte[] profileImageBytes = new byte[]{0x01, 0x02, 0x03};
            BinaryContent profileImage = binaryContentService.create(new CreateBinaryContentDto("profile.png", profileImageBytes));

            // 2. 유저 생성 (프로필 이미지 UUID로 전달)
            CreateUserDto userDto = new CreateUserDto("woody", "woody@codeit.com", "woody1234", profileImage.getUuid());
            User user = userService.create(userDto);
            System.out.println("유저 생성 완료: " + user.getUuid());

            // 3. PRIVATE 채널 생성 (유저 참여)
            Channel privateChannel = channelService.createPrivate(new CreatePrivateChannelDto(ChannelType.PRIVATE, List.of(user.getUuid())));
            System.out.println("PRIVATE 채널 생성 완료: " + privateChannel.getUuid());

            // 4. 첨부파일 생성
            byte[] fileBytes = new byte[]{0x10, 0x20};
            BinaryContent attachment = binaryContentService.create(new CreateBinaryContentDto("file.txt", fileBytes));

            // 5. 메시지 생성 (첨부파일 포함)
            Message message = messageService.create(new CreateMessageDto(privateChannel.getUuid(), user.getUuid(), "비밀 메시지입니다.", List.of(attachment.getUuid()))
            );
            System.out.println("메시지 생성 완료: " + message.getUuid());

            // 6. 유저 접속 상태 확인
            boolean isOnline = userService.read(user.getUuid()).isOnline();
            System.out.println("유저 온라인 상태: " + (isOnline ? "접속 중" : "오프라인"));

            // 7. 채널 삭제 (ReadStatus, Message도 함께 삭제)
            channelService.delete(privateChannel.getUuid());
            System.out.println("채널 삭제 완료");

            // 8. 유저 삭제 (UserStatus, BinaryContent도 함께 삭제)
            userService.delete(user.getUuid());
            System.out.println("유저 삭제 완료");

        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }


}
