package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFServerRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class JCFChannelService implements ChannelService {
    private final ServerRepository serverRepository;
    private final ChannelRepository channelRepository;

    public JCFChannelService(ServerRepository serverRepository, ChannelRepository channelRepository) {
        this.serverRepository = serverRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public Message write(String creatorId, String channelId, String text) {
        UUID UID = UUID.fromString(channelId);
        UUID CID = UUID.fromString(channelId);
        Message message = new Message(UID, CID, text);
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
        UUID SID = UUID.fromString(serverId);
        UUID CID = UUID.fromString(channelId);
        Channel channel = serverRepository.findChannelByChanelId(SID, CID);

        List<Message> messages = channelRepository.findMessageListByChannel(channel);

        System.out.println(channel.getName());
        for (Message message : messages) {
            System.out.println(message.getCreatorId() + " : " + message.getText());
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
