package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;

    public BasicChannelService(ChannelRepository channelRepository, UserService userService) {
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    private void saveChannelData() {
        channelRepository.save();
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        channelRepository.addChannel(channel);
        return channel;
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        validateChannelExists(channelId);
        return channelRepository.findChannelById(channelId);
    }

    @Override
    public String findChannelNameById(UUID channelId) {
        validateChannelExists(channelId);
        return channelRepository.findChannelById(channelId).getChannelName();
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAllChannels();
    }


    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        validateChannelExists(channelId);
        Channel channel = channelRepository.findChannelById(channelId);
        channel.updateChannelName(newChannelName);
        saveChannelData();
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        userService.validateUserExists(userId);

        Channel channel = channelRepository.findChannelById(channelId);
        userService.addChannel(userId, channelId);

        channel.addMembers(userId);
        saveChannelData();
    }

    @Override
    public void addMessage(UUID channelId, UUID messageId) {
        Channel channel = channelRepository.findChannelById(channelId);
        channel.addMessages(messageId);
        saveChannelData();
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = channelRepository.findChannelById(channelId);

        Set<UUID> userIds = channel.getMembers();
        userIds.forEach(userId -> userService.removeChannel(userId, channelId));

        channelRepository.deleteChannelById(channelId);
    }

    @Override
    public void removeUser(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findChannelById(channelId);
        channel.removeMember(userId);
        saveChannelData();
    }

    @Override
    public void removeMessage(UUID channelId, UUID messageId) {
        Channel channel = channelRepository.findChannelById(channelId);
        channel.removeMessage(messageId);
        saveChannelData();
    }

    @Override
    public void validateChannelExists(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }
}
