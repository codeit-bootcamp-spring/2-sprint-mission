package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private UserService userService;
    private ChannelRepository channelRepository;

    public JCFChannelService(UserService userService, ChannelRepository channelRepository) {
        this.userService = userService;
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel createChannel(UUID ownerId, String title, String description) {
        userService.validateUserId(ownerId);
        return channelRepository.save(new Channel(ownerId, title, description));
    }

    @Override
    public UUID getChannelOwnerId(UUID channelId) {
        return getChannelByChannelId(channelId).getOwnerId();
    }

    @Override
    public Channel getChannelByChannelId(UUID channelId) {
        validateChannelId(channelId);
        return channelRepository.find(channelId);
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public Set<UUID> getChannelMembers(UUID channelId) {
        validateChannelId(channelId);
        return channelRepository.findMemberIds(channelId);
    }

    @Override
    public Channel updateChannel(UUID channelId, String title, String description) {
        validateChannelId(channelId);
        Channel channel = getChannelByChannelId(channelId);
        channel.update(channelId, title, description);
        return channel;
    }

    @Override
    public void deleteChannelByChannelId(UUID channelId) {
        validateChannelId(channelId);
        channelRepository.delete(channelId);
    }

    @Override
    public Channel addUserToChannel(UUID channelId, UUID userId) {
        userService.validateUserId(userId);
        validateChannelId(channelId);
        channelRepository.addMember(channelId, userId);
        return getChannelByChannelId(channelId);
    }

    @Override
    public void deleteUserFromChannel(UUID channelId, UUID userId) {
        userService.validateUserId(userId);
        validateChannelId(channelId);
        validateNotOwnerDeletion(channelId, userId);
        channelRepository.removeMember(channelId, userId);
    }

    @Override
    public boolean isChannelMember(UUID channelId, UUID userId) {
        userService.validateUserId(userId);
        validateChannelId(channelId);

        Set<UUID> channelMembers = channelRepository.findMemberIds(channelId);

        return channelMembers.stream()
                .anyMatch(memberId -> memberId.equals(userId));
    }

    @Override
    public void validateChannelId(UUID channelId) {
        if (!channelRepository.exists(channelId)) {
            throw new ChannelNotFoundException("없는 채널");
        }
    }

    private void validateNotOwnerDeletion(UUID channelId, UUID userId) {
        if (isChannelOwner(channelId, userId)) {
            throw new IllegalArgumentException("채널 소유자 삭제 불가");
        }
    }

    private boolean isChannelOwner(UUID channelId, UUID userId) {
        return userId.equals(getChannelOwnerId(channelId));
    }

}

