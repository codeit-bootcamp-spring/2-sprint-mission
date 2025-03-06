package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

// JCFMessageService와 동일하며, 추후 BasicMessageService도 동일할 예정입니다. (스프린트 요구 사항으로 남겨두었습니다.)
public class FileMessageService implements MessageService {
    private static volatile FileMessageService instance;

    private UserService userService;
    private ChannelService channelService;
    private MessageRepository messageRepository;

    private FileMessageService(UserService userService, ChannelService channelService, MessageRepository messageRepository) {
        this.userService = userService;
        this.channelService = channelService;
        this.messageRepository = messageRepository;
    }

    public static FileMessageService getInstance(UserService userService, ChannelService channelService, MessageRepository messageRepository) {
        if (instance == null) {
            synchronized (FileMessageService.class) {
                if (instance == null) {
                    instance = new FileMessageService(userService, channelService, messageRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public Message sendMessage(UUID senderId, String content, UUID channelId) {
        channelService.validateChannelId(channelId);
        userService.validateUserId(senderId);
        if (!channelService.isChannelMember(channelId, senderId)) {
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
