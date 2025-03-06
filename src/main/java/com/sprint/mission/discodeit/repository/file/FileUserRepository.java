package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {

    private static final String FILE_PATH = "src/main/resources/users.dat";
    private static Map<UUID, User> users = new HashMap<>();

    public FileUserRepository() {
        loadFile();
    }

    private void loadFile() {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            users = (Map<UUID, User>) ois.readObject();
        }catch (EOFException e){
            System.out.println("⚠ users.dat 파일이 비어 있습니다. 빈 데이터로 유지합니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("유저 로드 중 오류 발생", e);
        }
    }

    private void saveFile() {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException("유저 저장 중 오류 발생", e);
        }
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
        saveFile();
    }

    @Override
    public User findById(UUID userId) {
        return users.get(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(UUID userId) {
        users.remove(userId);
        saveFile();
    }

    @Override
    public boolean exists(UUID userId) {
        return users.containsKey(userId);
    }
}
