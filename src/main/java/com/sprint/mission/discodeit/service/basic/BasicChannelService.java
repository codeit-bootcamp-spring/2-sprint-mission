package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BasicChannelService implements ChannelService {

    private static ChannelRepository channelRepository;
    private static UserRepository userRepository;
    private static BasicChannelService INSTANCE;

    public BasicChannelService(ChannelRepository channelRepository, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    public static synchronized BasicChannelService getInstance(ChannelRepository channelRepository, UserRepository userRepository) {
        if (INSTANCE == null) {
            INSTANCE = new BasicChannelService(channelRepository, userRepository);
        }
        return INSTANCE;
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        return channelRepository.findChannelById(channelId);
    }

    @Override
    public String findChannelNameById(UUID channelId) {
        return channelRepository.findChannelById(channelId).getChannelName();
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAllChannels();
    }

    @Override
    public void updateChannelData() {

    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        Channel channel = channelRepository.findChannelById(channelId);
        channel.updateChannelName(newChannelName);
        channelRepository.save(channel);
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findChannelById(channelId);
        User user = userRepository.findUserById(userId);

        channel.addMembers(userId);
        user.addJoinedChannel(channelId);

        userRepository.save(user);
        channelRepository.save(channel);
    }

    @Override
    public void addMessage(UUID channelId, UUID messageId) {
        Channel channel = channelRepository.findChannelById(channelId);
        channel.addMessages(messageId);
        channelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = channelRepository.findChannelById(channelId);
        Set<UUID> userIds = channel.getMembers();
        for(UUID id : userIds){
            User user = userRepository.findUserById(id);
            user.removeJoinedChannel(channelId);
            userRepository.save(user);
        }

        channelRepository.deleteChannelById(channelId);
    }

    @Override
    public void removeUser(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findChannelById(channelId);
        channel.removeMember(userId);
        channelRepository.save(channel);
    }

    @Override
    public void removeMessage(UUID channelId, UUID messageId) {
        Channel channel = channelRepository.findChannelById(channelId);
        channel.removeMessage(messageId);
        channelRepository.save(channel);
    }

    @Override
    public void validateChannelExists(UUID channelId) {
        if(!channelRepository.existsById(channelId)){
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }
}
