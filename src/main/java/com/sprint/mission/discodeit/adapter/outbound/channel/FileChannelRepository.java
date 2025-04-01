package com.sprint.mission.discodeit.adapter.outbound.channel;

import com.sprint.mission.discodeit.adapter.inbound.channel.dto.UpdateChannelDTO;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.server.entity.Server;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.exception.user.UserListEmptyError;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.SaveFileNotFoundException;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepository;
import com.sprint.mission.discodeit.adapter.outbound.FileRepositoryImpl;
import com.sprint.mission.discodeit.util.CommonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileChannelRepository implements ChannelRepository {

  private final FileRepositoryImpl<Map<UUID, List<Channel>>> fileRepository;
  private final Path path = Paths.get(System.getProperty("user.dir"), "data", "ChannelList.ser");

  private Map<UUID, List<Channel>> channelList = new ConcurrentHashMap<>();

  public FileChannelRepository() {
    this.fileRepository = new FileRepositoryImpl<>(path);
    try {
      this.channelList = fileRepository.load();
    } catch (SaveFileNotFoundException e) {
      System.out.println("FileChannelRepository init");
    }
  }

  @Override
  public void reset() {
    fileRepository.init();
    try {
      Files.deleteIfExists(path);
      channelList = new ConcurrentHashMap<>();
    } catch (IOException e) {
      System.out.println("리스트 초기화 실패");
    }
  }

  @Override
  public Channel save(Server server, Channel channel) {
    List<Channel> channels = channelList.getOrDefault(server.getServerId(), new ArrayList<>());
    channels.add(channel);
    channelList.put(server.getServerId(), channels);

    fileRepository.save(channelList);
    return channel;
  }

  @Override
  public User join(Channel channel, User user) {
    List<User> list = channel.getUserList();
    list.add(user);

    return user;
  }

  @Override
  public User quit(Channel channel, User user) {
    List<User> list = channel.getUserList();
    if (list.isEmpty()) {
      throw new UserListEmptyError("채널 내 유저 리스트가 비어있습니다.");
    }
    list.remove(user);

    fileRepository.save(channelList);
    return user;
  }


  @Override
  public Channel find(UUID channelId) {
    List<Channel> list = channelList.values().stream().flatMap(List::stream).toList();
    return CommonUtils.findById(list, channelId, Channel::getChannelId)
        .orElseThrow(() -> new ChannelNotFoundException("채널을 찾을 수 없습니다."));
  }

  @Override
  public List<Channel> findAll() {
    return channelList.values().stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  @Override
  public List<Channel> findAllByServerId(UUID serverId) {
    List<Channel> channels = channelList.get(serverId);
    return channels;
  }

  @Override
  public List<Channel> findAllByChannelId(UUID channelId) {
    List<Channel> channels = channelList.values().stream()
        .flatMap(List::stream)
        .filter(channel -> channel.getChannelId().equals(channelId)).toList();
    return channels;
  }

  @Override
  public void remove(UUID channelId) {
    Channel channel = find(channelId);
    UUID serverId = channel.getServerId();
    List<Channel> list = channelList.get(serverId);
    list.remove(channel);
    fileRepository.save(channelList);
  }

  @Override
  public boolean existId(UUID id) {
    return channelList.keySet().stream().anyMatch(u -> u.equals(id));
  }

  @Override
  public boolean existName(String name) {
    return channelList.values().stream().flatMap(List::stream)
        .anyMatch(c -> c.getName().equalsIgnoreCase(name));
  }

}
