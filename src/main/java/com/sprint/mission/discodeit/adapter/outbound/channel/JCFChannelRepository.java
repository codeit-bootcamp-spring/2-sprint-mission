package com.sprint.mission.discodeit.adapter.outbound.channel;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepository;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserListEmptyError;
import com.sprint.mission.discodeit.util.CommonUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFChannelRepository implements ChannelRepository {

  private Map<UUID, List<Channel>> channelList = new ConcurrentHashMap<>();

  @Override
  public Channel save(Channel channel) {
    List<Channel> channels = channelList.getOrDefault(channel.getChannelId(), new ArrayList<>());

    channels.add(channel);

    channelList.put(channel.getChannelId(), channels);

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
  public List<Channel> findAllByChannelId(UUID channelId) {
    List<Channel> channels = channelList.values().stream()
        .flatMap(List::stream)
        .filter(channel -> channel.getChannelId().equals(channelId)).toList();
    return channels;
  }

  @Override
  public void remove(UUID channelId) {
    Channel channel = find(channelId);
    List<Channel> list = channelList.get(channelId);
    list.remove(channel);
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
