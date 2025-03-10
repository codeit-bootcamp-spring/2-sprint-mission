package com.sprint.mission.discodeit.jcf.serviceimpl;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.Set;
import java.util.UUID;

public class JCFChannelServiceImplement implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ValidationService validationService;
    private final UserChannelService userChannelService;

    public JCFChannelServiceImplement(
            ChannelRepository channelRepository,
            ValidationService validationService,
            UserChannelService userChannelService) {
        this.channelRepository = channelRepository;
        this.validationService = validationService;
        this.userChannelService = userChannelService;
    }

    @Override
    public void createChannel(String channelName, UUID userId) {
        validationService.validateString(channelName, "채널 이름");
        validationService.validateUUID(userId, "사용자 ID");
        validationService.validateChannelNameNotExists(channelName);
        validationService.validateUserExists(userId);
        
        Channel channel = new Channel(channelName, userId);
        channelRepository.registerChannel(channel);
        
        // 소유자를 채널에 추가
        userChannelService.addUserToChannel(userId, channel.getChannelId());
    }

    @Override
    public Set<UUID> allChannelList() {
        return channelRepository.AllChannelUserList();
    }

    @Override
    public void setChannelName(UUID channelId, String newChannelName, UUID userId) {
        validationService.validateUUID(channelId, "채널 ID");
        validationService.validateString(newChannelName, "새 채널 이름");
        validationService.validateUUID(userId, "사용자 ID");
        
        Channel channel = validationService.validateChannelExists(channelId);
        validationService.validateUserIsChannelOwner(userId, channelId);
        
        channel.setChannelName(newChannelName);
    }

    @Override
    public Set<UUID> getChannelUserList(UUID channelId) {
        validationService.validateUUID(channelId, "채널 ID");
        validationService.validateChannelExists(channelId);
        
        return userChannelService.getChannelUsers(channelId);
    }

    @Override
    public Channel joinChannel(UUID channelId, UUID userId) {
        validationService.validateUUID(channelId, "채널 ID");
        validationService.validateUUID(userId, "사용자 ID");
        
        Channel channel = validationService.validateChannelExists(channelId);
        validationService.validateUserExists(userId);
        
        userChannelService.addUserToChannel(userId, channelId);
        
        return channel;
    }

    @Override
    public void leaveChannel(UUID channelId, UUID userId) {
        validationService.validateUUID(channelId, "채널 ID");
        validationService.validateUUID(userId, "사용자 ID");
        
        userChannelService.removeUserFromChannel(userId, channelId);
    }
}

