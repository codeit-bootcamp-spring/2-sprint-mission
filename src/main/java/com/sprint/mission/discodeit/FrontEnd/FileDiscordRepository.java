package com.sprint.mission.discodeit.FrontEnd;

import com.sprint.mission.discodeit.entity.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class FileDiscordRepository implements DiscordRepository{
    private static FileDiscordRepository instance;
    private final Path path =  Paths.get(System.getProperty("user.dir"), "data", "UserList.ser");
    private List<User> list = new LinkedList<>();

    private FileDiscordRepository(){
        loadUserList();
    }

    public static FileDiscordRepository getInstance() {
        if (instance == null) {
            instance = new FileDiscordRepository();
        }
        return instance;
    }

    private void init() {
        Path directory = path.getParent();
        if (!Files.exists(directory)) {
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
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                List<User> list = (List<User>) ois.readObject();
                for (User user : list) {
                    //나중에 바꿔야 함
                    User u = new User(user.getId(), user.getCreatedAt(), user.getName(), user.getPassword());

                    this.list.add(u);
                    System.out.println("유저 로드 완료 - ID 유지: " + u.getId());
                }

                System.out.println("유저 리스트 로드 완료: " + path);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("유저 리스트 로드 실패");
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void register(User user) {
        list.add(user);
        saveUserList();
    }

    private void saveUserList() {
        init();

        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(list);

        } catch (IOException e) {
            System.out.println("유저 리스트 저장 실패");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getUserList() {
        return list;
    }

    @Override
    public void updateUserList(List<User> userList) {
        this.list = userList;
        saveUserList();
    }
}
