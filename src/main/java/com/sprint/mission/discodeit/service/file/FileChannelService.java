package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class FileChannelService implements ChannelService {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data", "channel");

    private final List<Channel> channelsData;

    public FileChannelService() {
        channelsData = new ArrayList<>();
    }

    // 채널 생성
    @Override
    public Channel create(Channel channel) {
        if (find(channel.getChannelName()) != null) {
            System.out.println("등록된 채널이 존재합니다.");
            return null;
        } else {
            channelsData.add(channel);
            save(channel);
            System.out.println(channel);
            return channel;
        }
    }

    private void save(Channel channel) {
        init();
        Path path = directory.resolve(channel.getId() + ".ser");
        saveToFile(path, channel);
    }

    private void init() {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveToFile(Path path, Channel channel) {
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    // 채널 단일 조회
    @Override
    public Channel getChannel(String channelName) {
        return find(channelName);
    }

    private Channel find(String channelName) {
        return load().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findAny()
                .orElse(null);
    }

    private List<Channel> load() {
        if (Files.exists(directory)) {
            try (Stream<Path> path = Files.list(directory)) {
                return path
                        .map(this::loadToFile)
                        .toList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return Collections.emptyList();
        }
    }

    private Channel loadToFile(Path path) {
        try (FileInputStream fis = new FileInputStream(path.toFile());
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    // 채널 전체 조회
    @Override
    public List<Channel> getAllChannel() {
        List<Channel> channels = load();
        if (channels.isEmpty()) {
            System.out.println("전체 조회 결과가 없습니다.");
            return Collections.emptyList();
        }
        return channels;
    }


    // 채널 정보 수정
    @Override
    public Channel update(String channelName, String changeChannel, String changeDescription) {
        Channel channel = find(channelName);
        if (channel == null) {
            System.out.println("채널이 존재하지 않습니다.");
            return null;
        } else {
            channel.updateChannel(changeChannel, changeDescription);
            save(channel);
            return channel;
        }
    }


    // 채널 삭제
    @Override
    public void delete(String channelName) {
        Channel channel = find(channelName);
        try {
            if (channel != null && Files.exists(directory.resolve(channel.getId() + ".ser"))) {
                Files.delete(directory.resolve(directory.resolve(channel.getId() + ".ser")));
            } else {
                System.out.println("메시지가 존재하지 않습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

