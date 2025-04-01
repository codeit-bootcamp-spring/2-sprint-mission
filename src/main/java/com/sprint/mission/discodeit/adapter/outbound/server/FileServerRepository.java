package com.sprint.mission.discodeit.adapter.outbound.server;

import com.sprint.mission.discodeit.adapter.inbound.server.dto.UpdateServerRequestDTO;
import com.sprint.mission.discodeit.core.server.entity.Server;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.exception.server.EmptyServerListException;
import com.sprint.mission.discodeit.exception.user.UserListEmptyError;
import com.sprint.mission.discodeit.exception.SaveFileNotFoundException;
import com.sprint.mission.discodeit.exception.server.ServerNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundError;
import com.sprint.mission.discodeit.adapter.outbound.FileRepositoryImpl;
import com.sprint.mission.discodeit.core.server.port.ServerRepository;
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

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileServerRepository implements ServerRepository {

  private final FileRepositoryImpl<Map<UUID, List<Server>>> fileRepository;
  private Map<UUID, List<Server>> serverList = new ConcurrentHashMap<>();
  private final Path path = Paths.get(System.getProperty("user.dir"), "data", "ServerList.ser");

  public FileServerRepository() {
    this.fileRepository = new FileRepositoryImpl<>(path);
    try {
      this.serverList = fileRepository.load();
    } catch (SaveFileNotFoundException e) {
      System.out.println("FileServerRepository init");
    }
  }

  @Override
  public void reset() {
    fileRepository.init();
    try {
      Files.deleteIfExists(path);

      serverList = new ConcurrentHashMap<>();

    } catch (IOException e) {
      System.out.println("리스트 초기화 실패");
    }
  }

  @Override
  public Server save(Server server, User user) {
    List<Server> servers = serverList.getOrDefault(user.getId(), new ArrayList<>());
    servers.add(server);
    serverList.put(user.getId(), servers);

    fileRepository.save(serverList);
    return server;
  }

  @Override
  public User join(Server server, User user) {
    List<User> users = server.getUserList();
    users.add(user);
    fileRepository.save(serverList);
    return user;
  }


  @Override
  public User quit(Server server, User user) {
    List<User> users = server.getUserList();
    if (users.isEmpty()) {
      throw new UserListEmptyError("서버 내 유저 리스트가 비어있습니다.");
    }
    users.remove(user);
    fileRepository.save(serverList);
    return user;
  }

  @Override
  public Server findById(UUID serverId) {
    List<Server> list = serverList.values().stream().flatMap(List::stream).toList();
    return CommonUtils.findById(list, serverId, Server::getServerId)
        .orElseThrow(() -> new ServerNotFoundException("서버를 찾을 수 없습니다."));
  }

  @Override
  public Server findByOwnerId(UUID userId) {
    List<Server> servers = findAllByUserId(userId);
    return servers.stream().filter(s -> s.getUserId().equals(userId))
        .findFirst()
        .orElseThrow(() -> new UserNotFoundError("서버장을 찾을 수 없습니다."));

  }

  @Override
  public List<Server> findAllByUserId(UUID userId) {
    if (serverList.isEmpty()) {
      throw new EmptyServerListException("Repository 에 저장된 서버 리스트가 없습니다.");
    }

    List<Server> list = serverList.get(userId);

    if (list.isEmpty()) {
      throw new EmptyServerListException("해당 서버에 저장된 채널 리스트가 없습니다.");
    }
    return list;
  }

  @Override
  public void remove(UUID serverId) {
    Server server = findById(serverId);
    UUID ownerId = server.getUserId();
    List<Server> list = findAllByUserId(ownerId);
    list.remove(server);

    fileRepository.save(serverList);
  }
}
