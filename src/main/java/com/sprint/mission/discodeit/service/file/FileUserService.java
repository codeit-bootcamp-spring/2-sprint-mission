package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private volatile static FileUserService instance = null;
    private static final String FILE_PATH = "users.dat";
    private final Map<UUID, User> userRepository;

    public FileUserService() {
        this.userRepository = loadAll();
    }

    public static FileUserService getInstance(){
        if(instance == null){
            synchronized (FileUserService.class){
                if(instance == null){
                    instance = new FileUserService();
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
            oos.writeObject(userRepository);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public User saveUser(String name) {
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("name is null or empty");
        }
        User user = new User(name);
        userRepository.put(user.getId(), user);
        saveToFile();
        return user;
    }

    @Override
    public List<User> findByName(String name) {
        List<User> users = userRepository.values().stream()
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .toList();
        if(users.isEmpty()){
            throw new NoSuchElementException("해당 이름의 유저가 없습니다.");
        }
        return users;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(userRepository.get(id));
    }

    @Override
    public List<User> findAll() {
        if(userRepository.isEmpty()){
            throw new NoSuchElementException("유저가 없습니다.");
        }
        return userRepository.values().stream().toList();
    }

    @Override
    public void update(UUID id, String name) {
        if(!userRepository.containsKey(id)){
            throw new NoSuchElementException("유저가 없습니다.");
        }
        if(name == null){
            throw new IllegalArgumentException("수정할 이름은 null일 수 없습니다.");
        }

        userRepository.get(id).setName(name);
        saveToFile();
    }

    @Override
    public void delete(UUID id) {
        userRepository.remove(id);
        saveToFile();
    }
}
