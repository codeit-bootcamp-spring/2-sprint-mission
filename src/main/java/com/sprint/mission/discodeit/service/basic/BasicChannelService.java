package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

// JCFChannelService, FileChannelService, BasicChannelService 전부 동일합니다. 최종적으로는 BasicChannelService 사용합니다 (스프린트 요구 사항으로 남겨두었습니다.)
public class BasicChannelService implements ChannelService {
    private static volatile BasicChannelService instance;

    private final UserService userService;
    private final ChannelRepository channelRepository;

    private BasicChannelService(UserService userService, ChannelRepository channelRepository) {
        this.userService = userService;
        this.channelRepository = channelRepository;
    }

    public static BasicChannelService getInstance(UserService userService, ChannelRepository channelRepository) {
        if (instance == null) {
            synchronized (BasicChannelService.class) {
                if (instance == null) {
                    instance = new BasicChannelService(userService, channelRepository);
                }
            }
        }
        return instance;
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
        return channelRepository.findById(channelId);
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
        return channelRepository.save(channel);
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
        if (channelRepository.exists(channelId) == false) {
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

