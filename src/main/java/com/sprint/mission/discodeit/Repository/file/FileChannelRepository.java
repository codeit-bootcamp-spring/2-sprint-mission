package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.DTO.Channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.Exception.*;
import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private Map<UUID, List<Channel>> channelList = new ConcurrentHashMap<>();
    private final Path channelPath = Paths.get(System.getProperty("user.dir"), "data", "ChannelList.ser");

    public FileChannelRepository() {
        loadChannelList();
    }

    // 채널 리스트를 저장할 디렉토리가 있는지 확인
    private void init() {
        Path directory = channelPath.getParent();
        if (Files.exists(directory) == false) {
            try {
                Files.createDirectories(directory);
                System.out.println("디렉토리 생성 완료: " + directory);
            } catch (IOException e) {
                System.out.println("디렉토리 생성 실패");
                throw new RuntimeException(e);
            }
        }
    }

    private void loadChannelList() {
        if (Files.exists(channelPath) == true) {
            try (FileInputStream fis = new FileInputStream(channelPath.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                Map<UUID, List<Channel>> list = (Map<UUID, List<Channel>>) ois.readObject();
                channelList = list;

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("채널 리스트 로드 실패");
                throw new RuntimeException(e);
            }
        }
    }


    private void saveChannelList() {
        init();
        try (FileOutputStream fos = new FileOutputStream(channelPath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(channelList);

        } catch (IOException e) {
            System.out.println("채널 리스트 저장 실패");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reset() {
        init();
        try {
            Files.deleteIfExists(channelPath);
            channelList = new ConcurrentHashMap<>();
        } catch (IOException e) {
            System.out.println("리스트 초기화 실패");
        }
    }

    @Override
    public UUID join(Channel channel, User user) {
        List<User> list = channel.getUserList();
        list.add(user);

        saveChannelList();
        return user.getId();
    }

    @Override
    public UUID quit(Channel channel, User user) {
        List<User> list = channel.getUserList();
        if (list.isEmpty()) {
            throw CommonExceptions.EMPTY_USER_LIST;
        }
        list.remove(user);

        saveChannelList();
        return user.getId();
    }

    @Override
    public UUID save(Server server, Channel channel) {
        List<Channel> channels = channelList.getOrDefault(server.getServerId(), new ArrayList<>());
        channels.add(channel);
        channelList.put(server.getServerId(), channels);

        saveChannelList();
        return channel.getChannelId();
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channel = channelList.values().stream().flatMap(List::stream)
                .filter(c -> c.getChannelId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> CommonExceptions.CHANNEL_NOF_FOUND);
        return channel;
    }

    @Override
    public List<Channel> findAllByServerId(UUID serverId) {
        if (channelList.isEmpty()) {
            throw CommonExceptions.EMPTY_CHANNEL_LIST;
        }
        List<Channel> channels = channelList.get(serverId);

        if (channels.isEmpty()) {
            throw CommonExceptions.EMPTY_CHANNEL_LIST;
        }
        return channels;
    }

    @Override
    public UUID update(Channel targetChannel, ChannelUpdateDTO channelUpdateDTO) {
        if (channelUpdateDTO.replaceChannelId() != null) {
            targetChannel.setChannelId(channelUpdateDTO.replaceChannelId());
        }
        if (channelUpdateDTO.replaceName() != null) {
            targetChannel.setName(channelUpdateDTO.replaceName());
        }
        if (channelUpdateDTO.replaceType() != null) {
            targetChannel.setType(channelUpdateDTO.replaceType());
        }
        saveChannelList();
        return targetChannel.getChannelId();
    }

    @Override
    public void remove(Server server, Channel channel) {
        List<Channel> list = findAllByServerId(server.getServerId());
        list.remove(channel);
        saveChannelList();
    }
}
