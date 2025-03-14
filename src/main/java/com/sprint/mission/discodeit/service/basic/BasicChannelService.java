package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.Exception.EmptyMessageListException;
import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BasicChannelService implements ChannelService {
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    private final ChannelRepository channelRepository;

    public BasicChannelService(UserRepository userRepository, ServerRepository serverRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.serverRepository = serverRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            channelRepository.reset();
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

        channelRepository.saveMessage(message);
        return message;
    }

    @Override
    public Message getMessage(String serverId, String channelId, String messageId) {
        UUID SID = UUID.fromString(serverId);
        UUID CID = UUID.fromString(channelId);
        UUID MID = UUID.fromString(messageId);

        Channel channel = serverRepository.findChannelByChanelId(SID, CID);
        Message message = channelRepository.findMessageByChannel(channel, MID);

        return message;
    }

    @Override
    public void printMessage(String serverId, String channelId) {
        try {
            UUID SID = UUID.fromString(serverId);
            UUID CID = UUID.fromString(channelId);

            Channel channel = serverRepository.findChannelByChanelId(SID, CID);
            System.out.println(channel.getName());

            List<Message> messages = channelRepository.findMessageListByChannel(channel);
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
        Channel channel = serverRepository.findChannelByChanelId(SID, CID);
        Message message = channelRepository.findMessageByChannel(channel, MID);

        channelRepository.removeMessage(channel, message);
        return true;
    }

    @Override
    public boolean updateMessage(String serverId,String channelId, String messageId, String replaceText) {
        UUID SID = UUID.fromString(serverId);
        UUID CID = UUID.fromString(channelId);
        UUID MID = UUID.fromString(messageId);
        Channel channel = serverRepository.findChannelByChanelId(SID, CID);
        Message message = channelRepository.findMessageByChannel(channel, MID);

        channelRepository.updateMessage(channel, message, replaceText);
        return true;
    }
}
