package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private static FileChannelService INSTANCE;
    private final UserService userService;
    private final FileChannelRepository fileChannelRepository;

    private FileChannelService(UserService userService, FileChannelRepository fileChannelRepository) {
        this.fileChannelRepository = fileChannelRepository;
        this.userService = userService;
    }

    public static synchronized FileChannelService getInstance(UserService userService, FileChannelRepository fileChannelRepository){
        if(INSTANCE == null){
            INSTANCE = new FileChannelService(userService,fileChannelRepository);
        }
        return INSTANCE;
    }

    private void saveChannelData() {
        fileChannelRepository.save();
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        fileChannelRepository.addChannel(channel);
        return channel;
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        validateChannelExists(channelId);
        return fileChannelRepository.findChannelById(channelId);
    }

    @Override
    public String findChannelNameById(UUID channelId) {
        validateChannelExists(channelId);
        return fileChannelRepository.findChannelById(channelId).getChannelName();
    }

    @Override
    public List<Channel> getAllChannels() {
        return fileChannelRepository.findAllChannels();
    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        Channel channel = findChannelById(channelId);
        channel.updateChannelName(newChannelName);
        saveChannelData();
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        Channel channel = findChannelById(channelId);

        if (channel.isUserInChannel(userId)) {
            throw new IllegalArgumentException("이미 가입되어 있는 유저입니다.");
        }

        userService.addChannel(userId, channelId);
        saveChannelData();
    }

    @Override
    public void addMessage(UUID channelId, UUID messageId) {
        Channel channel = findChannelById(channelId);

        channel.addMessages(messageId);
        saveChannelData();
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = findChannelById(channelId);

        for (UUID userId : channel.getMembers()) {
            userService.removeChannel(userId, channelId);
        }

        fileChannelRepository.deleteChannelById(channelId);
    }

    @Override
    public void removeUser(UUID channelId, UUID userId) {
        Channel channel = findChannelById(channelId);
        if (!channel.isUserInChannel(userId)) {
            throw new IllegalArgumentException("채널에 존재하지 않는 유저입니다.");
        }

        userService.removeChannel(userId, channelId);
        channel.removeMember(userId);
        saveChannelData();
    }

    @Override
    public void removeMessage(UUID channelId, UUID messageId) {
        Channel channel = findChannelById(channelId);
        if (!channel.isMessageInChannel(messageId)) {
            throw new IllegalArgumentException("체널에 존재하지 않는 메세지 입니다.");
        }

        channel.removeMessage(messageId);
        saveChannelData();
    }

    public void validateChannelExists(UUID channelId) {
        if (!fileChannelRepository.existsById(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
    }
}

