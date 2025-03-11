package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private volatile static FileUserRepository instance = null;
    private static final String FILE_PATH = "users.dat";
    private final Map<UUID, User> data;

    public FileUserRepository() {
        this.data = loadAll();
    }

    public static FileUserRepository getInstance(){
        if(instance == null){
            synchronized (FileUserService.class){
                if(instance == null){
                    instance = new FileUserRepository();
                }
            }
        }
        return instance;
    }

    private Map<UUID, User> loadAll(){
        File file = new File(FILE_PATH);
        if(!file.exists()){
            return new HashMap<>();
        }

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            return (Map<UUID, User>) ois.readObject();
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveToFile(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(data);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void save(User user) {
        data.put(user.getId(), user);
        saveToFile();
    }

    @Override
    public User findById(UUID id) {
        return Optional.ofNullable(data.get(id)).orElseThrow(()-> new NoSuchElementException("User not found"));
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
        saveToFile();
    }
}
