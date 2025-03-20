/*
package com.sprint.mission.discodeit.service.jcf;

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

public class JCFMessageService implements MessageService {
    private final UserRepository jcfUserRepository;
    private final ChannelRepository jcfChannelRepository;
    private final MessageRepository jcfMessageRepository;

    public JCFMessageService(UserRepository jcfUserRepository, ChannelRepository jcfChannelRepository, MessageRepository jcfMessageRepository) {
        this.jcfUserRepository = jcfUserRepository;
        this.jcfChannelRepository = jcfChannelRepository;
        this.jcfMessageRepository = jcfMessageRepository;
    }

    @Override
    public Message createMessage(UUID senderId, String content, UUID channelId) {
        UserService.validateUserId(senderId, this.jcfUserRepository);
        ChannelService.validateChannelId(channelId, this.jcfChannelRepository);
        // 해당 채널에 sender가 participant로 있는지 확인하는 코드 필요?
        if (!jcfChannelRepository.findById(channelId).getParticipants().contains(senderId)) {
            throw new NoSuchElementException("해당 senderId를 가진 사용자가 해당 channelId의 Channel에 참여하지 않았습니다.");
        }
        Message newMessage = new Message(senderId, content, channelId);     //content에 대한 유효성 검증은 Message 생성자에게 맡긴다.
        jcfMessageRepository.add(newMessage);
        return newMessage;
    }

    @Override
    public Message readMessage(UUID messageId) {
        return jcfMessageRepository.findById(messageId);
    }

    @Override
    public Map<UUID, Message> readAllMessages() {
        return jcfMessageRepository.getAll();
    }

    @Override
    public void updateMessageContent(UUID messageId, String newContent) {
        jcfMessageRepository.updateMessageContent(messageId, newContent);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        jcfMessageRepository.deleteById(messageId);
    }
}
*/
