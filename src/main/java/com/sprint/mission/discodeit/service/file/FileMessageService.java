package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Map;
import java.util.NoSuchElementException;
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
    public Message createMessage(UUID senderId, String content, UUID channelId) {
        UserService.validateUserId(senderId, this.fileUserRepository);
        ChannelService.validateChannelId(channelId, this.fileChannelRepository);
        // 해당 채널에 sender가 participant로 있는지 확인하는 코드 필요?
        if (!fileChannelRepository.findById(channelId).getParticipants().contains(senderId)) {
            throw new NoSuchElementException("해당 senderId를 가진 사용자가 해당 channelId의 Channel에 참여하지 않았습니다.");
        }
        Message newMessage = new Message(senderId, content, channelId);     //content에 대한 유효성 검증은 Message 생성자에게 맡긴다.
        fileMessageRepository.add(newMessage);
        return newMessage;
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
    public void updateMessageContent(UUID messageId, String newContent) {
        fileMessageRepository.updateMessageContent(messageId, newContent);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        fileMessageRepository.deleteById(messageId);
    }
}
