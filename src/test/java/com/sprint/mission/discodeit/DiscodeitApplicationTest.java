package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelResDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelReqDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageReqDto;
import com.sprint.mission.discodeit.dto.message.MessageResDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageReqDto;
import com.sprint.mission.discodeit.dto.user.CreateUserReqDto;
import com.sprint.mission.discodeit.dto.user.UserResDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DiscodeitApplicationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private MessageService messageService;

    @AfterEach
    public void cleanup() {
        try {
            Path fileDir = Paths.get(System.getProperty("user.dir"), ".discodeit");
            if (Files.exists(fileDir)) {
                // .discodeit 폴더 자체를 포함하여 재귀적으로 삭제
                Files.walk(fileDir)
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                throw new RuntimeException("파일 삭제 실패: " + path, e);
                            }
                        });
            }
            // 삭제 후 .discodeit 폴더 및 각 리포지토리 하위 폴더를 재생성
            Files.createDirectories(fileDir);
            Files.createDirectories(fileDir.resolve("User"));
            Files.createDirectories(fileDir.resolve("Channel"));
            Files.createDirectories(fileDir.resolve("Message"));
            Files.createDirectories(fileDir.resolve("ReadStatus"));
            Files.createDirectories(fileDir.resolve("BinaryContent"));
            Files.createDirectories(fileDir.resolve("UserStatus"));
        } catch (IOException e) {
            throw new RuntimeException("초기화 실패: .discodeit 폴더", e);
        }
    }

    @Test
    public void givenUserData_whenCreateUser_thenUserCanBeFoundAndDeleted() {
        // Given: 새로운 사용자 데이터를 준비한다.
        CreateUserReqDto createUserReqDto =
                new CreateUserReqDto("recordUser", "recordUser@example.com", "password", null);

        UserResDto createdUser = userService.create(createUserReqDto);

        UserResDto foundUser = userService.find(createdUser.id());
        assertEquals(createdUser.username(), foundUser.username(), "사용자 이름이 일치해야 합니다.");

        // And When: 사용자를 삭제한다.
        userService.delete(createdUser.id());

        // Then: 삭제 후 조회 시 NoSuchElementException 예외가 발생해야 한다.
        assertThrows(NoSuchElementException.class, () -> userService.find(createdUser.id()));
    }

    @Test
    public void givenChannelData_whenCreateChannel_thenChannelCanBeFoundAndDeleted() {
        // Given: 공개 채널 생성을 위한 데이터를 준비한다.
        CreateChannelReqDto.Public publicChannelDto =
                new CreateChannelReqDto.Public("Record Public Channel", "A public channel using record style");

        // When: 공개 채널을 생성한다.
        ChannelResDto publicChannel = channelService.createPublicChannel(publicChannelDto);

        // Then: 채널은 유효한 ID와 PUBLIC 타입을 가져야 한다.
        assertNotNull(publicChannel.id(), "공개 채널의 ID는 null이 아니어야 합니다.");
        assertEquals(ChannelType.PUBLIC, publicChannel.channelType());

        // And Given: 개인 채널 생성을 위한 사용자를 생성한다.
        UserResDto user = userService.create(new CreateUserReqDto("recordChannelUser", "recordChannelUser@example.com", "password", null));
        // And When: 개인 채널을 생성한다.
        ChannelResDto privateChannel = channelService.createPrivateChannel(
                new CreateChannelReqDto.Private(List.of(user.id())));
        assertNotNull(privateChannel.id(), "개인 채널의 ID는 null이 아니어야 합니다.");
        assertEquals(ChannelType.PRIVATE, privateChannel.channelType());
        assertTrue(privateChannel.participantUserIds().contains(user.id()),
                "참여자 목록에 생성한 사용자가 포함되어야 합니다.");

        // And When: 두 채널을 삭제한다.
        channelService.delete(publicChannel.id());
        channelService.delete(privateChannel.id());

        // Then: 삭제 후 채널 조회 시 NoSuchElementException 예외가 발생해야 한다.
        assertThrows(NoSuchElementException.class, () -> channelService.find(publicChannel.id()));
        assertThrows(NoSuchElementException.class, () -> channelService.find(privateChannel.id()));
    }

    @Test
    public void givenMessageData_whenCreateUpdateAndDeleteMessage_thenOperationsSucceed() {
        // Given: 메시지 작성을 위한 사용자와 채널 데이터를 준비한다.
        UserResDto user = userService.create(
                new CreateUserReqDto("recordMessageUser", "recordMessageUser@example.com", "password", null));
        ChannelResDto channel = channelService.createPublicChannel(
                new CreateChannelReqDto.Public("Record Message Channel", "Channel for record message tests"));

        // And Given: 생성할 메시지 데이터를 준비한다.
        CreateMessageReqDto createMessageReqDto =
                new CreateMessageReqDto(user.id(), channel.id(), "Record Test Message", null);

        // When: 메시지를 생성한다.
        MessageResDto message = messageService.create(createMessageReqDto);

        // Then: 생성된 메시지는 유효한 ID와 올바른 내용을 가져야 한다.
        assertNotNull(message.id(), "생성된 메시지의 ID는 null이 아니어야 합니다.");
        assertEquals("Record Test Message", message.content(), "생성된 메시지의 내용이 예상과 일치해야 합니다.");

        // And When: 개별 메시지 조회를 검증한다.
        MessageResDto foundMessage = messageService.find(message.id());
        assertEquals(message.id(), foundMessage.id(), "조회된 메시지의 ID가 일치해야 합니다.");
        assertEquals(message.content(), foundMessage.content(), "조회된 메시지의 내용이 일치해야 합니다.");

        // And When: 메시지를 업데이트한다.
        UpdateMessageReqDto updateDto = new UpdateMessageReqDto("Updated Record Message", null);
        messageService.update(message.id(), updateDto);

        // Then: 채널 내 메시지 목록에서 업데이트된 내용이 반영되어야 한다.
        List<MessageResDto> messages = messageService.findAllByChannelId(channel.id());
        MessageResDto updatedMessage = messages.stream()
                .filter(m -> m.id().equals(message.id()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("업데이트된 메시지를 찾을 수 없습니다."));
        assertEquals("Updated Record Message", updatedMessage.content(), "메시지 업데이트 후 내용이 반영되어야 합니다.");

        // And When: 메시지를 삭제한다.
        messageService.delete(message.id());

        // Then: 삭제 후 해당 메시지는 채널 메시지 목록에서 존재하지 않아야 한다.
        List<MessageResDto> messagesAfterDeletion = messageService.findAllByChannelId(channel.id());
        boolean exists = messagesAfterDeletion.stream().anyMatch(m -> m.id().equals(message.id()));
        assertFalse(exists, "메시지가 삭제되어 목록에 존재하면 안됩니다.");
    }
}
