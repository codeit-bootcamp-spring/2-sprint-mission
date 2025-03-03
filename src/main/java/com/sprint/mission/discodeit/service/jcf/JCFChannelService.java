package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.ChannelEntity;
import com.sprint.mission.discodeit.entity.MessageEntity;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelService extends JCFBaseService<ChannelEntity> implements ChannelService{

    private final MessageService messageService; //채널 삭제 시 메시지도 삭제.

    public JCFChannelService(MessageService messageService){
        this.messageService = messageService;
    }

    @Override
    public Optional<ChannelEntity> getChannelByName(String channelName){
        return data.stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(channelName))
                .findFirst();

    }

    @Override
    public void updateChannelName(String channelName, String newName) {
        getChannelByName(channelName).ifPresent(channel -> channel.updateChannelName(newName));
    }

    @Override
    public void updateChannelType(String channelName, String newType) {
        getChannelByName(channelName).ifPresent(channel -> channel.updateChannelType(newType));
    }

    @Override
    public void deleteById(UUID channelId){
        findById(channelId).ifPresent(channel -> {
            delete(channel);
        });
    }

    @Override
    public void delete(ChannelEntity channel) {
        List<MessageEntity> messagesToDelete = channel.getMessages();
        messagesToDelete.forEach(messageService::delete);
        messagesToDelete.clear();

        data.remove(channel);
    }
}
