package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class FileUserService implements UserService {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data", "user");

    public final List<User> usersData;

    public FileUserService() {
        usersData = new ArrayList<>();
    }

    // 사용자 생성
    @Override
    public User create(User user) {
        if (find(user.getName()) != null) {
            System.out.println("등록된 사용자가 존재합니다.");
            return null;
        } else{
            usersData.add(user);
            save(user);
            System.out.println(user);
            return user;
        }
    }

    private void save(User user) {
        init();
        Path filePath = directory.resolve(user.getId() + ".ser");
        saveToFile(filePath, user);
    }

    private void init() {
        // 저장할 경로의 파일 초기화
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveToFile(Path path, User user) {
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // 사용자 조회
    @Override
    public User getUser(String name) {
        return find(name);
    }

    private User find(String name) {
        return load().stream()
                .filter(user -> user.getName().equals(name))
                .findAny()
                .orElse(null);
    }

    private List<User> load() {
        if (Files.exists(directory)) {
            try (Stream<Path> path = Files.list(directory)) {
                return path
                        .map(this::loadToFile)
                        .toList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return Collections.emptyList();
        }
    }

    private User loadToFile(Path path) {
        try (FileInputStream fis = new FileInputStream(path.toFile());
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    //사용자 전체 조회
    @Override
    public List<User> getAllUser() {
        List<User> userList = load();
        if (userList.isEmpty()) {
            System.out.println("전체 조회 결과가 없습니다.");
            return Collections.emptyList();
        }
        return userList;
    }


    // 사용자 수정
    @Override
    public User update(String name, String changeName, String changeEmail) {
        User user = find(name);
        if (user == null) {
            System.out.println("사용자가 존재하지 않습니다.");
            return null;
        } else {
            user.update(changeName, changeEmail);
            save(user);
        }
        return user;
    }


    // 사용자 삭제
    @Override
    public void delete(String name) {
        User user = find(name);
        try {
            if (user == null && !Files.exists(directory.resolve(user.getId() + ".ser"))) {
            } else {
                Files.delete(directory.resolve(user.getId() + ".ser"));
            }
        } catch (IOException | NullPointerException e) {
            System.out.println("사용자가 존재하지 않습니다.\n" + e.getMessage());
        }
    }
}


