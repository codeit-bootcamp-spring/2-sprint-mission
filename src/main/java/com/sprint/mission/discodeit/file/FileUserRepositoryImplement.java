package com.sprint.mission.discodeit.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DataStorageException;
import com.sprint.mission.discodeit.service.UserRepository;
import jakarta.annotation.PreDestroy;

import org.springframework.stereotype.Repository;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import java.util.*;

@Repository("fileUserRepositoryImplement")
public class FileUserRepositoryImplement implements UserRepository {

  private final String dataDir;
  private final String userDataFile;
  private final ConcurrentHashMap<UUID, User> userRepository;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public FileUserRepositoryImplement() {
    this.dataDir = "./data";  // 기본 경로를 프로젝트 루트 아래 data 폴더로 설정
    this.userDataFile = "users.dat";
    userRepository = loadData();
    System.out.println("사용자 저장소 초기화 - 파일 경로: " + new File(dataDir, userDataFile).getAbsolutePath());
  }

  public FileUserRepositoryImplement(String dataDir) {
    this.dataDir = dataDir;
    this.userDataFile = "users.dat";
    userRepository = loadData();
  }

  private ConcurrentHashMap<UUID, User> loadData() {
    File dir = new File(dataDir);
    if (!dir.exists()) {
      dir.mkdirs();
    }

    File file = new File(dir, userDataFile);

    if (file.exists()) {
      try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {

        return (ConcurrentHashMap<UUID, User>) in.readObject();
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException("오류 발생", e);
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
    File file = new File(dir, userDataFile);

    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
      out.writeObject(this.userRepository);
      logger.info("사용자 데이터 저장 완료: {}", file.getAbsolutePath());
    } catch (IOException e) {
      throw new DataStorageException("파일 저장 오류", e);
    }
  }

  @Override
  public Optional<User> findByUser(UUID userId) {
    return Optional.ofNullable(userRepository.get(userId));
  }

  @Override
  public void register(User user) {
    if (user == null || user.getId() == null) {
      throw new IllegalArgumentException("등록할 사용자 또는 사용자 ID는 null 수 없습니다.");
    }
    userRepository.put(user.getId(), user);
    saveData();
    logger.debug("사용자 등록 완료: {}", user.getId());
  }

  @Override
  public boolean deleteUser(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("삭제할 사용자 ID는 null 수 없습니다.");
    }
    boolean removed = userRepository.remove(userId) != null;
    if (removed) {
      saveData();
      logger.debug("사용자 삭제 완료, id: {}", userId);
    }
    return removed;
  }

  @Override
  public Set<UUID> findAllUsers() {
    return new HashSet<>(userRepository.keySet());
  }

  @Override
  public boolean updateUser(User user) {
    if (user == null || !userRepository.containsKey(user.getId())) {
      return false;
    }
    userRepository.put(user.getId(), user);
    return true;
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userRepository.values().stream()
        .filter(user -> user.getEmail() != null && user.getEmail().equals(email))
        .findFirst();
  }

  // 애플리케이션 종료 시 데이터 저장 보장
  @PreDestroy
  public void saveDataOnShutdown() {
    System.out.println("애플리케이션 종료 - 사용자 데이터 저장 중");
    saveData();
  }
}
