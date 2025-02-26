package com.sprint.mission.discodeit.FrontEnd;

import com.sprint.mission.discodeit.Factory.CreateUserFactory;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Scanner;

public class JCFDiscordService implements DiscordService {
    private static JCFDiscordService instance;
    DiscordRepository discordRepository = DiscordRepository.getInstance();
    List<User> list = discordRepository.getList();
    private JCFDiscordService() {
    }

    public static JCFDiscordService getInstance() {
        if (instance == null) {
            instance = new JCFDiscordService();
        }
        return instance;
    }

    @Override
    public User create() {
        User user = CreateUserFactory.getInstance().create();
        add(user);
        return user;
    }

    @Override
    public User create(String name) {
        User user = CreateUserFactory.getInstance().create(name);
        add(user);
        return user;
    }

    public void add(User user) {
        list.add(user);
        System.out.println(user.getName() + "유저 저장 성공");
    }


    public void remove() {
        if (list == null) {
            System.out.println("아무것도 저장되어있지 않습니다.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 유저의 이름을 입력하시오. : ");
        String s = sc.next();
        for (User data : list) {
            if (data.getName().equals(s)) {
                list.remove(data);
                System.out.println(data.getName() + "유저 삭제 성공");
                return;
            }
        }
        System.out.println("해당 유저가 존재하지 않습니다.");
    }


    public void print() {
        if (list.isEmpty()) {
            return;
        }
        System.out.println("==============유저 정보===================");
        for (User data : list) {
            System.out.println(data.getName());
        }
        System.out.println("======================================");
    }


    public void update() {
        if (list.isEmpty()) {
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("변경할 유저의 이름을 입력하시오. : ");
        String s = sc.next();
        for (User data : list) {
            if (data.getName().equals(s)) {
                System.out.print("바꿀 이름을 입력하시오. : ");
                s = sc.next();
                data.setName(s);
                System.out.println(data.getName() + "유저 이름 변경 성공");
                return;
            }
        }
        System.out.println("해당 유저가 존재하지 않습니다.");
    }
}
