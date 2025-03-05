package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {

    private final String FILE_PATH = "src/main/resources/channels.dat";
    private  Map<UUID, Channel> channels = new HashMap<>();
    private  Map<String, UUID> channelIds = new HashMap<>();
    private final UserService userService;
    private static FileChannelService INSTANCE;

    private FileChannelService(UserService userService) {
        loadChannelsFromFile();
        this.userService = userService;
    }

    public static synchronized FileChannelService getInstance(UserService userService){
        if(INSTANCE == null){
            INSTANCE = new FileChannelService(userService);
        }
        return INSTANCE;
    }

    private void saveChannelsToFile(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channels);   // ✅ 첫 번째 Map 저장
            oos.writeObject(channelIds); // ✅ 두 번째 Map 저장
        } catch (IOException e) {
            throw new RuntimeException("채널 저장 중 오류 발생", e);
        }
    }


    private void loadChannelsFromFile(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            channels = (Map<UUID, Channel>) ois.readObject();   // ✅ 첫 번째 Map 로드
            channelIds = (Map<String, UUID>) ois.readObject();  // ✅ 두 번째 Map 로드
        }catch (EOFException e) {
            System.out.println("⚠ channels.dat 파일이 비어 있습니다. 빈 데이터로 유지합니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 로드 중 오류 발생", e);
        }
    }

    @Override
    public void updataChannelData() {
        saveChannelsToFile();
    }

    @Override
    public void createChannel(String channelName) {
        if(channelIds.containsKey(channelName)){
            throw new IllegalArgumentException("이미 존재하는 채널입니다.");
        }

        Channel channel = new Channel(channelName);
        channels.put(channel.getId(), channel);
        saveChannelsToFile();
        channelIds.put(channelName, channel.getId());
    }

    @Override
    public Channel getChannelById(UUID channelId) {
        validateChannelExists(channelId);
        return channels.get(channelId);
    }

    @Override
    public String getChannelNameById(UUID channelId) {
        validateChannelExists(channelId);
        return channels.get(channelId).getChannelName();
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        Channel channel = getChannelById(channelId);

        // 채널명이 이미 존재하는지 확인
        for (Channel existingChannel : channels.values()) {
            if (existingChannel.getChannelName().equals(newChannelName)) {
                throw new IllegalArgumentException("이미 존재하는 채널명입니다.");
            }
        }

        String oldChannelName = channel.getChannelName();
        channel.updateChannelName(newChannelName);
        channelIds.remove(oldChannelName);
        channelIds.put(newChannelName, channel.getId());
        saveChannelsToFile();
    }

    @Override
    public void addUserToChannel(UUID channelId, UUID userId) {
        Channel channel = getChannelById(channelId);
        User user = userService.getUserById(userId);

        if (channel.isUserInChannel(userId)) {
            throw new IllegalArgumentException("이미 가입되어 있는 유저입니다.");
        }

        channel.addMembers(userId);
        user.addJoinedChannel(channelId);
        userService.updataUserData();
        saveChannelsToFile();
    }

    @Override
    public void addMessageToChannel(UUID channelId, UUID messageId) {
        Channel channel = getChannelById(channelId);

        channel.addMessages(messageId);
        saveChannelsToFile();
    }

    @Override
    public void removeChannel(UUID channelId) {
        Channel channel = getChannelById(channelId);

        for (UUID userId : channel.getMembers()) {
            User user = userService.getUserById(userId);
            if (user != null) {
                user.removeJoinedChannel(channelId);
            }
        }

        userService.updataUserData();
        channelIds.remove(channel.getChannelName());
        channels.remove(channelId);
        saveChannelsToFile();
    }

    @Override
    public void removeUserFromChannel(UUID channelId, UUID userId) {
        Channel channel = getChannelById(channelId);
        if (!channel.isUserInChannel(userId)) {
            throw new IllegalArgumentException("채널에 존재하지 않는 유저입니다.");
        }

        User user = userService.getUserById(userId);
        user.removeJoinedChannel(channelId);
        userService.updataUserData();
        channel.removeMember(userId);
        saveChannelsToFile();
    }

    @Override
    public void removeMessageFromChannel(UUID channelId, UUID messageId) {
        Channel channel = getChannelById(channelId);
        if (!channel.isMessageInChannel(messageId)) {
            throw new IllegalArgumentException("체널에 존재하지 않는 메세지 입니다.");
        }

        channel.removeMessage(messageId);
        saveChannelsToFile();
    }

    public void validateChannelExists(UUID channelId) {
        if (!channels.containsKey(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }
}

