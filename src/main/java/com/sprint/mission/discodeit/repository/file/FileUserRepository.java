package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Primary
@Repository
public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "users.ser";

    @Override
    public void save(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("ID가 공백입니다.");
        }
        List<User> users = readFromFile();
        users.removeIf(u -> u.getId().equals(user.getId()));
        users.add(user);
        writeToFile(users);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return readFromFile().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public void delete(UUID id) {
        List<User> users = readFromFile();
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (removed) {
            writeToFile(users);
        }
    }
    @Override
    public boolean existsByUsername(String username) {
        return readFromFile().stream()
                .anyMatch(user -> user.getUserName().equals(username));
    }

    @Override
    public boolean existsByEmail(String email) {
        return readFromFile().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public List<User> findAll() {
        return readFromFile();
    }

    private void writeToFile(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            List<Map<String, Object>> userList = users.stream()
                    .map(user -> {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("id", user.getId());
                        userMap.put("username", user.getUserName());
                        userMap.put("createdAt", user.getCreateAt().toEpochMilli());
                        userMap.put("lastActiveAt", user.getStatus().getLastActiveAt().toEpochMilli()); // lastActiveAt 추가
                        return userMap;
                    })
                    .collect(Collectors.toList());

            oos.writeObject(userList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<User> readFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            List<Map<String, Object>> userList = (List<Map<String, Object>>) ois.readObject();

            return userList.stream()
                    .map(userMap -> {
                        Long idLong = (Long) userMap.get("id");
                        UUID id = new UUID(idLong, idLong);

                        String username = (String) userMap.get("username");
                        Long createdAtLong = (Long) userMap.get("createdAt");
                        Instant createdAt = Instant.ofEpochMilli(createdAtLong);

                        Long lastActiveAtLong = (Long) userMap.get("lastActiveAt");
                        Instant lastActiveAt = Instant.ofEpochMilli(lastActiveAtLong);

                        UserStatus status = new UserStatus(lastActiveAt);

                        return new User(id, username, "", createdAt, createdAt, status);
                    })
                    .collect(Collectors.toList());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
