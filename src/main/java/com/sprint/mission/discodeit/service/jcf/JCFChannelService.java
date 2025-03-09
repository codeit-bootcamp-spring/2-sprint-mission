package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

// ChannelService interface를 참조하여 기능구현한다.
public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> channelmap = new HashMap<UUID, Channel>();

    @Override
    public void createChannel(String name, String topic){
        Channel newchannel = new Channel(name, topic);
        channelmap.put(newchannel.getId(), newchannel);
    }
    public Optional<Channel> getOneChannel(UUID id){
        return Optional.ofNullable(channelmap.get(id));
    }
    public List<Channel> getAllChannel(){
        List<Channel> channelList = new ArrayList<Channel>(channelmap.values());
        return channelList;
    }
    public void updateChannel(String newname, String newtopic, UUID id){
        channelmap.get(id).updateChannel(newname, newtopic);
    }
    public void deleteChannel(UUID id){
        channelmap.remove(id);
    }
}
