package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    final Map<UUID, Channel> data = new HashMap<>();
    private final UserService userService;

    public JCFChannelService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UUID signUp(String name, UUID userKey) {
        Channel channel = data.get(getChannelKey(name,userKey));

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
    public UUID create(String category, String name, String introduction, UUID memberKey, UUID ownerKey) {

        if (isChannelCheck(name)) {
            throw new IllegalArgumentException("[Error] 동일한 채널명이 존재합니다.");
        }
        if (userService.getUserName(memberKey) == null || userService.getUserName(ownerKey) == null) {
            throw new IllegalStateException("[Error] 존재하지 않는 유저 입니다.");
        }
        Channel channel = new Channel(category, name, introduction, memberKey, ownerKey, userService.getUserName(memberKey), userService.getUserName(ownerKey));
        data.put(channel.getUuid(), channel);
        return channel.getUuid();
    }

    @Override
    public Channel read(String name) {
        UUID channelKey = convertToKey(name);
        Channel channel = data.get(channelKey);
        if (channel == null) {
            throw new IllegalArgumentException("[Error] 조회할 채널이 존재하지 않습니다.");
        }
        return data.get(channelKey);
    }

    @Override
    public List<Channel> readAll(List<String> nameList) {
        List<UUID> channelsKey = convertToKey(nameList);
        List<Channel> channels = channelsKey.stream().map(data::get).toList();
        if (channels.isEmpty()) {
            throw new IllegalArgumentException("[Error] 조회할 채널이 존재하지 않습니다.");
        }
        return channels;
    }

    @Override
    public List<Channel> readAll() {
        List<Channel> info = data.values().stream().toList();
        if (info.isEmpty()) {
            throw new IllegalStateException("[Error] 조회할 채널이 존재하지 않습니다.");
        }
        return info;
    }

    @Override
    public Channel update(String inputNameToModify, String category, String name, String introduction) {
        UUID channelKey = convertToKey(inputNameToModify);
        Channel currentChannel = data.get(channelKey);

        if (currentChannel == null) {
            throw new IllegalArgumentException("[Error] 존재하지 않는 채널입니다.");
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
        return currentChannel;
    }

    @Override
    public void delete(String name, UUID userKey) {
        UUID channelUuid = convertToKey(name, userKey);
        Channel channel = data.get(channelUuid);
        if (channel == null) {
            throw new IllegalArgumentException("[Error] 존재하지 않는 채널입니다.");
        }
        data.remove(channelUuid);
    }

    @Override
    public UUID getChannelKey(String inputName, UUID userKey) {
        UUID channelKey = convertToKey(inputName, userKey);
        if (channelKey == null) {
            throw new IllegalArgumentException("[Error] 올바르지 않은 Key");
        }
        return channelKey;
    }

    @Override
    public String getChannelName(UUID channelKey) {
        if (channelKey == null) {
            throw new IllegalStateException("[Error] 올바르지 않은 Key");
        }
        return data.get(channelKey).getName();
    }


    private boolean isChannelCheck(String name) {
        return data.values().stream()
                .anyMatch(c -> c.getName().equals(name));
    }

    private boolean isSignUp(String name, UUID userKey) {
        return data.values().stream()
                .anyMatch(c -> c.getName().equals(name) && c.getMemberKeys().contains(userKey));
    }

    private UUID convertToKey(String inputName, UUID userKey) {
        return data.values().stream()
                .filter(c -> c.getName().equals(inputName) && c.getMemberKeys().contains(userKey))
                .map(Channel::getUuid)
                .findFirst()
                .orElse(null);
    }

    private UUID convertToKey(String inputName) {
        return data.values().stream()
                .filter(c -> c.getName().equals(inputName))
                .map(Channel::getUuid)
                .findFirst()
                .orElse(null);
    }

    private List<UUID> convertToKey(List<String> inputNameList) {
        return data.values().stream()
                .filter(c -> inputNameList.contains(c.getName()))
                .map(Channel::getUuid)
                .toList();
    }
}
