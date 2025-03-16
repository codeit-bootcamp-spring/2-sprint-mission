package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.Exception.EmptyMessageListException;
import com.sprint.mission.discodeit.Repository.MessageRepository;
import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            messageRepository.reset();
        }
    }

    @Override
    public Message write(String creatorId, String channelId, String text) {
        UUID UID = UUID.fromString(creatorId);
        UUID CID = UUID.fromString(channelId);
        System.out.println("🔍 write: 요청된 creatorId: " + creatorId);
        System.out.println("🔍 write: 요청된 channelId: " + channelId);

        User user = userRepository.findUserByUserId(UID);
        System.out.println("🔍 write: 반환된 user: " + user.getId());

        Message message = new Message(UID,user.getName(), CID, text);

        messageRepository.saveMessage(message);
        return message;
    }

    @Override
    public Message getMessage(String serverId, String channelId, String messageId) {
        UUID SID = UUID.fromString(serverId);
        UUID CID = UUID.fromString(channelId);
        UUID MID = UUID.fromString(messageId);

        Channel channel = channelRepository.findChannelByChanelId(SID, CID);
        Message message = messageRepository.findMessageByChannel(channel, MID);

        return message;
    }

    @Override
    public void printMessage(String serverId, String channelId) {
        try {
            UUID SID = UUID.fromString(serverId);
            UUID CID = UUID.fromString(channelId);

            Channel channel = channelRepository.findChannelByChanelId(SID, CID);
            System.out.println(channel.getName());

            List<Message> messages = messageRepository.findMessageListByChannel(channel);
            for (Message message : messages) {
                System.out.println(message.getCreatorName() + " : " + message.getText());
            }
        } catch (EmptyMessageListException e) {
            System.out.println("메시지 함이 비어있습니다.");
        }

    }

    @Override
    public boolean removeMessage(String serverId,String channelId, String messageId) {
        UUID SID = UUID.fromString(serverId);
        UUID CID = UUID.fromString(channelId);
        UUID MID = UUID.fromString(messageId);
        Channel channel = channelRepository.findChannelByChanelId(SID, CID);
        Message message = messageRepository.findMessageByChannel(channel, MID);

        messageRepository.removeMessage(channel, message);
        return true;
    }

    @Override
    public boolean updateMessage(String serverId,String channelId, String messageId, String replaceText) {
        UUID SID = UUID.fromString(serverId);
        UUID CID = UUID.fromString(channelId);
        UUID MID = UUID.fromString(messageId);
        Channel channel = channelRepository.findChannelByChanelId(SID, CID);
        Message message = messageRepository.findMessageByChannel(channel, MID);

        messageRepository.updateMessage(channel, message, replaceText);
        return true;
    }
}
