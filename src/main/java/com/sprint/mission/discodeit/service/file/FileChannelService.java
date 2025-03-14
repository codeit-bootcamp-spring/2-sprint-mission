package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.Repository.file.FileServerRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFServerRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FileChannelService implements ChannelService{
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    private final ChannelRepository channelRepository;

    public FileChannelService(UserRepository userRepository, ServerRepository serverRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.serverRepository = serverRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public Message write(String creatorId, String channelId, String text) {
        UUID UID = UUID.fromString(channelId);
        UUID CID = UUID.fromString(channelId);
        User user = userRepository.findUserByUserId(UID);
        Message message = new Message(user.getId(), user.getName(), CID, text);
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
