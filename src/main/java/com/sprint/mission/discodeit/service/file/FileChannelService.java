package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private Map<UUID, Channel> data = new HashMap<>();
    private final UserService userService;
    private static final String FILE_NAME = "channel.sar";

    public FileChannelService(UserService userService) {
        this.userService = userService;
        loadFromFile();
    }

    @Override
    public UUID signUp(String name, UUID userKey) {
        Channel channel = data.values().stream()
                .filter(c -> c.getName().equals(name) && c.getMemberKeys().contains(userKey))
                .findFirst()
                .orElse(null);

        if (channel == null) {
            throw new IllegalArgumentException("[Error] 가입하려는 채널이 존재하지 않습니다.");
        }
        if (isSignUp(name, userKey)) {
            throw new IllegalStateException("[Error] 이미 가입된 채널입니다.");
        }
        channel.updateMemberKeys(userKey);
        channel.updateMemberNames(userService.getUserName(userKey));
        saveToFile();
        return channel.getUuid();
    }

    @Override
    public Channel create(String category, String name, String introduction, UUID memberKey, UUID ownerKey) {
        if (isChannelCheck(name)) {
            throw new IllegalArgumentException("[Error] 동일한 채널명이 존재합니다.");
        }
        if (userService.getUserName(memberKey) == null || userService.getUserName(ownerKey) == null) {
            throw new IllegalStateException("[Error] 존재하지 않는 유저 입니다.");
        }
        Channel channel = new Channel(category, name, introduction, memberKey, ownerKey, userService.getUserName(memberKey), userService.getUserName(ownerKey));
        data.put(channel.getUuid(), channel);
        saveToFile();
        return channel;
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
    public Channel update(String inputNameToModify, String category, String name, String introduction, UUID userKey) {
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
        saveToFile();
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
        saveToFile();
    }

    @Override
    public String getChannelName(UUID channelKey) {
        if (channelKey == null) {
            throw new IllegalStateException("[Error] 올바르지 않은 Key");
        }
        return data.get(channelKey).getName();
    }

    @Override
    public UUID login(String name, UUID userKey) {
        UUID channelKey = convertToKey(name, userKey);
        if (channelKey == null) {
            throw new IllegalArgumentException("[Error] 채널을 찾을 수 없습니다.");
        }
        if (!isSignUp(name,userKey)) {
            throw new IllegalStateException("[Error] 해당 채널에 가입되지 않았습니다.");
        }
        return channelKey;
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

    private void saveToFile() {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("[Error] 사용자 데이터를 저장하는 중 문제가 발생 했습니다.");
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("[Info] 채널 데이터 파일이 없습니다. 빈 데이터로 시작합니다.");
            data = new HashMap<>();
            return;
        }

        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object obj = ois.readObject();
            if (obj instanceof Map<?, ?> rawMap) {
                for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
                    if (entry.getKey() instanceof UUID key && entry.getValue() instanceof Channel value) {
                        data.put(key, value);
                    }
                }
            }
            System.out.println("[Info] 사용자 데이터를 성공적으로 로드했습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Error] 사용자 데이터를 불러오는 중 문제가 발생했습니다.");
            data = new HashMap<>();
        }
    }
}
