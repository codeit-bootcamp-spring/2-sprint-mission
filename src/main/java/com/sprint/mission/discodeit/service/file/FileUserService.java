package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {

    private final String FILE_PATH = "src/main/resources/users.dat";
    private Map<UUID, User> users = new HashMap<>();
    private Map<String, UUID> userIds = new HashMap<>();
    private static FileUserService INSTANCE;

    private FileUserService() {
        loadUserFromFile();
    }

    public static synchronized FileUserService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileUserService();
        }
        return INSTANCE;
    }

    private void saveUserToFile(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);   // ✅ 첫 번째 Map 저장
            oos.writeObject(userIds); // ✅ 두 번째 Map 저장
        } catch (IOException e) {
            throw new RuntimeException("유저 저장 중 오류 발생", e);
        }
    }

    private void loadUserFromFile(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            users = (Map<UUID, User>) ois.readObject();   // ✅ 첫 번째 Map 로드
            userIds = (Map<String, UUID>) ois.readObject();  // ✅ 두 번째 Map 로드
        }catch (EOFException e) {
            System.out.println("⚠ users.dat 파일이 비어 있습니다. 빈 데이터로 유지합니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("유저 로드 중 오류 발생", e);
        }
    }

    @Override
    public void updataUserData() {
    }

    @Override
    public void createUser(String username) {
        if (userIds.containsKey(username)) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        }
        User user = new User(username);
        users.put(user.getId(), user);
        userIds.put(username, user.getId());
        saveUserToFile();
    }

    @Override
    public User getUserByName(String username) {
        validateUserExists(username);
        return users.get(userIds.get(username));
    }

    @Override
    public User getUserById(UUID userId) {
        validateUserExists(userId);
        return users.get(userId);
    }

    @Override
    public UUID getUserIdByName(String username) {
        validateUserExists(username);
        return userIds.get(username);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public String getUserNameByid(UUID userId) {
        validateUserExists(userId);
        return users.get(userId).getUsername();
    }

    @Override
    public void updateUsername(UUID userID, String newUsername) {
        if (userIds.containsKey(newUsername)) {
            throw new IllegalArgumentException("이미 존재하는 유저명입니다.");
        }
        User user = getUserById(userID);
        String oldUserName = user.getUsername();
        user.updateUsername(newUsername);
        userIds.put(newUsername, userIds.remove(oldUserName));
        saveUserToFile();
    }

    @Override
    public void addChannel(UUID userID, UUID channelId) {
        User user = getUserById(userID);

        if (user.isJoinedChannel(channelId)) {
            throw new IllegalArgumentException("이미 가입되어 있는 채널입니다. ");
        }

        user.addJoinedChannel(channelId);
        saveUserToFile();
    }

    @Override
    public void deleteUser(UUID userID) {
        User user = getUserById(userID);
        users.remove(user.getId());
        userIds.remove(user.getUsername());
        saveUserToFile();
    }

    @Override
    public void deleteChannel(UUID userID, UUID channelId) {
        User user = getUserById(userID);
        user.removeJoinedChannel(channelId);
        saveUserToFile();
    }

    public void validateUserExists(String username) {
        UUID userId = userIds.get(username);
        if (userId == null) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        validateUserExists(userId);
    }

    public void validateUserExists(UUID userId) {
        if (!users.containsKey(userId)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }
}
