package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Map;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final UserRepository fileUserRepository;
    private final ChannelRepository fileChannelRepository;
    private final MessageRepository fileMessageRepository;

    public FileMessageService(UserRepository fileUserRepository, ChannelRepository fileChannelRepository, MessageRepository fileMessageRepository) {
        this.fileUserRepository = fileUserRepository;
        this.fileChannelRepository = fileChannelRepository;
        this.fileMessageRepository = fileMessageRepository;
    }

    @Override
    public void createMessage(UUID senderId, String content, UUID channelId) {
        UserService.validateUserId(senderId, this.fileUserRepository);
        ChannelService.validateChannelId(channelId, this.fileChannelRepository);
        Message newMessage = new Message(senderId, content, channelId);     //content에 대한 유효성 검증은 Message 생성자에게 맡긴다.
        fileMessageRepository.add(newMessage);
    }

    @Override
    public Message readMessage(UUID messageId) {
        return fileMessageRepository.findById(messageId);
    }

    @Override
    public Map<UUID, Message> readAllMessages() {
        return fileMessageRepository.getAll();
    }

    @Override
    public void updateMessageContent(UUID messageId, String content) {
        fileMessageRepository.findById(messageId).updateContent(content);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        fileMessageRepository.deleteById(messageId);
    }
}
