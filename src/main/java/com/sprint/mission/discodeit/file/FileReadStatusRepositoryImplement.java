package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.DataStorageException;
import com.sprint.mission.discodeit.service.ReadStatusRepository;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PreDestroy;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository("fileReadStatusRepositoryImplement")
public class FileReadStatusRepositoryImplement implements ReadStatusRepository {

  private final ConcurrentHashMap<UUID, ReadStatus> readStatusRepository;
  private final String dataDir;
  private final String readStatusDataFile;
  private final Logger logger = LoggerFactory.getLogger(FileReadStatusRepositoryImplement.class);

  public FileReadStatusRepositoryImplement() {
    this.dataDir = "./data";
    this.readStatusDataFile = "read_status.dat";
    readStatusRepository = loadData();
  }

  public FileReadStatusRepositoryImplement(String dataDir) {
    this.dataDir = dataDir;
    this.readStatusDataFile = "read_status.dat";
    readStatusRepository = loadData();
  }

  @SuppressWarnings("unchecked")
  private ConcurrentHashMap<UUID, ReadStatus> loadData() {
    File dir = new File(dataDir);
    if (!dir.exists()) {
      dir.mkdirs();
    }

    File file = new File(dir, readStatusDataFile);
    logger.info("읽기 상태 데이터 로드 경로: {}", file.getAbsolutePath());
    if (file.exists()) {
      try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
        return (ConcurrentHashMap<UUID, ReadStatus>) in.readObject();
      } catch (IOException | ClassNotFoundException e) {
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

    File file = new File(dir, readStatusDataFile);
    logger.info("읽기 상태 데이터 저장 경로: {}", file.getAbsolutePath());

    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
      out.writeObject(readStatusRepository);
    } catch (IOException e) {
      throw new DataStorageException("데이터 저장 중 오류 발생");
    }
  }

  @Override
  public void register(ReadStatus readStatus) {
    if (readStatusRepository == null || readStatus.getId() == null) {
      throw new IllegalArgumentException("저장 할 값은 null  수 없습니다.");
    }
    readStatusRepository.put(readStatus.getId(), readStatus);
    saveData();
    logger.debug("유저 상태가 등록되었습니다.");
  }

  @Override
  public Optional<ReadStatus> findById(UUID id) {
    return Optional.ofNullable(readStatusRepository.get(id));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.values().stream()
        .filter(status -> status.getUserId().equals(userId))
        .collect(Collectors.toList());
  }

  @Override
  public List<ReadStatus> findAll() {
    return new ArrayList<>(readStatusRepository.values());
  }

  @Override
  public boolean updateReadStatus(ReadStatus readStatus) {
    if (readStatus == null || !readStatusRepository.containsKey(readStatus.getId())) {
      return false;
    }
    readStatusRepository.put(readStatus.getId(), readStatus);
    return true;
  }

  @Override
  public boolean deleteReadStatus(UUID id) {
    if (id == null) {
      return false;
    }
    boolean removed = readStatusRepository.remove(id) != null;
    if (removed) {
      saveData();
    }
    return removed;
  }

  @Override
  public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
    return readStatusRepository.values().stream()
        .filter(
            status -> status.getUserId().equals(userId) && status.getChannelId().equals(channelId))
        .findFirst();
  }

  public boolean deleteAllByUserId(UUID userId) {
    boolean success = true;
    List<ReadStatus> statusesToDelete = new ArrayList<>();

    for (ReadStatus status : readStatusRepository.values()) {
      if (userId.equals(status.getUserId())) {
        statusesToDelete.add(status);
      }
    }

    // 삭제 실행
    for (ReadStatus status : statusesToDelete) {
      if (!deleteReadStatus(status.getId())) {
        success = false;
      }
    }

    return success;
  }

  @Override
  public boolean deleteAllByChannelId(UUID channelId) {
    boolean success = true;
    List<ReadStatus> statusesToDelete = new ArrayList<>();

    // 먼저 삭제할 항목 수집
    for (ReadStatus status : readStatusRepository.values()) {
      if (channelId.equals(status.getChannelId())) {
        statusesToDelete.add(status);
      }
    }

    // 삭제 실행
    for (ReadStatus status : statusesToDelete) {
      if (!deleteReadStatus(status.getId())) {
        success = false;
      }
    }

    return success;
  }

  @PreDestroy
  public void saveDataOnShutdown() {
    System.out.println("애플리케이션 종료 - 읽기 상태 데이터 저장 중");
    saveData();
  }
} 