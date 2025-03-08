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

    public BasicChannelService(ChannelRepository channelRepository, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel getChannelById(UUID channelId) {
        return channelRepository.findById(channelId);
    }

    @Override
    public String getChannelNameById(UUID channelId) {
        return channelRepository.findById(channelId).getChannelName();
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void updataChannelData() {

    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        Channel channel = channelRepository.findById(channelId);
        channel.updateChannelName(newChannelName);
        channelRepository.save(channel);
    }

    @Override
    public void addUserToChannel(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId);
        User user = userRepository.findById(userId);

        channel.addMembers(userId);
        user.addJoinedChannel(channelId);

        userRepository.save(user);
        channelRepository.save(channel);
    }

    @Override
    public void addMessageToChannel(UUID channelId, UUID messageId) {
        Channel channel = channelRepository.findById(channelId);
        channel.addMessages(messageId);
        channelRepository.save(channel);
    }

    @Override
    public void removeChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);
        Set<UUID> userIds = channel.getMembers();
        for(UUID id : userIds){
            User user = userRepository.findById(id);
            user.removeJoinedChannel(channelId);
            userRepository.save(user);
        }

        channelRepository.deleteById(channelId);
    }

    @Override
    public void removeUserFromChannel(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId);
        channel.deleteMember(userId);
        channelRepository.save(channel);
    }

    @Override
    public void removeMessageFromChannel(UUID channelId, UUID messageId) {
        Channel channel = channelRepository.findById(channelId);
        channel.removeMessage(messageId);
        channelRepository.save(channel);
    }

    @Override
    public void validateChannelExists(UUID channelId) {
        if(!channelRepository.exists(channelId)){
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }
}
