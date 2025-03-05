package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {

    private final String FILE_PATH = "src/main/resources/users.dat";
    private Map<UUID, User> users = new HashMap<>();
    private static FileUserService INSTANCE;

    private FileUserService() {
        loadUser();
    }

    public static synchronized FileUserService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileUserService();
        }
        return INSTANCE;
    }

    private void saveUser() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException("유저 저장 중 오류 발생", e);
        }
    }


    public void loadUser() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            users = (Map<UUID, User>) ois.readObject();
        } catch (EOFException e) {
            System.out.println("⚠ users.dat 파일이 비어 있습니다. 빈 데이터로 유지합니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("유저 로드 중 오류 발생", e);
        }
    }

    public void updataUserData() {
        saveUser();
    }

    @Override
    public User createUser(String username) {
        User user = new User(username);
        users.put(user.getId(), user);
        saveUser();
        return user;
    }

    @Override
    public User getUserById(UUID userId) {
        validateUserExists(userId);
        return users.get(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public String getUserNameById(UUID userId) {
        validateUserExists(userId);
        return users.get(userId).getUsername();
    }

    @Override
    public void updateUsername(UUID userID, String newUsername) {
        User user = getUserById(userID);
        String oldUserName = user.getUsername();
        user.updateUsername(newUsername);
        saveUser();
    }

    @Override
    public void addChannel(UUID userID, UUID channelId) {
        User user = getUserById(userID);

        if (user.isJoinedChannel(channelId)) {
            throw new IllegalArgumentException("이미 가입되어 있는 채널입니다. ");
        }

        user.addJoinedChannel(channelId);
        saveUser();
    }

    @Override
    public void deleteUser(UUID userID) {
        User user = getUserById(userID);
        users.remove(user.getId());
        saveUser();
    }

    @Override
    public void deleteChannel(UUID userID, UUID channelId) {
        User user = getUserById(userID);
        user.removeJoinedChannel(channelId);
        saveUser();
    }


    public void validateUserExists(UUID userId) {
        if (!users.containsKey(userId)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }
}
