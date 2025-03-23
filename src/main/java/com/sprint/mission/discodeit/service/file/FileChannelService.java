package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileChannelService() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", User.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Path getFilePath(UUID channelId) {
        return DIRECTORY.resolve("channel-" + channelId + ".data");
    }

    private void serializeChannel(Channel channel, Path path) {
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException("사용자 데이터를 저장하는 중 오류 발생: " + path, e);
        }
    }

    private Channel deserializeChannel(Path path) {
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("사용자 파일을 읽는 중 오류 발생: " + path, e);
        }
    }


    @Override
    public Channel createChannel(ChannelType type, String channelName, String description) {
        Channel channel = new Channel(type, channelName, description);
        serializeChannel(channel, getFilePath(channel.getId()));
        System.out.println("채널이 생성되었습니다: \n" + channel);
        return channel;
    }

    @Override
    public Channel searchChannel(UUID channelId) {
        return deserializeChannel(getFilePath(channelId));
    }

    @Override
    public List<Channel> searchAllChannels() {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> deserializeChannel(path))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Channel updateAll(UUID channelId, String channelName, String description) {
        existChannel(channelId);
        Path path = getFilePath(channelId);
        Channel channel = deserializeChannel(path);
        channel.updateAll(channelName, description);
        serializeChannel(channel, path);
        return channel;
    }

    @Override
    public Channel updateChannelName(UUID channelId, String channelName) {
        existChannel(channelId);
        Path path = getFilePath(channelId);
        Channel channel = deserializeChannel(path);
        channel.updateChannelName(channelName);
        serializeChannel(channel, path);
        return channel;
    }

    @Override
    public Channel updateChannelDescription(UUID channelId, String description) {
        existChannel(channelId);
        Path path = getFilePath(channelId);
        Channel channel = deserializeChannel(path);
        channel.updateChannelDescription(description);
        serializeChannel(channel, path);
        return channel;
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Path path = getFilePath(channelId);
        try {
            existChannel(channelId);
            Files.delete(path);
            System.out.println(channelId + " 채널 삭제 완료되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException("채널 삭제 실패: " + channelId, e);
        }
    }

    public boolean existChannel(UUID channelId) {
        Path path = getFilePath(channelId);
        if (!Files.exists(path)) {
            throw new NoSuchElementException("채널이 존재하지 않습니다." + channelId);
        }
        return true;
    }
}