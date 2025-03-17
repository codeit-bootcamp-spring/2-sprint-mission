package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.Channel.ChannelCRUDDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Exception.EmptyUserListException;
import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
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
        if (list.isEmpty()) {
            throw new EmptyUserListException("채널 내 유저 리스트가 비어있습니다.");
        }
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
        List<Channel> list = channelList.values().stream().flatMap(List::stream).toList();
        Channel channel = CommonUtils.findById(list, channelId, Channel::getChannelId)
                .orElseThrow(() -> CommonExceptions.CHANNEL_NOF_FOUND);
        return channel;
    }

    @Override
    public List<Channel> findAllByServerId(UUID serverId) {
        if (channelList.isEmpty()) {
            throw CommonExceptions.EMPTY_CHANNEL_LIST;
        }
        List<Channel> channels = channelList.get(serverId);

        if (channels.isEmpty()) {
            throw CommonExceptions.EMPTY_CHANNEL_LIST;
        }
        return channels;
    }


    @Override
    public UUID update(Channel targetChannel, ChannelCRUDDTO channelUpdateDTO) {
        if (channelUpdateDTO.channelId() != null) {
            targetChannel.setChannelId(channelUpdateDTO.channelId());
        }
        if (channelUpdateDTO.name() != null) {
            targetChannel.setName(channelUpdateDTO.name());
        }
        if (channelUpdateDTO.type() != null) {
            targetChannel.setType(channelUpdateDTO.type());
        }
        return targetChannel.getChannelId();
    }

    @Override
    public void remove(Server server, Channel channel) {
        List<Channel> list = findAllByServerId(server.getServerId());
        list.remove(channel);
    }
}
