package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

// JCFMessageService, FileMessageService, BasicMessageService 전부 동일합니다. 최종적으로는 BasicMessageService 사용합니다 (스프린트 요구 사항으로 남겨두었습니다.)
public class BasicMessageService implements MessageService {
    private static volatile BasicMessageService instance;

    private UserService userService;
    private ChannelService channelService;
    private MessageRepository messageRepository;

    private BasicMessageService(UserService userService, ChannelService channelService, MessageRepository messageRepository) {
        this.userService = userService;
        this.channelService = channelService;
        this.messageRepository = messageRepository;
    }

    public static BasicMessageService getInstance(UserService userService, ChannelService channelService, MessageRepository messageRepository) {
        if (instance == null) {
            synchronized (BasicMessageService.class) {
                if (instance == null) {
                    instance = new BasicMessageService(userService, channelService, messageRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public Message sendMessage(UUID senderId, String content, UUID channelId) {
        channelService.validateChannelId(channelId);
        userService.validateUserId(senderId);
        if (channelService.isChannelMember(channelId, senderId) == false) {
            return null;
        }
        Message message = new Message(senderId, content, channelId);
        messageRepository.save(message);
        return message;
    }


    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }


    @Override
    public Message getMessageById(UUID messageId) {
        validateMessageId(messageId);
        return messageRepository.findById(messageId);
    }

    @Override
    public Message updateMessage(UUID messageId, String content) {
        validateMessageId(messageId);
        Message message = getMessageById(messageId);
        message.update(content);

        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        validateMessageId(messageId);
        Message message = messageRepository.findById(messageId);
        messageRepository.delete(messageId);
    }

    private void validateMessageId(UUID messageId) {
        if (!messageRepository.exists(messageId)) {
            throw new MessageNotFoundException("해당 메세지 없음");
        }
    }
}
