package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.DTO.User.UserUpdateDTO;
import com.sprint.mission.discodeit.Exception.ServerNotFoundException;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileUserRepository implements UserRepository {
    private  List<User> userList = new ArrayList<>();

    private final Path userPath =  Paths.get(System.getProperty("user.dir"), "data", "UserList.ser");


    public FileUserRepository() {
        loadUserList();

    }


    private void init() {
        Path directory = userPath.getParent();
        if (Files.exists(directory) == false) {
            try {
                Files.createDirectories(directory);
                System.out.println("디렉토리 생성 완료: " + directory);
            } catch (IOException e) {
                System.out.println("디렉토리 생성 실패");
                throw new RuntimeException(e);
            }
        }
    }

    private void loadUserList() {
        if (Files.exists(userPath) == true) {
            try (FileInputStream fis = new FileInputStream(userPath.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                List<User> list = (List<User>) ois.readObject();
                for (User user : list) {
                    User u = new User(user.getId(),user.getProfileId() ,user.getCreatedAt(), user.getName(), user.getEmail(), user.getPassword());
                    userList.add(u);
                }


            } catch (IOException | ClassNotFoundException e) {
                System.out.println("서버 리스트 로드 실패");
                throw new RuntimeException(e);
            }
        }
    }

    private void saveUserList() {
        init();
        try (FileOutputStream fos = new FileOutputStream(userPath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(userList);

        } catch (IOException e) {
            System.out.println("서버 리스트 저장 실패");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reset() {
        init();
        try {
            Files.deleteIfExists(userPath);
            userList = new ArrayList<>();

        } catch (IOException e) {
            System.out.println("리스트 초기화 실패");
        }
    }

    @Override
    public UUID save(User user) {

        userList.add(user);
        saveUserList();
        return user.getId();
    }

    @Override
    public User find(UUID userId) {
        User user = userList.stream().filter(u -> u.getId().equals(userId)).findFirst()
                .orElseThrow(() -> new UserNotFoundException("해당 ID를 가지는 유저를 찾을 수 없습니다."));
        return user;
    }

    @Override
    public List<User> findUserList() {
        return userList;
    }


    @Override
    public UUID update(User user, UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO.replaceId() != null) {
            user.setId(userUpdateDTO.replaceId());
        }
        if (userUpdateDTO.replaceName() != null) {
            user.setName(userUpdateDTO.replaceName());
        }
        if (userUpdateDTO.replaceEmail() != null) {
            user.setEmail(userUpdateDTO.replaceEmail());
        }
        if (userUpdateDTO.binaryContentId() != null) {
            user.setProfileId(userUpdateDTO.binaryContentId());
        }
        saveUserList();
        return user.getId();
    }


    @Override
    public UUID remove(User user) {
        userList.remove(user);
        saveUserList();
        return user.getId();
    }
}

