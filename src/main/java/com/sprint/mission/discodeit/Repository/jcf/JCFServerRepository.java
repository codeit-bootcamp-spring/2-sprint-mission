package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.Exception.*;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFServerRepository implements ServerRepository {
    private Map<UUID, List<User>> channelUsers = new ConcurrentHashMap<>();
    private Map<UUID, List<Channel>> channelList = new ConcurrentHashMap<>();

    @Override
    public UUID saveUser(Channel channel, User user) {
        channelUsers.computeIfAbsent(channel.getChannelId(), k -> new ArrayList<>()).add(user);

        return user.getId();
    }

    @Override
    public UUID saveChannel(Server server, Channel channel) {
        channelList.computeIfAbsent(server.getServerId(), k -> new ArrayList<>()).add(channel);

        return channel.getChannelId();
    }

    @Override
    public User findUser(Channel channel, User user) {
        List<User> users = findUserListByChannelId(channel.getChannelId());
        User findUser = users.stream().filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("채널 내 해당 유저가 없습니다."));
        return findUser;
    }

    @Override
    public Channel findChannel(Server server, Channel channel) {
        return findChannelByChanelId(server, channel.getChannelId());
    }



    @Override
    public Channel findChannelByChanelId(Server server, UUID channelId) {
        List<Channel> channels = findChannelListByServerId(server.getServerId());
        Channel findChannel = channels.stream().filter(c -> c.getChannelId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new ChannelNotFoundException("채널이 존재하지 않습니다."));
        return findChannel;
    }

    @Override
    public List<User> findUserListByChannelId(UUID channelId) {
        List<User> users = Optional.ofNullable(channelUsers.get(channelId))
                .orElseThrow(() -> new EmptyUserListException("유저 리스트가 비어있습니다."));
        return users;
    }

    @Override
    public List<Channel> findChannelListByServerId(UUID serverId) {
        List<Channel> channels = Optional.ofNullable(channelList.get(serverId))
                .orElseThrow(() -> new EmptyChannelListException("채널 리스트가 비어있습니다."));
        return channels;
    }

    @Override
    public UUID updateChannelName(Server server, Channel channel, String replaceName) {
        Channel findChannel = findChannel(server, channel);
        findChannel.setName(replaceName);
        return findChannel.getChannelId();
    }

    @Override
    public UUID removeChannel(Server server, Channel channel) {
        List<Channel> channels = findChannelListByServerId(server.getServerId());
        Channel findChannel = findChannel(server, channel);

        channels.remove(findChannel);
        channelList.put(server.getServerId(), channels);
        return findChannel.getChannelId();
    }

    @Override
    public UUID quitChannel(Channel channel, User user) {
        List<User> users = findUserListByChannelId(channel.getChannelId());
        User findUser = findUser(channel, user);

        users.remove(findUser);
        channelUsers.put(channel.getChannelId(), users);

        return findUser.getId();
    }
}
