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
        Channel channel = channelRepository.findByName(name);
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
        Channel checkedChannel = channelRepository.findByName(name);
        if (channelRepository.existsByKey(checkedChannel.getUuid())) {
            throw new IllegalArgumentException("[Error] 동일한 채널명이 존재합니다.");
        }
        Channel channel = new Channel(category, name, introduction, memberKey, ownerKey, userService.getUserName(memberKey), userService.getUserName(ownerKey));
        return channelRepository.save(channel);
    }

    @Override
    public Channel read(String name) {
        Channel channel = channelRepository.findByName(name);
        if (channel == null) {
            throw new IllegalArgumentException("[Error] 조회할 채널이 존재하지 않습니다.");
        }
        return channel;
    }

    @Override
    public List<Channel> readAll(List<String> names) {
        List<Channel> channels = channelRepository.findAllByNames(names);
        if (channels.isEmpty()) {
            throw new IllegalArgumentException("[Error] 조회할 채널이 존재하지 않습니다.");
        }
        return channels;
    }

    @Override
    public Channel update(String inputNameToModify, String category, String name, String introduction, UUID userKey) {
        Channel channel = channelRepository.findByName(inputNameToModify);
        if (channel == null) {
            throw new IllegalArgumentException("[Error] 존재하지 않는 채널입니다.");
        }
        if (!isOwner(channel.getUuid(), userKey)) {
            throw new IllegalArgumentException("[Error] 권한이 부족합니다.");
        }
        if (!category.isEmpty()) {
            channel.updateCategory(category);
        }
        if (!name.isEmpty()) {
            channel.updateName(name);
        }
        if (!name.isEmpty()) {
            channel.updateIntroduction(introduction);
        }
        return channelRepository.save(channel);
    }

    @Override
    public void delete(String name, UUID userKey) {
        Channel channel = channelRepository.findByName(name);
        if (channel == null) {
            throw new IllegalArgumentException("[Error] 존재하지 않는 채널입니다.");
        }
        if (!isOwner(channel.getUuid(), userKey)) {
            throw new IllegalArgumentException("[Error] 권한이 부족합니다.");
        }
        channelRepository.delete(channel.getUuid());
    }

    @Override
    public UUID login(String name, UUID userKey) {
        Channel channel = channelRepository.findByName(name);
        if (channel == null) {
            throw new IllegalArgumentException("[Error] 채널을 찾을 수 없습니다.");
        }
        if (!isSignUp(name,userKey)) {
            throw new IllegalStateException("[Error] 해당 채널에 가입되지 않았습니다.");
        }
        return channel.getUuid();
    }

    @Override
    public String getChannelName(UUID channelKey) {
        if (channelKey == null) {
            throw new IllegalArgumentException("[Error] 채널을 찾을 수 없습니다.");
        }
        return channelRepository.findByKey(channelKey).getName();
    }

    private boolean isSignUp(String name, UUID userKey) {
        Channel channel = channelRepository.findByName(name);
        if (channel == null) {
            throw new IllegalArgumentException("[Error] 채널을 찾을 수 없습니다.");
        }
        return channel.getMemberKeys().contains(userKey);
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
