package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BasicChannelService implements ChannelService {

    private static BasicChannelService INSTANCE;
    private final ChannelRepository channelRepository;
    private final UserService userService;

    public BasicChannelService(ChannelRepository channelRepository, UserService userService) {
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    public static synchronized BasicChannelService getInstance(ChannelRepository channelRepository, UserService userService) {
        if (INSTANCE == null) {
            INSTANCE = new BasicChannelService(channelRepository, userService);
        }
        return INSTANCE;
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
        channelRepository.addChannel(channel);
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        userService.validateUserExists(userId);

        Channel channel = channelRepository.findChannelById(channelId);
        userService.addChannel(userId, channelId);

        channelRepository.addChannel(channel);
    }

    @Override
    public void addMessage(UUID channelId, UUID messageId) {
        Channel channel = channelRepository.findChannelById(channelId);
        channel.addMessages(messageId);
        channelRepository.addChannel(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = channelRepository.findChannelById(channelId);

        Set<UUID> userIds = channel.getMembers();
        List<User> users = userService.findUsersByIds(userIds);

        users.forEach(user -> user.removeJoinedChannel(channelId));

        channelRepository.deleteChannelById(channelId);
    }

    @Override
    public void removeUser(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findChannelById(channelId);
        channel.removeMember(userId);
        channelRepository.addChannel(channel);
    }

    @Override
    public void removeMessage(UUID channelId, UUID messageId) {
        Channel channel = channelRepository.findChannelById(channelId);
        channel.removeMessage(messageId);
        channelRepository.addChannel(channel);
    }

    @Override
    public void validateChannelExists(UUID channelId) {
        if(!channelRepository.existsById(channelId)){
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }
}
