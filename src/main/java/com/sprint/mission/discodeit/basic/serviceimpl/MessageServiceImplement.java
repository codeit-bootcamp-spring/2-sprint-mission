package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.util.ValidationUtil;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.service.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserRepository;

import java.util.List;
import java.util.UUID;

public class MessageServiceImplement implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    private static MessageServiceImplement instance;

    private MessageServiceImplement(
            MessageRepository messageRepository,
            UserRepository userRepository,
            ChannelRepository channelRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    public static synchronized MessageServiceImplement getInstance(
            MessageRepository messageRepository,
            UserRepository userRepository,
            ChannelRepository channelRepository) {
        if (instance == null) {
            instance = new MessageServiceImplement(messageRepository, userRepository, channelRepository);
        }
        return instance;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        ValidationUtil.validateNotEmpty(content, "메시지 내용");
        ValidationUtil.validateNotNull(channelId, "채널 ID");
        ValidationUtil.validateNotNull(authorId, "사용자 ID");
        
        // 사용자가 채널에 속해 있는지 확인
        validateUserInChannel(channelId, authorId);

        // 메시지 생성 및 저장
        Message message = new Message(channelId, authorId, content);
        messageRepository.register(message);
        return message;
    }
    
    // 사용자가 채널에 속해 있는지 확인하는 메서드
    private void validateUserInChannel(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findChannelById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다: " + channelId));
                
        User user = userRepository.findByUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        boolean channelInUser = channel.getUserList().contains(userId);
        boolean userBelongsToChannel = user.getBelongChannels().contains(channelId);

        if (!(channelInUser && userBelongsToChannel)) {
            throw new IllegalArgumentException("해당 유저는 이 채널에 존재하지 않습니다.");
        }
    }

    @Override
    public Message findByMessage(UUID messageId) {
        ValidationUtil.validateNotNull(messageId, "메시지 ID");
        
        return messageRepository.findById(messageId).orElse(null);
    }

    @Override
    public List<Message> findAllMessage() {
        return messageRepository.findAll();
    }

    @Override
    public Message updateMessage(UUID messageId, String newContent) {
        ValidationUtil.validateNotNull(messageId, "메시지 ID");
        ValidationUtil.validateNotEmpty(newContent, "새 메시지 내용");
        
        Message message = findByMessage(messageId);
        if (message == null) {
            return null;
        }
        
        message.updateMessage(newContent);
        
        // 변경된 메시지 정보를 저장
        messageRepository.updateMessage(message);
        
        return message;
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        ValidationUtil.validateNotNull(messageId, "메시지 ID");
        
        return messageRepository.deleteMessage(messageId);
    }
}