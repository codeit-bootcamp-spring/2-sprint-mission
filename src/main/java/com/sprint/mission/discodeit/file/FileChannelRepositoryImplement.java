package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.DataStorageException;
import com.sprint.mission.discodeit.service.ChannelRepository;
import jakarta.annotation.PreDestroy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository("fileChannelRepositoryImplement")
public class FileChannelRepositoryImplement implements ChannelRepository {

  private final String dataDir;
  private final String channelDataFile;
  private final Logger logger = LoggerFactory.getLogger(FileChannelRepositoryImplement.class);
  private final ConcurrentHashMap<UUID, Channel> channelRepository;

  public FileChannelRepositoryImplement() {
    this.dataDir = "./data";
    this.channelDataFile = "channels.dat";
    channelRepository = loadData();
  }

  public FileChannelRepositoryImplement(String dataDir) {
    this.dataDir = dataDir;
    this.channelDataFile = "channels.dat";
    channelRepository = loadData();
  }

  @SuppressWarnings("unchecked")
  private ConcurrentHashMap<UUID, Channel> loadData() {
    File dir = new File(dataDir);
    if (!dir.exists()) {
      dir.mkdirs();
    }

    File file = new File(dir, channelDataFile);
    logger.info("채널 데이터 로드 경로: {}", file.getAbsolutePath());

    if (file.exists()) {
      try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
        return (ConcurrentHashMap<UUID, Channel>) in.readObject();
      } catch (IOException | ClassNotFoundException e) {
        throw new DataStorageException("데이터 저장 중 오류 발생");
      }
    }

    return new ConcurrentHashMap<>();
  }

  @Override
  public synchronized void saveData() {
    File dir = new File(dataDir);
    if (!dir.exists()) {
      dir.mkdirs();
    }

    File file = new File(dir, channelDataFile);
    logger.info("채널 데이터 저장 경로: " + file.getAbsolutePath());

    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
      out.writeObject(channelRepository);
    } catch (IOException e) {
      throw new DataStorageException("채널 데이터 저장 실패");
    }
  }

  @Override
  public void register(Channel channel) {
    if (channel == null || channel.getChannelId() == null) {
      throw new IllegalArgumentException(" 저장 하려는 객체가 null 수 없습니다 ");
    }
    channelRepository.put(channel.getChannelId(), channel);
    saveData();
    logger.debug("채널 등록에 성공 하였습니다.");
  }

  @Override
  public Set<UUID> allChannelIdList() {
    return new HashSet<>(channelRepository.keySet());
  }

  @Override
  public Optional<Channel> findById(UUID channelId) {
    return Optional.ofNullable(channelRepository.get(channelId));
  }

  @Override
  public Optional<String> findChannelNameById(UUID channelId) {
    return findById(channelId).map(Channel::getChannelName);
  }

  @Override
  public Optional<Channel> findByName(String channelName) {
    return channelRepository.values().stream()
        .filter(channel -> channel.getChannelName() != null && channel.getChannelName()
            .equals(channelName))
        .findFirst();
  }

  @Override
  public boolean deleteChannel(UUID channelId) {
    if (channelId == null) {
      return false;
    }
    boolean removed = channelRepository.remove(channelId) != null;
    if (removed) {
      saveData();
      logger.debug("채널 삭제에 성공하였습니다.");
    }
    return removed;
  }

  @Override
  public boolean updateChannel(Channel channel) {
    if (channel == null || !channelRepository.containsKey(channel.getChannelId())) {
      return false;
    }
    channelRepository.put(channel.getChannelId(), channel);
    return true;
  }

  @Override
  public List<Channel> findAllPublicChannels() {
    return channelRepository.values().stream()
        .filter(Channel::isPublic)
        .collect(Collectors.toList());
  }

  @Override
  public List<Channel> findPrivateChannels(UUID userId) {
    Collection<Channel> allChannels = this.channelRepository.values();
    return allChannels.stream()
        .filter(Channel::isPrivate)
        .filter(channel ->
            channel.getOwnerId().equals(userId) ||
                (channel.getUserList() != null && channel.getUserList().contains(userId))
        )
        .toList();
  }

  @PreDestroy
  public void saveDataOnShutdown() {
    logger.info("애플리케이션 종료 - 채널 데이터 저장 중");
    saveData();
  }
}