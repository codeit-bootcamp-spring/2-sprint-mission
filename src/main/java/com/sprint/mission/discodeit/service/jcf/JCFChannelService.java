package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data = new HashMap<>();

    public void createChannel(Channel channel) { data.put(channel.getId(), channel); }
    public Channel getChannel(UUID id) { return data.get(id); }
    public List<Channel> getAllChannels() { return new ArrayList<>(data.values()); }
    public void updateChannel(UUID id, String name) {
        if (data.containsKey(id)) data.get(id).updateName(name);
    }
    public void deleteChannel(UUID id) { data.remove(id); }
}