package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusRepository;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository("fileUserStatusRepositoryImpl")
public class FileUserStatusRepositoryImpl implements UserStatusRepository {

  private final String dataDir;
  private final String userStatusDataFile;
  private final Logger logger = Logger.getLogger(FileUserStatusRepositoryImpl.class.getName());
  private final ConcurrentHashMap<UUID, UserStatus> userStatuses;

  public FileUserStatusRepositoryImpl(String dataDir) {
    this.dataDir = dataDir;
    this.userStatusDataFile = "user_status.dat";
    userStatuses = loadData();
  }

  @SuppressWarnings("unchecked")
  private ConcurrentHashMap<UUID, UserStatus> loadData() {
    File dir = new File(dataDir);
    if (!dir.exists()) {
      dir.mkdirs();
    }

    File file = new File(dir, userStatusDataFile);
    System.out.println("사용자 상태 데이터 로드 경로: " + file.getAbsolutePath());

    if (file.exists()) {
      try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
        return (ConcurrentHashMap<UUID, UserStatus>) in.readObject();
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

    File file = new File(dir, userStatusDataFile);
    System.out.println("사용자 상태 데이터 저장 경로: " + file.getAbsolutePath());

    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
      out.writeObject(userStatuses);
    } catch (IOException e) {
      throw new RuntimeException("사용자 상태 데이터 저장 실패", e);
    }
  }

  @Override
  public void register(UserStatus userStatus) {
    userStatuses.put(userStatus.getId(), userStatus);
    saveData();
  }

  @Override
  public Optional<UserStatus> findById(UUID id) {
    return Optional.ofNullable(userStatuses.get(id));
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return userStatuses.values().stream()
        .filter(userStatus -> userStatus.getUserId().equals(userId))
        .findFirst();
  }

  @Override
  public List<UserStatus> findAll() {
    return new ArrayList<>(userStatuses.values());
  }

  @Override
  public boolean update(UserStatus userStatus) {
    if (userStatuses.containsKey(userStatus.getId())) {
      userStatuses.put(userStatus.getId(), userStatus);
      return true;
    }
    return false;
  }

  @Override
  public boolean delete(UUID id) {
    boolean result = userStatuses.remove(id) != null;
    if (result) {
      saveData();
    }
    return result;
  }

  // 애플리케이션 종료 시 데이터 저장 보장
  @PreDestroy
  public void saveDataOnShutdown() {
    logger.info("애플리케이션 종료 - 사용자 상태 데이터 저장 중");
    saveData();
  }
} 