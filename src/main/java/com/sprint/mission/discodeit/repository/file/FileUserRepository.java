package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    private final String USER_FILE = "users.ser";
    private final Map<UUID, User> userData;

    public  FileUserRepository() {
        userData = loadData();
    }

    private Map<UUID, User> loadData(){
        File file = new File(USER_FILE);
        if(!file.exists()){
            return new HashMap<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)){
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveData(){
        try(FileOutputStream fos = new FileOutputStream(USER_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(this.userData);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public User save(User user) {
        userData.put(user.getId(), user);
        saveData();
        return  user;
    }

    @Override
    public User findById(UUID id) {
        return userData.get(id);
    }

    @Override
    public List<User> findAll(String name) {
        return userData.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        userData.remove(id);
        saveData();
    }
}
