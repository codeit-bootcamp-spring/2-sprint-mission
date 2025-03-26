package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelResDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelReqDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageReqDto;
import com.sprint.mission.discodeit.dto.message.MessageResDto;
import com.sprint.mission.discodeit.dto.user.CreateUserReqDto;
import com.sprint.mission.discodeit.dto.user.UserResDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
@Slf4j
public class DiscodeitApplication {

    static UserResDto setupUser(UserService userService) {
        return userService.create(new CreateUserReqDto("woody34", "woody34@codeit.com", "woody1234", null));
    }

    static ChannelResDto setupChannel(ChannelService channelService) {
        return channelService.createPublicChannel(new CreateChannelReqDto.Public("공지", "공지 채널입니다."));
    }

    static ChannelResDto setupPrivateChannel(ChannelService channelService, List<UUID> joinUsers) {
        return channelService.createPrivateChannel(new CreateChannelReqDto.Private(joinUsers));
    }

    static void messageCreateTest(MessageService messageService, UUID channelId, UUID authorId) {
        MessageResDto messageResDto = messageService.create(new CreateMessageReqDto(authorId, channelId, "안녕하세요.", null));
        log.info("메시지 생성: {}", messageResDto.id());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // 서비스 초기화
        // TODO context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 셋업
        UserResDto user = setupUser(userService);
        ChannelResDto channel = setupChannel(channelService);
        ChannelResDto privateChannel = setupPrivateChannel(channelService, List.of(user.id()));
        // 테스트
        messageCreateTest(messageService, channel.id(), user.id());
    }

}
