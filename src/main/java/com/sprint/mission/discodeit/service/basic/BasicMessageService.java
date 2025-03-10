package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    MessageRepository messageRepository;
    UserService userService;
    ChannelService channelService;

    public BasicMessageService(MessageRepository messageRepository, UserService userService, ChannelService channelService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void sendMessage(UUID channelUUID, UUID userUUID, String content) {
        if (userService.findByUser(userUUID) ==  null) {
            return;
        }

        if (channelService.findChannel(channelUUID) == null) {
            return;
        }

        Message message = messageRepository.messageSave(channelUUID, userUUID, content);
        if (message == null) {
            System.out.println("[실패] 메세지 저장 실패");
            return;
        }
        System.out.println("[성공]" + message);
    }

    @Override
    public Message findMessageById(UUID messageUUID) {
        return messageRepository.findMessageById(messageUUID)
                .orElseGet(() -> {
                    System.out.println("메세지가 존재하지 않습니다.");
                    return null;
                });
    }

    @Override
    public Optional<List<Message>> findAllMessages() {
        List<Message> messageList = messageRepository.findAllMessage();
        if (messageList.isEmpty()) {
            System.out.println("메세지가 존재하지 않습니다.");
            return Optional.empty();
        }
        return Optional.of(messageList);
    }

    @Override
    public Optional<List<Message>> findMessageByChannelId(UUID channelUUID) {
        List<Message> channelMessageList = messageRepository.findMessageByChannel(channelUUID);
        if (channelMessageList.isEmpty()) {
            System.out.println("해당 채널에 메세지가 존재하지 않습니다");
            return Optional.empty();
        }
        return Optional.of(channelMessageList);
    }

    @Override
    public void updateMessage(UUID messageUUID, String content) {
        Message message = messageRepository.updateMessage(messageUUID, content);
        if (message == null) {
            System.out.println("[실패] 메세지 수정 실패");
        }
    }

    @Override
    public void deleteMessageById(UUID messageUUID) {
        boolean isDelete = messageRepository.deleteMessageById(messageUUID);
        if (!isDelete) {
            System.out.println("[실패] 메세지 삭제 실패");
            return;
        }
        System.out.println("[성공] 메세지 삭제 완료");
    }
}
