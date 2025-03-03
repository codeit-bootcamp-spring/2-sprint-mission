package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.MessageEntity;
import com.sprint.mission.discodeit.entity.UserEntity;
import com.sprint.mission.discodeit.entity.ChannelEntity;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService extends JCFBaseService<MessageEntity> implements MessageService {

    private UserService userService;

    public JCFMessageService(UserService userService) {
        this.userService = userService;
    }

    private UserEntity findUserById(UUID userId) {
        return userService.findById(userId).orElse(null);
    }
    @Override
    public MessageEntity createMessage(String content, UUID senderId, ChannelEntity channel ){
        UserEntity sender = findUserById(senderId);

        if (sender == null || channel == null){
            throw new IllegalArgumentException("존재하지 않는 사용자 또는 채널입니다.");
        }

        MessageEntity message = new MessageEntity(content, sender, channel);
        channel.addMessage(message);
        data.add(message);
        return message;
    }

    @Override
    public Optional<MessageEntity> getMessageById(UUID messageId) {
        return findById(messageId);
    }

    @Override
    public List<MessageEntity> getAllMessages() {
        return findAll();
    }

    @Override
    public void updateMessage (UUID messageId, String newContent){
        findById(messageId).ifPresent(message -> {
            message.updateMessage(newContent);
            update(message);
        });
    }

    @Override
    public void delete(MessageEntity message) {
        message.getChannel().getMessages().remove(message);
        data.remove(message); //객체로 메시지 삭제
    }

    @Override
    public void deleteById(UUID messageId){
        findById(messageId).ifPresent(this::delete); //ID로 메시지 삭제
    }
}
