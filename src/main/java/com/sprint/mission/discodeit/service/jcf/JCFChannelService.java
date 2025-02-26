package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// ChannelService interface를 참조하여 기능구현한다.
public class JCFChannelService implements ChannelService {
    Map<UUID, Channel> channelmap = new HashMap<UUID, Channel>();

    @Override
    public void CreateChannel(String name, String topic){
        Channel newchannel = new Channel(name, topic);
        channelmap.put(newchannel.getId(), newchannel);
    }
    public Optional<Channel> getOneChannel(UUID id){
        return Optional.ofNullable(channelmap.get(id));
    }
    public Map<UUID, Channel> getAllChannel(){
        return channelmap;
    }
    public void UpdateChannel(String newname, String newtopic, UUID id){
        channelmap.get(id).updateChannel(newname, newtopic);
    }
    public void DeleteChannel(UUID id){
        channelmap.remove(id);
    }
}
