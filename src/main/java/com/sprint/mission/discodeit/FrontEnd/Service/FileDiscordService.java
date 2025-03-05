package com.sprint.mission.discodeit.FrontEnd.Service;

import com.sprint.mission.discodeit.Factory.CreateUserFactory;
import com.sprint.mission.discodeit.FrontEnd.DiscordService;
import com.sprint.mission.discodeit.FrontEnd.Repository.FileDiscordRepository;
import com.sprint.mission.discodeit.entity.User;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class FileDiscordService implements DiscordService {
    private static FileDiscordService instance;
    FileDiscordRepository discordRepository = FileDiscordRepository.getInstance();
    List<User> list = discordRepository.getUserList();

    private final Path path = Paths.get(System.getProperty("user.dir"), "data", "UserList.ser");

    private FileDiscordService() {
    }

    public static FileDiscordService getInstance() {
        if (instance == null) {
            instance = new FileDiscordService();
        }
        return instance;
    }

    @Override
    public User create() {
        User user = CreateUserFactory.getInstance().create();
        discordRepository.register(user);
        return user;
    }

    @Override
    public User create(String name) {
        User user = CreateUserFactory.getInstance().create(name);
        discordRepository.register(user);
        return user;
    }

    @Override
    public User get() {
        if (list == null) {
            System.out.println("아무것도 저장되어있지 않습니다.");
            return null;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("찾을 유저의 이름을 입력하시오. : ");
        String targetName = sc.next();
        return get(targetName);
    }

    @Override
    public User get(User user) {
        return get(user.getName());
    }

    @Override
    public User get(String targetName) {
        for (User data : list) {
            if (data.getName().equals(targetName)) {
                System.out.println(data.getName() + " 유저 조회 성공");
                return data;
            }
        }
        System.out.println("해당 유저가 존재하지 않습니다.");
        return null;
    }

    @Override
    public boolean remove() {
        if (list == null) {
            System.out.println("아무것도 저장되어있지 않습니다.");
            return false;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 유저의 이름을 입력하시오. : ");
        String targetName = sc.next();
        return remove(targetName);
    }

    @Override
    public boolean remove(User user) {
        if (list == null || user == null) {
            System.out.println("아무것도 저장되어있지 않습니다.");
            return false;
        }
        return remove(user.getName());
    }

    @Override
    public boolean remove(String targetName) {
        if (list == null) {
            System.out.println("아무것도 저장되어있지 않습니다.");
            return false;
        }
        for (User data : list) {
            if (data.getName().equals(targetName)) {

                list.remove(data);
                discordRepository.updateUserList(list);

                System.out.println(data.getName() + "유저 삭제 성공");
                return true;
            }
        }
        System.out.println("해당 유저가 존재하지 않습니다.");
        return false;
    }

    @Override
    public void print() {
        if (list.isEmpty()) {
            return;
        }
        System.out.println("\n==============유저 정보===================");
        for (User data : list) {
            System.out.println(data.getName());
        }
        System.out.println("======================================\n");

    }

    @Override
    public boolean update() {
        if (list.isEmpty()) {
            return false;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("변경할 유저의 이름을 입력하시오. : ");
        String targetName = sc.next();
        System.out.print("유저 이름을 무엇으로 바꾸시겠습니까? : ");
        String replaceName = sc.next();
        return update(targetName, replaceName);
    }

    @Override
    public boolean update(String targetName) {
        if (list.isEmpty()) {
            return false;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("유저 이름을 무엇으로 바꾸시겠습니까? : ");
        String replaceName = sc.next();
        return update(targetName, replaceName);
    }

    @Override
    public boolean update(User user) {
        if (list.isEmpty()) {
            return false;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("유저 이름을 무엇으로 바꾸시겠습니까? : ");
        String replaceName = sc.next();
        return update(user.getName(), replaceName);
    }

    @Override
    public boolean update(User user, String replaceName) {
        if (list.isEmpty()) {
            return false;
        }
        return update(user.getName(), replaceName);
    }

    @Override
    public boolean update(String targetName, String replaceName) {
        for (User data : list) {
            if (data.getName().equals(targetName)) {
                data.setName(replaceName);
                discordRepository.updateUserList(list);
                System.out.println(data.getName() + "유저 이름 변경 성공");
                return true;
            }
        }
        System.out.println("해당 유저가 존재하지 않습니다.");
        return false;
    }
}
