package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageRepository;
import jakarta.annotation.PreDestroy;
import java.time.ZonedDateTime;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository("fileMessageRepositoryImplement")
public class FileMessageRepositoryImplement implements MessageRepository {

  private final String dataDir;
  private final String messageDataFile;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final Map<UUID, Message> messageRepository;

  public FileMessageRepositoryImplement() {
    this.dataDir = "./data";
    this.messageDataFile = "messages.dat";
    messageRepository = loadData();
  }

  public FileMessageRepositoryImplement(String dataDir) {
    this.dataDir = dataDir;
    this.messageDataFile = "messages.dat";
    messageRepository = loadData();
  }

  @SuppressWarnings("unchecked")
  private Map<UUID, Message> loadData() {
    File dir = new File(dataDir);
    if (!dir.exists()) {
      dir.mkdirs();
    }

    File file = new File(dir, messageDataFile);
    System.out.println("메시지 데이터 로드 경로: " + file.getAbsolutePath());

    if (file.exists()) {
      try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
        return (Map<UUID, Message>) in.readObject();
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
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

    File file = new File(dir, messageDataFile);
    logger.debug("메시지 데이터 저장 경로: {}", file.getAbsolutePath());

    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
      out.writeObject(messageRepository);
    } catch (IOException e) {
      logger.error("메시지 데이터 저장 오류: {}", e.getMessage());
      throw new RuntimeException("메시지 데이터 저장 실패", e);
    }
  }

  @Override
  public void register(Message message) {
    if (message == null || message.getId() == null) {
      throw new IllegalArgumentException("등록할 메시지 또는 메시지 ID는 null 수 없습니다.");
    }
    messageRepository.put(message.getId(), message);
    logger.debug("메시지 등록 완료: ID={}", message.getId());
    saveData();

  }

  @Override
  public Optional<Message> findById(UUID messageId) {
    return Optional.ofNullable(messageRepository.get(messageId));
  }

  @Override
  public List<Message> findAll() {
    return new ArrayList<>(messageRepository.values());
  }

  @Override
  public boolean deleteMessage(UUID messageId) {
    if (messageId == null) {
      return false;
    }
    boolean removed = messageRepository.remove(messageId) != null;
    if (removed) {
      saveData();
      logger.debug("메시지 삭제 완료: ID={}", messageId);
    }
    return removed;
  }

  @Override
  public List<ZonedDateTime> findTimestampsByChannelIdSorted(UUID channelId) {
    List<Message> channelMessages = findAllByChannelId(channelId);

    if (channelMessages == null || channelMessages.isEmpty()) {
      return Collections.emptyList();
    }
    return channelMessages.stream()
        .map(Message::getCreatedAt)
        .sorted()
        .collect(Collectors.toList());
  }

  @Override
  public Optional<ZonedDateTime> findLatestTimestampByChannelId(UUID channelId) {

    List<Message> channelMessages = findAllByChannelId(channelId);

    if (channelMessages == null || channelMessages.isEmpty()) {
      return Optional.empty();
    }

    return channelMessages.stream()
        .map(Message::getCreatedAt)
        .max(ZonedDateTime::compareTo);
  }

  //업데이트는 맵에만
  @Override
  public boolean updateMessage(Message message) {
    if (message == null || !messageRepository.containsKey(message.getId())) {
      return false;
    }
    messageRepository.put(message.getId(), message);
    return true;
  }

  public List<Message> findAllByAuthorId(UUID authorId) {
    return messageRepository.values().stream()
        .filter(message -> authorId.equals(message.getAuthorId()))
        .collect(Collectors.toList());
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.values().stream()
        .filter(
            message -> message.getChannelId() != null && message.getChannelId().equals(channelId))
        .collect(Collectors.toList());
  }

  // 애플리케이션 종료 시 데이터 저장 보장
  @PreDestroy
  public void saveDataOnShutdown() {
    logger.info("애플리케이션 종료 - 메시지 데이터 저장 중");
    saveData();
  }
} 