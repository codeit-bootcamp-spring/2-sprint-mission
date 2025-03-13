package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private static volatile FileChannelRepository instance;

    private final Path path;

    private Map<UUID, Channel> channels;
    private Map<UUID, Set<UUID>> memberIdsByChannelId;

    //레코드 통째로 직렬화
    private record ChannelData(
            Map<UUID, Channel> channels,
            Map<UUID, Set<UUID>> memberIdsByChannelId
    ) implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    private FileChannelRepository(Path path) {
        this.path = path;
        channels = new HashMap<>();
        memberIdsByChannelId = new HashMap<>();
        init(path.getParent());
        loadChannelData();
    }

    public static FileChannelRepository getInstance(Path path) {
        if (instance == null) {
            synchronized (FileChannelRepository.class) {
                if (instance == null) {
                    instance = new FileChannelRepository(path);
                }
            }
        }
        return instance;
    }


    @Override
    public Channel save(Channel channel) {
        channels.put(channel.getId(), channel);
        memberIdsByChannelId.putIfAbsent(channel.getId(), new HashSet<>());
        Set<UUID> memberIds = memberIdsByChannelId.get(channel.getId());
        memberIds.add(channel.getOwnerId());
        saveChannelData();
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        return channels.get(channelId);
    }

    @Override
    public Set<UUID> findMemberIds(UUID channelId) {
        return memberIdsByChannelId.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void delete(UUID channelId) {
        channels.remove(channelId);
        memberIdsByChannelId.remove(channelId);
        saveChannelData();
    }

    @Override
    public void addMember(UUID channelId, UUID userId) {
        memberIdsByChannelId.get(channelId).add(userId);
        saveChannelData();
    }

    @Override
    public void removeMember(UUID channelId, UUID userId) {
        memberIdsByChannelId.get(channelId).remove(userId);
        saveChannelData();
    }

    @Override
    public boolean exists(UUID channelId) {
        return channels.containsKey(channelId) && memberIdsByChannelId.containsKey(channelId);
    }

    private void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 안 됨");
            }
        }
    }

    private void loadChannelData() {
        if (Files.exists(path)) {
            try (InputStream is = Files.newInputStream(path);
                 ObjectInputStream ois = new ObjectInputStream(is)) {
                Object channelDataObject = ois.readObject();

                if (channelDataObject instanceof ChannelData channelData) {
                    channels = channelData.channels();
                    memberIdsByChannelId = channelData.memberIdsByChannelId();
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("체널 데이터 로드 실패");
            }
        }
    }

    private void saveChannelData() {
        try (OutputStream os = Files.newOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(new ChannelData(channels, memberIdsByChannelId));
        } catch (IOException e) {
            throw new RuntimeException("체널 데이터 저장 실패");
        }
    }
}





