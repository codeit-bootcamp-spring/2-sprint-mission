package com.sprint.mission.discodeit.adapter.outbound.channel;

import com.sprint.mission.discodeit.adapter.outbound.FileRepositoryImpl;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepository;
import com.sprint.mission.discodeit.exception.SaveFileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileChannelRepository implements ChannelRepository {

  private final FileRepositoryImpl<Map<UUID, Channel>> fileRepository;
  private final Path path = Paths.get(System.getProperty("user.dir"), "data", "ChannelList.ser");

  private Map<UUID, Channel> channelList = new ConcurrentHashMap<>();

  public FileChannelRepository() {
    this.fileRepository = new FileRepositoryImpl<>(path);
    try {
      this.channelList = fileRepository.load();
    } catch (SaveFileNotFoundException e) {
      System.out.println("FileChannelRepository init");
    }
  }

  @Override
  public Channel save(Channel channel) {
    channelList.put(channel.getId(), channel);
    fileRepository.save(channelList);
    return channel;
  }


  @Override
  public Optional<Channel> findByChannelId(UUID channelId) {
    return Optional.ofNullable(channelList.get(channelId));
  }

  @Override
  public List<Channel> findAll() {
    return channelList.values().stream().toList();
  }

  @Override
  public List<Channel> findAllByChannelId(UUID channelId) {
    return channelList.values().stream().filter(channel -> channel.getId().equals(channelId))
        .toList();
  }

  @Override
  public void remove(UUID channelId) {
    channelList.remove(channelId);
    fileRepository.save(channelList);
  }

//  @Override
//  public User join(Channel channel, User user) {
//    List<User> list = channel.getUserList();
//    list.add(user);
//
//    return user;
//  }
//
//  @Override
//  public User quit(Channel channel, User user) {
//    List<User> list = channel.getUserList();
//    if (list.isEmpty()) {
//      throw new UserListEmptyError("채널 내 유저 리스트가 비어있습니다.");
//    }
//    list.remove(user);
//
//    fileRepository.save(channelList);
//    return user;
//  }
}
