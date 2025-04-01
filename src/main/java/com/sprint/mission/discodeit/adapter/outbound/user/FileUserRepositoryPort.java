package com.sprint.mission.discodeit.adapter.outbound.user;

import com.sprint.mission.discodeit.adapter.outbound.FileRepositoryImpl;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.exception.SaveFileNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserListEmptyError;
import com.sprint.mission.discodeit.exception.user.UserNotFoundError;
import com.sprint.mission.discodeit.util.CommonUtils;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserRepositoryPort implements UserRepositoryPort {

  private final FileRepositoryImpl<List<User>> fileRepository;
  private List<User> userList = new ArrayList<>();

  private final Path path = Paths.get(System.getProperty("user.dir"), "data", "UserList.ser");


  public FileUserRepositoryPort() {
    this.fileRepository = new FileRepositoryImpl<>(path);
    try {
      this.userList = fileRepository.load();
    } catch (SaveFileNotFoundException e) {
      System.out.println("FileUserRepository init");
    }
  }

  @Override
  public User save(User user) {

    userList.add(user);
    fileRepository.save(userList);
    return user;
  }

  @Override
  public User findById(UUID userId) {
    User user = CommonUtils.findById(userList, userId, User::getId)
        .orElseThrow(() -> new UserNotFoundError("유저를 찾을 수 없습니다."));

    return user;
  }

  @Override
  public List<User> findAll() {
    if (userList.isEmpty()) {
      throw new UserListEmptyError("유저 리스트가 비어있습니다.");
    }
    return userList;
  }

  @Override
  public UUID remove(User user) {
    if (userList.isEmpty()) {
      throw new UserListEmptyError("유저 리스트가 비어있습니다.");
    }
    userList.remove(user);
    fileRepository.save(userList);
    return user.getId();
  }


  @Override
  public boolean existId(UUID id) {
    return userList.stream().anyMatch(u -> u.getId().equals(id));
  }

  @Override
  public boolean existName(String name) {
    return userList.stream().anyMatch(u -> u.getName().equalsIgnoreCase(name));
  }

  @Override
  public boolean existEmail(String email) {
    return userList.stream().anyMatch(u -> u.getEmail().equals(email));
  }

}

