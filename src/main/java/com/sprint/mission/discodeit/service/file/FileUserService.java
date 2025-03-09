package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

public class FileUserService implements UserService {
    private static final Path directoryPath= Paths.get("data/users");
    private static FileUserService instance = null;

    public static synchronized FileUserService getInstance() {
        if(instance == null) {
            instance= new FileUserService();
        }
        return instance;
    }
    private FileUserService() {
        try{
            Files.createDirectories(directoryPath);
        }catch(IOException e){
            throw new RuntimeException("디렉토리를 생성할 수 없습니다."+ e.getMessage());
        }
    }
    public void saveUser(User user){
        Path filePath= getFilePath(user.getId());
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))){
            oos.writeObject(user);
        }catch(IOException e){
            throw new RuntimeException("사용자 저장 실패:" +user.getId(),e);
        }
    }
    public User loadUser(Path filePath){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("사용자 데이터 읽기 실패: " + filePath, e);
        }
    }
    public Path getFilePath(UUID userid){
        return directoryPath.resolve("user-" + userid + ".data");
    }
    @Override
    public UUID createUser(String username) {
        if (existUser(username)) {
            throw new RuntimeException("이미 존재하는 사용자 이름입니다: " + username);
        }
        User user = new User (username);
        saveUser(user);
        System.out.println("사용자가 생성되었습니다: \n" + user);
        return user.getId();
    }

    @Override
    public void searchUser(UUID id) {
        Path filePath= getFilePath(id);
        if (!Files.exists(filePath)) {
            System.out.println("조회하신 사용자가 존재하지 않습니다.");
            return;
        }
        User user = loadUser(filePath);
        System.out.println("USER: " + user);

    }

    @Override
    public void searchAllUsers() {
        try{
            Files.list(directoryPath)
                    .filter(Files::isRegularFile)
                    .forEach(path->{
                        User user = loadUser(path);
                        System.out.println("USER: " + user);
                    });
        }catch (IOException e){
            throw new RuntimeException("사용자 목록 읽기 실패"+e);
        }
    }

    @Override
    public void updateUser(UUID id) {
        Path filePath= getFilePath(id);
        if(!Files.exists(filePath)) {
            System.out.println("업데이트할 사용자가 존재하지 않습니다.");
            return;
        }
        User user = loadUser(filePath);
        user.updateTime(System.currentTimeMillis());
        saveUser(user);
        System.out.println(id + " 사용자 업데이트 완료되었습니다.");
    }

    @Override
    public void deleteUser(UUID id) {
        Path filePath= getFilePath(id);
        try {
            Files.deleteIfExists(filePath); // 사용자 파일 삭제
            System.out.println(id + " 사용자 삭제 완료되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException("사용자 삭제 실패: " + id, e);
        }
    }
    public boolean existUser(UUID id) {
        return Files.exists(getFilePath(id));
    }
    public boolean existUser(String username) {
        try {
            return Files.list(directoryPath)
                    .filter(Files::isRegularFile)
                    .anyMatch(path -> {
                        User user = loadUser(path); // 파일에서 User 객체 로드
                        return user.getUsername().equals(username); // 이름이 일치하는지 확인
                    });
        } catch (IOException e) {
            throw new RuntimeException("사용자 이름 확인 중 오류 발생: " + e.getMessage());
        }
    }
}
