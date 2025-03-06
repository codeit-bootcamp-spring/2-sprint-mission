package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class BasicUserService implements UserService {
    private volatile static BasicUserService instance = null;
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static BasicUserService getInstance(UserRepository userRepository){
        if(instance == null){
            synchronized (BasicUserService.class){
                if(instance == null){
                    instance = new BasicUserService(userRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public User saveUser(String name) {
        User user = new User(name);
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        if(userRepository.findAll().isEmpty()){
            throw new NoSuchElementException("등록된 유저가 없습니다.");
        }

        return userRepository.findAll();
    }

    @Override
    public List<User> findByName(String name) {
        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .toList();
        if(users.isEmpty()){
            throw new NoSuchElementException("등록된 유저가 없습니다. : " + name);
        }
        return users;
    }

    @Override
    public Optional<User> findById(UUID id) {
        try{
            return Optional.ofNullable(userRepository.findById(id));
        }catch(NoSuchElementException e){
            throw new NoSuchElementException("등록된 유저가 없습니다. : " + id);
        }
    }

    @Override
    public void update(UUID id, String name) {
        try{
            User user = userRepository.findById(id);
            user.setName(name);
            userRepository.save(user);
        }catch(NoSuchElementException e){
            throw new NoSuchElementException("업데이트 할 유저가 없습니다. : " + id);
        }catch(NullPointerException e){
            throw new NullPointerException("수정할 이름은 null일 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        userRepository.delete(id);
    }
}
