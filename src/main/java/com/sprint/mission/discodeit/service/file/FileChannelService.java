package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private static final Path directoryPath = Paths.get("data/channels");
    private static FileChannelService instance = null;

    public static synchronized FileChannelService getInstance() {
        if (instance == null) {
            instance = new FileChannelService();
        }
        return instance;
    }

    private FileChannelService() {
        try {
            Files.createDirectories(directoryPath);
        } catch (IOException e) {
            throw new RuntimeException("디렉토리를 생성할 수 없습니다: " + e.getMessage());
        }
    }

    @Override
    public UUID createChannel() {
        Channel channel = new Channel();
        saveChannel(channel);
        System.out.println("채널이 생성되었습니다: \n" + channel);
        return channel.getId();
    }

    public void saveChannel(Channel channel) {
        Path filePath = getFilePath(channel.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException("채널 저장 실패: " + channel.getId(), e);
        }
    }

    public Channel loadChannel(Path filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 데이터 읽기 실패: " + filePath, e);
        }
    }

    public Path getFilePath(UUID channelId) {
        return directoryPath.resolve("channel-" + channelId + ".data");
    }

    @Override
    public void searchChannel(UUID id) {
        Path filePath = getFilePath(id);
        if (!Files.exists(filePath)) {
            System.out.println("조회하신 채널이 존재하지 않습니다.");
            return;
        }
        Channel channel = loadChannel(filePath);
        System.out.println("CHANNEL: " + channel);
    }

    @Override
    public void searchAllChannels() {
        try {
            Files.list(directoryPath)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        Channel channel = loadChannel(path);
                        System.out.println("CHANNEL: " + channel);
                    });
        } catch (IOException e) {
            throw new RuntimeException("채널 목록 읽기 실패: " + e);
        }
    }

    @Override
    public void updateChannel(UUID id) {
        Path filePath = getFilePath(id);
        if (!Files.exists(filePath)) {
            System.out.println("업데이트할 채널이 존재하지 않습니다.");
            return;
        }
        Channel channel = loadChannel(filePath);
        channel.updateTime(System.currentTimeMillis());
        saveChannel(channel);
        System.out.println(id + " 채널 업데이트 완료되었습니다.");
    }

    @Override
    public void deleteChannel(UUID id) {
        Path filePath = getFilePath(id);
        try {
            Files.deleteIfExists(filePath);
            System.out.println(id + " 채널 삭제 완료되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException("채널 삭제 실패: " + id, e);
        }
    }

    public boolean existChannel(UUID id) {
        return Files.exists(getFilePath(id));
    }
}