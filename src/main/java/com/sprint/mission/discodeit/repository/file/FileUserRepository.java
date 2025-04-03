package com.sprint.mission.discodeit.repository.file;

import static com.sprint.mission.discodeit.util.FileUtils.loadAndSave;
import static com.sprint.mission.discodeit.util.FileUtils.loadObjectsFromFile;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {

  private final Path userPath;

  public FileUserRepository(
      @Value("${discodeit.repository.file-directory.user-path}") Path userPath) {
    this.userPath = userPath;
  }

  @Override
  public User save(User requestUser) {
    loadAndSave(userPath, (Map<UUID, User> users) ->
        users.put(requestUser.getId(), requestUser)
    );

    return requestUser;
  }

  @Override
  public Optional<User> findByUserId(UUID id) {
    Map<UUID, User> users = loadObjectsFromFile(userPath);

    return Optional.ofNullable(users.get(id));
  }

  @Override
  public Optional<User> findByName(String name) {
    Map<UUID, User> users = loadObjectsFromFile(userPath);

    return users.values()
        .stream()
        .filter(user -> user.isSameName(name))
        .findFirst();
  }

  @Override
  public Optional<User> findByEmail(String email) {
    Map<UUID, User> users = loadObjectsFromFile(userPath);

    return users.values()
        .stream()
        .filter(user -> user.isSameEmail(email))
        .findFirst();
  }

  @Override
  public List<User> findAll() {
    Map<UUID, User> users = loadObjectsFromFile(userPath);

    return users.values()
        .stream()
        .toList();
  }

  @Override
  public void delete(UUID id) {
    loadAndSave(userPath, (Map<UUID, User> users) ->
        users.remove(id)
    );
  }
}
