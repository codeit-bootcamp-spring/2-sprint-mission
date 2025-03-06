package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.Factory.CreateUserFactory;
import com.sprint.mission.discodeit.FrontEnd.DiscordRepository;
import com.sprint.mission.discodeit.FrontEnd.DiscordService;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Scanner;

public class BasicDiscordService implements DiscordService {
    private final DiscordRepository discordRepository;
    List<User> list;

    public BasicDiscordService(DiscordRepository discordRepository) {
        this.discordRepository = discordRepository;
        this.list = discordRepository.getUserList();
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
        User user = list.stream().filter(u -> u.getName().equals(targetName))
                .findFirst().orElse(null);
        if (user != null) {
            System.out.println(user.getName() + " 유저 조회 성공");
            return user;
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
        User user = list.stream().filter(u -> u.getName().equals(targetName))
                .findFirst().orElse(null);
        if (user != null) {
            list.remove(user);
            discordRepository.updateUserList(list);
            System.out.println(user.getName() + "유저 삭제 성공");
            return true;
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
        list.forEach(u-> System.out.println(u.getName()));
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
        User user = list.stream().filter(u -> u.getName().equals(targetName))
                .findFirst().orElse(null);
        if (user != null) {
            user.setName(replaceName);
            discordRepository.updateUserList(list);
            System.out.println(user.getName() + "유저 이름 변경 성공");
            return true;
        }
        System.out.println("해당 유저가 존재하지 않습니다.");
        return false;
    }
}
