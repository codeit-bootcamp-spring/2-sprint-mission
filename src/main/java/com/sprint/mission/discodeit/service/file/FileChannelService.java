package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {

    private static FileChannelService INSTANCE;
    private final String FILE_PATH = "src/main/resources/channels.dat";
    private final UserService userService;
    private  Map<UUID, Channel> channels = new HashMap<>();

    private FileChannelService(UserService userService) {
        loadChannel();
        this.userService = userService;
    }

    public static synchronized FileChannelService getInstance(UserService userService){
        if(INSTANCE == null){
            INSTANCE = new FileChannelService(userService);
        }
        return INSTANCE;
    }

    private void saveChannel(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            throw new RuntimeException("채널 저장 중 오류 발생", e);
        }
    }


    private void loadChannel(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            channels = (Map<UUID, Channel>) ois.readObject();// ✅ 두 번째 Map 로드
        }catch (EOFException e) {
            System.out.println("⚠ channels.dat 파일이 비어 있습니다. 빈 데이터로 유지합니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 로드 중 오류 발생", e);
        }
    }

    public void updateChannelData() {
        saveChannel();
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        channels.put(channel.getId(), channel);
        saveChannel();
        return channel;
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        validateChannelExists(channelId);
        return channels.get(channelId);
    }

    @Override
    public String findChannelNameById(UUID channelId) {
        validateChannelExists(channelId);
        return channels.get(channelId).getChannelName();
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        Channel channel = findChannelById(channelId);
        channel.updateChannelName(newChannelName);
        saveChannel();
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        Channel channel = findChannelById(channelId);

        if (channel.isUserInChannel(userId)) {
            throw new IllegalArgumentException("이미 가입되어 있는 유저입니다.");
        }

        userService.addChannel(userId, channelId);
        userService.updateUserData();
        saveChannel();
    }

    @Override
    public void addMessage(UUID channelId, UUID messageId) {
        Channel channel = findChannelById(channelId);

        channel.addMessages(messageId);
        saveChannel();
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = findChannelById(channelId);

        for (UUID userId : channel.getMembers()) {
            userService.removeChannel(userId, channelId);
        }

        channels.remove(channelId);
        userService.updateUserData();
        saveChannel();
    }

    @Override
    public void removeUser(UUID channelId, UUID userId) {
        Channel channel = findChannelById(channelId);
        if (!channel.isUserInChannel(userId)) {
            throw new IllegalArgumentException("채널에 존재하지 않는 유저입니다.");
        }

        userService.removeChannel(userId, channelId);
        userService.updateUserData();
        channel.removeMember(userId);
        saveChannel();
    }

    @Override
    public void removeMessage(UUID channelId, UUID messageId) {
        Channel channel = findChannelById(channelId);
        if (!channel.isMessageInChannel(messageId)) {
            throw new IllegalArgumentException("체널에 존재하지 않는 메세지 입니다.");
        }

        channel.removeMessage(messageId);
        saveChannel();
    }

    public void validateChannelExists(UUID channelId) {
        if (!channels.containsKey(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }
}

