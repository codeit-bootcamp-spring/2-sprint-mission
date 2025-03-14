package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.Exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.Exception.EmptyChannelListException;
import com.sprint.mission.discodeit.Exception.EmptyUserListException;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.ServerRepository;
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
public class FileServerRepository implements ServerRepository {
    private Map<UUID, List<User>> channelUsers = new ConcurrentHashMap<>();
    private Map<UUID, List<Channel>> channelList = new ConcurrentHashMap<>();
    private final Path channelUsersPath = Paths.get(System.getProperty("user.dir"), "data", "ChannelUsersList.ser");
    private final Path channelPath = Paths.get(System.getProperty("user.dir"), "data", "ChannelList.ser");

    public FileServerRepository() {
        loadChannelUsers();
        loadChannelList();
    }

    // 채널 리스트를 저장할 디렉토리가 있는지 확인
    private void init() {
        Path directory = channelPath.getParent();
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                System.out.println("디렉토리 생성 완료: " + directory);
            } catch (IOException e) {
                System.out.println("디렉토리 생성 실패");
                throw new RuntimeException(e);
            }
        }
    }

    private void loadChannelUsers() {
        if (Files.exists(channelUsersPath)) {
            try (FileInputStream fis = new FileInputStream(channelUsersPath.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                Map<UUID, List<User>> list = (Map<UUID, List<User>>) ois.readObject();
                channelUsers = list;

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("채널 리스트 로드 실패");
                throw new RuntimeException(e);
            }
        }
    }

    private void loadChannelList() {
        if (Files.exists(channelPath)) {
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

    private void saveChannelUserList() {
        init();

        try (FileOutputStream fos = new FileOutputStream(channelUsersPath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(channelUsers);

        } catch (IOException e) {
            System.out.println("채널 리스트 저장 실패");
            throw new RuntimeException(e);
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
            Files.deleteIfExists(channelPath);
            channelUsers = new ConcurrentHashMap<>();
            channelList = new ConcurrentHashMap<>();
        } catch (IOException e) {
            System.out.println("리스트 초기화 실패");
        }
    }

    @Override
    public UUID saveUser(Channel channel, User user) {
        List<User> users = channelUsers.get(channel.getChannelId());
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
        channelUsers.put(channel.getChannelId(), users);

        saveChannelUserList();

        return user.getId();
    }

    @Override
    public UUID saveChannel(Server server, Channel channel) {
        List<Channel> channels = channelList.get(server.getServerId());
        if (channels == null) {
            channels = new ArrayList<>();
        }
        channels.add(channel);
        channelList.put(server.getServerId(), channels);

        saveChannelList();

        return channel.getChannelId();
    }

    @Override
    public User findUser(Channel channel, User user) {
        List<User> users = findUserListByChannelId(channel.getChannelId());
        User findUser = users.stream().filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("채널 내 해당 유저가 없습니다."));
        return findUser;
    }

    @Override
    public Channel findChannel(Server server, Channel channel) {
        return findChannelByChanelId(server.getServerId(), channel.getChannelId());
    }

    @Override
    public Channel findChannelByChanelId(Server server, UUID channelId) {
        return findChannelByChanelId(server.getServerId(), channelId);
    }

    @Override
    public Channel findChannelByChanelId(UUID serverId, Channel channel) {
        return findChannelByChanelId(serverId, channel.getChannelId());
    }

    @Override
    public Channel findChannelByChanelId(UUID serverId, UUID channelId) {
        List<Channel> channels = findChannelListByServerId(serverId);
        Channel findChannel = channels.stream().filter(c -> c.getChannelId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new ChannelNotFoundException("채널이 존재하지 않습니다."));
        return findChannel;
    }


    @Override
    public List<User> findUserListByChannelId(UUID channelId) {

        List<User> users = channelUsers.get(channelId);
        if (users == null) {
            throw  new EmptyUserListException("유저 리스트가 비어있습니다.");
        }

        return users;
    }

    @Override
    public List<Channel> findChannelListByServerId(UUID serverId) {
        List<Channel> channels = channelList.get(serverId);
        if (channels == null) {
            throw new EmptyChannelListException("채널 리스트가 비어있습니다.");
        }
        return channels;
    }

    @Override
    public UUID updateChannelName(Server server, Channel channel, String replaceName) {
        Channel findChannel = findChannel(server, channel);
        findChannel.setName(replaceName);
        saveChannelList();

        return findChannel.getChannelId();
    }

    @Override
    public UUID removeChannel(Server server, Channel channel) {
        List<Channel> channels = findChannelListByServerId(server.getServerId());
        Channel findChannel = findChannel(server, channel);

        channels.remove(findChannel);
        channelList.put(server.getServerId(), channels);

        saveChannelList();
        return findChannel.getChannelId();
    }

    @Override
    public UUID quitChannel(Channel channel, User user) {
        List<User> users = findUserListByChannelId(channel.getChannelId());
        User findUser = findUser(channel, user);

        users.remove(findUser);
        channelUsers.put(channel.getChannelId(), users);
        saveChannelUserList();
        return findUser.getId();
    }
}
