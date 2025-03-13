package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {
    private static FileChannelService instance;
    private final FileChannelRepository fileChannelRepository;

    public FileChannelService(FileChannelRepository fileChannelRepository) {
        this.fileChannelRepository = fileChannelRepository;
    }

    public static synchronized FileChannelService getInstance(FileChannelRepository fileChannelRepository) {
        if (instance == null) {
            instance = new FileChannelService(fileChannelRepository);
        }
        return instance;
    }


    @Override
    public Channel create(ChannelType type, String channelName, String description) {
        Channel channel = new Channel(type, channelName, description);
        fileChannelRepository.save(channel);
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return fileChannelRepository.findAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Channel findById(UUID channelId) {
        return fileChannelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다 : " + channelId));
    }

    @Override
    public Channel update(UUID channelId, String newChannelName, String newDescription) {
        Channel existingChannel = fileChannelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다 : " + channelId));
        fileChannelRepository.save(existingChannel);
        return existingChannel;
    }

    @Override
    public void delete(UUID channelId) {
        if (!fileChannelRepository.delete(channelId)) {
            throw new NoSuchElementException("해당 채널을 찾을 수 없습니다 : " + channelId);
        }
    }
}
