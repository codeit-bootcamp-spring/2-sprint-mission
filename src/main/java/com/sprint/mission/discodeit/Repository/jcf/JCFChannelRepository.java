package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.Channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.Exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JCFChannelRepository implements ChannelRepository {
    private Map<UUID, List<Channel>> channelList = new ConcurrentHashMap<>();

    @Override
    public void reset() {
        channelList = new ConcurrentHashMap<>();
    }

    @Override
    public UUID join(Channel channel, User user) {
        List<User> list = channel.getUserList();
        list.add(user);

        return user.getId();
    }

    @Override
    public UUID quit(Channel channel, User user) {
        List<User> list = channel.getUserList();
        list.remove(user);

        return user.getId();
    }

    @Override
    public UUID save(Server server, Channel channel) {
        List<Channel> channels = channelList.getOrDefault(server.getServerId(), new ArrayList<>());

        channels.add(channel);

        channelList.put(server.getServerId(), channels);

        return channel.getChannelId();
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channel = channelList.values().stream().flatMap(List::stream)
                .filter(c -> c.getChannelId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new ChannelNotFoundException("해당 ID를 가지는 채널을 찾을 수 없습니다."));
        return channel;
    }

    @Override
    public List<Channel> findAllByServerId(UUID serverId) {
        List<Channel> channels = channelList.get(serverId);
        return channels;
    }


    @Override
    public UUID update(Channel targetChannel, ChannelUpdateDTO channelUpdateDTO) {
        if (channelUpdateDTO.replaceChannelId() != null) {
            targetChannel.setChannelId(channelUpdateDTO.replaceChannelId());
        }
        if (channelUpdateDTO.replaceName() != null) {
            targetChannel.setName(channelUpdateDTO.replaceName());
        }
        if (channelUpdateDTO.replaceType() != null) {
            targetChannel.setType(channelUpdateDTO.replaceType());
        }
        return targetChannel.getChannelId();
    }

    @Override
    public void remove(Server server, Channel channel) {
        List<Channel> list = findAllByServerId(server.getServerId());
        list.remove(channel);
    }
}
