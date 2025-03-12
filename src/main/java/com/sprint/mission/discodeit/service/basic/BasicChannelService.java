package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    ChannelRepository channelRepository;
    UserService userService;
    public BasicChannelService(ChannelRepository channelRepository, UserService userService) {
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    @Override
    public UUID signUp(String name, UUID userKey) {
        UUID channelKey = channelRepository.findKeyByName(name);
        Channel channel = channelRepository.findByKey(channelKey);
        if (channel == null) {
            throw new IllegalArgumentException("[Error] 가입하려는 채널이 존재하지 않습니다.");
        }
        if (isSignUp(name, userKey)) {
            throw new IllegalStateException("[Error] 이미 가입된 채널입니다.");
        }
        channel.updateMemberKeys(userKey);
        channel.updateMemberNames(userService.getUserName(userKey));
        return channel.getUuid();
    }

    @Override
    public Channel create(String category, String name, String introduction, UUID memberKey, UUID ownerKey) {
        UUID channelKey = channelRepository.findKeyByName(name);
        if (channelRepository.existsByKey(channelKey)) {
            throw new IllegalArgumentException("[Error] 동일한 채널명이 존재합니다.");
        }
        Channel channel = new Channel(category, name, introduction, memberKey, ownerKey, userService.getUserName(memberKey), userService.getUserName(ownerKey));
        return channelRepository.save(channel);
    }

    @Override
    public Channel read(String name) {
        UUID channelKey = channelRepository.findKeyByName(name);
        if (channelKey == null) {
            throw new IllegalArgumentException("[Error] 조회할 채널이 존재하지 않습니다.");
        }
        return channelRepository.findByKey(channelKey);
    }

    @Override
    public List<Channel> readAll(List<String> names) {
        List<UUID> channelsKeys = channelRepository.findKeyByNames(names);
        if (channelsKeys.isEmpty()) {
            throw new IllegalArgumentException("[Error] 조회할 채널이 존재하지 않습니다.");
        }
        return channelRepository.findAllByKeys(channelsKeys);
    }

    @Override
    public Channel update(String inputNameToModify, String category, String name, String introduction, UUID userKey) {
        UUID channelKey = channelRepository.findKeyByName(inputNameToModify);
        Channel currentChannel = channelRepository.findByKey(channelKey);

        if (channelKey == null) {
            throw new IllegalArgumentException("[Error] 존재하지 않는 채널입니다.");
        }
        if (!isOwner(channelKey, userKey)) {
            throw new IllegalArgumentException("[Error] 권한이 부족합니다.");
        }
        if (!category.isEmpty()) {
            currentChannel.updateCategory(category);
        }
        if (!name.isEmpty()) {
            currentChannel.updateName(name);
        }
        if (!name.isEmpty()) {
            currentChannel.updateIntroduction(introduction);
        }
        return channelRepository.save(currentChannel);
    }

    @Override
    public void delete(String name, UUID userKey) {
        UUID channelKey = channelRepository.findKeyByName(name);

        if (!isOwner(channelKey, userKey)) {
            throw new IllegalArgumentException("[Error] 권한이 부족합니다.");
        }
        if (channelKey == null) {
            throw new IllegalArgumentException("[Error] 존재하지 않는 채널입니다.");
        }
        channelRepository.delete(channelKey);
    }

    @Override
    public UUID login(String name, UUID userKey) {
        UUID channelKey = channelRepository.findKeyByName(name);
        if (channelKey == null) {
            throw new IllegalArgumentException("[Error] 채널을 찾을 수 없습니다.");
        }
        if (!isSignUp(name,userKey)) {
            throw new IllegalStateException("[Error] 해당 채널에 가입되지 않았습니다.");
        }
        return channelKey;
    }

    @Override
    public String getChannelName(UUID channelKey) {
        if (channelKey == null) {
            throw new IllegalArgumentException("[Error] 채널을 찾을 수 없습니다.");
        }
        return channelRepository.findByKey(channelKey).getName();
    }

    private boolean isSignUp(String name, UUID userKey) {
        UUID channelKey = channelRepository.findKeyByName(name);
        if (channelKey == null) {
            throw new IllegalArgumentException("[Error] 채널을 찾을 수 없습니다.");
        }
        return channelRepository.findByKey(channelKey).getMemberKeys().contains(userKey);
    }

    private boolean isOwner(UUID channelKey, UUID userKey) {
        if (channelKey == null) {
            throw new IllegalArgumentException("[Error] 채널을 찾을 수 없습니다.");
        }
        if (userKey == null) {
            throw new IllegalArgumentException("[Error] 유저를 찾을 수 없습니다.");
        }
        return channelRepository.findByKey(channelKey).getOwnerKey().equals(userKey);
    }
}
