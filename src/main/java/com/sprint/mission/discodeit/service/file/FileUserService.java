package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUserService implements UserService {
    private final String EXTENSION = ".ser";
    private final Path directory;

    public FileUserService() {
        this.directory = Paths.get(System.getProperty("user.dir"), "file-data-map", User.class.getSimpleName())
                .toAbsolutePath().normalize();
        init();
    }

    public void init(){
        if(!Files.exists(directory)){
            try{
                Files.createDirectories(directory);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private Path getUserFilePath(UUID id){
        return directory.resolve(id + EXTENSION);
    }

    // Create - 생성
    @Override
    public void createUser(String name) {
        boolean exists = getAllUser().stream()
                .anyMatch(user -> user.getUsername().equals(name));
        if(exists){
            throw new IllegalArgumentException("User already exists");
        }
        // 생성자를 통해 id 생성
        User user = new User(name);
        save(getUserFilePath(user.getId()), user);
    }

    // Read - 읽기, 조회
    @Override
    public List<User> getAllUser(){
        if (!Files.exists(directory)) {
            return new ArrayList<>();
        }

        try {
            return Files.list(directory)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (User) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("파일 로드 실패: " + path, e);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("디렉토리 접근 실패: " + directory, e);
        }
    }
    @Override
    public User getOneUser(UUID id){
        User userNullable = null;
        Path path = getUserFilePath(id);
        if(Files.exists(path)){
            try(
                    FileInputStream fos = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fos)
                    ){
                userNullable = (User) ois.readObject();
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id" + id + " not found"));
    }

    // Update - 수정
    @Override
    public void updateUser(String newName, UUID id) {
        User userNullable = null;
        Path path = getUserFilePath(id);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                userNullable = (User) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));
        user.updateUser(newName);
        save(path, user);
    }

    @Override
    public void deleteUser(UUID id) {
        Path path = getUserFilePath(id);
        System.out.println("실제 삭제하려는 파일 경로: " + path.toAbsolutePath());
        if(Files.notExists(path)){
            throw new NoSuchElementException("User with id " + id + " not found");
        }
        try{
            Files.delete(path);
        }catch (IOException e){
            throw new RuntimeException("file delete error", e);
        }
    }

    // 바이트 스트림으로 변환하여 file에 저장하는 함수
    public static <T> void save(Path filePath, T data) {
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + filePath, e);
        }
    }
}
