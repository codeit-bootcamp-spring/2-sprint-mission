package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.FileService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.SerializationUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;

public class FileUserService implements UserService, FileService<User> {
    // 파일에서 역직렬화해 객체 불러오기
    private List<User> data = loadFromFile();

    private static UserService userService;

    // UserService<User> 구현 객체 반환
    public static UserService getInstanceOfUserService() {
        if (userService == null) {
            userService = new FileUserService();
        }
        return userService;
    }

    // FileService<User> 구현 객체 반환
    public static FileService<User> getInstanceOfFileService() {
        if (userService == null) {
            userService = new FileUserService();
        }
        return (FileUserService) userService;
    }

    // =================================== 유저 생성 ===================================

    @Override
    public void createUser(User user) {
        validateCreateUser(user);
        checkDuplicateUsername(user.getUsername());
        // 유저 객체 샏성 시 직렬화해 파일로 저장
        saveToFile(user);
    }

    private void validateCreateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("생성하려는 유저 정보가 입력되지 않았습니다.");
        }
        if (user.getUsername() == null || user.getUsername().isBlank() || user.getPassword() == null || user.getPassword().isBlank() || user.getNickname() == null || user.getNickname().isBlank()) {
            throw new IllegalArgumentException("필수 입력 항목을 빠뜨리셨습니다. (username, password, nickname)");
        }
    }

    private void checkDuplicateUsername(String username) {
        if (data.stream().
                anyMatch(u -> u.getUsername().equals(username))) {
            throw new IllegalStateException("중복된 username 입니다.");
        }
    }


    // =================================== 유저 조회 ===================================
    @Override
    public User getUser(String username) {
        if (username == null) {
            throw new IllegalArgumentException("username이 입력되지 않았습니다.");
        }

        User findUser = data.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 username을 가진 유저는 존재하지 않습니다."));

        return findUser;
    }


    @Override
    public List<User> getAllUsers() {
        return data;
    }


    // =================================== 유저 수정 ===================================
    @Override
    public void updateUser(User user) {
        validateUpdateUser(user);
        User findUser = getUser(user.getUsername());
        updateUserInfo(findUser, user);

        saveToFile(findUser);
    }

    private void validateUpdateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("수정 정보가 입력되지 않았습니다.");
        }
        if (user.getPassword() == null || user.getPassword().isBlank() || user.getNickname() == null || user.getNickname().isBlank()) {
            throw new IllegalArgumentException("필수 입력 항목을 빠뜨리셨습니다.");
        }
    }

    private void updateUserInfo(User findUser, User user) {
        findUser.updateUpdatedAt(System.currentTimeMillis());
        findUser.updateNickname(user.getNickname());
        findUser.updatePassword(user.getPassword());
    }


    // =================================== 유저 권한 관련 ===================================
    @Override
    public void addRole(String role, String username) {
        if (role == null) {
            throw new IllegalArgumentException("추가할 권한이 입력되지 않았습니다.");
        }

        User findUser = getUser(username);
        findUser.addRole(role);

        saveToFile(findUser);
    }

    @Override
    public void removeRole(String role, String username) {
        if (role == null) {
            throw new IllegalArgumentException("삭제할 권한이 입력되지 않았습니다.");
        }
        User findUser = getUser(username);
        if (!findUser.getRoles().stream().anyMatch(r -> r.equals(role))) {
            throw new NoSuchElementException("해당 유저에게 해당 권한은 존재하지 않습니다."); // role 자체는 유효한 값이지만, 검색 결과 role이 존재하지 않으므로 NoSuchElementException
        }
        findUser.removeRole(role);

        saveToFile(findUser);
    }


    // =================================== 유저 삭제 ===================================
    @Override
    public void deleteUser(String username) {
        User findUser = getUser(username);
        data.remove(findUser);
        deleteFile(findUser); // 파일에서도 삭제
    }


    // =================================== 직렬화 관련 메소드 ===================================
    @Override
    public  void saveToFile(User user) {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "user");
        SerializationUtil.init(directory); // 디렉토리가 존재하지 않으면 생성
        Path filePath = directory.resolve(user.getUsername().concat(".ser"));
        SerializationUtil.serialization(filePath, user);
        data = loadFromFile(); // 데이터를 최신으로 유지
    }

    @Override
    public void deleteFile(User user) {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "user");
        Path filePath = directory.resolve(user.getUsername() + ".ser");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("유저 파일 삭제 실패: " + e.getMessage());
        }
        data = loadFromFile(); // 데이터를 최신으로 유지
    }

    @Override
    public List loadFromFile() {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "user");
        return SerializationUtil.reverseSerialization(directory);
    }

}



