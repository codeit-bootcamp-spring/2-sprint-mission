package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.util.FilePathUtil;
import com.sprint.mission.discodeit.util.FileSerializationUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "channels");
    private final FileSerializationUtil fileUtil;
    private static FileChannelService channelService;

    private FileChannelService(FileSerializationUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public static FileChannelService getInstance(FileSerializationUtil fileUtil) {
        if (channelService == null) {
            channelService = new FileChannelService(fileUtil);
        }
        return channelService;
    }


    @Override
    public void create(Channel channel) {
        fileUtil.<Channel>writeObjectToFile(channel, FilePathUtil.getFilePath(directory, channel.getId()));
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return fileUtil.<Channel>readObjectFromFile(FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public List<Channel> findAll() {
        if (Files.exists(directory)) {
            try {
                return Files.list(directory)
                        .map(fileUtil::<Channel>readObjectFromFile) // Optional<User> 반환
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();
            } catch (Exception e) {
                throw new RuntimeException("채널 읽기에 실패했습니다.", e);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void update(UUID id, String name, String description) {
        Channel channel = findById(id).orElseThrow(() -> new RuntimeException("해당 ID의 채널이 존재하지 않습니다: " + id));
        channel.setName(name);
        channel.setDescription(description);
        fileUtil.<Channel>writeObjectToFile(channel, FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public void delete(UUID id) {
        fileUtil.deleteFile(FilePathUtil.getFilePath(directory, id));
    }
}
