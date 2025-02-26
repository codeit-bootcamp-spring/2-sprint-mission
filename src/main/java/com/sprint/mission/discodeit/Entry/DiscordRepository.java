package com.sprint.mission.discodeit.Entry;

import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DiscordRepository {
    private static DiscordRepository instance;
    List<User> list;

    private DiscordRepository( ) {
        list = new ArrayList<>();
    }

    public static DiscordRepository getInstance() {
        if (instance == null) {
            instance = new DiscordRepository();
        }
        return instance;
    }

    public void add(User user) {
        list.add(user);
    }


    public void remove() {
        if (list == null) {
            System.out.println("아무것도 저장되어있지 않습니다.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 유저의 이름을 입력하시오. : ");
        String s = sc.next();
        sc.close();
        for (User data : list) {
            if (data.getName().equals(s)) {
                list.remove(data);
                System.out.println("유저 삭제 성공");
                return;
            }
        }
        System.out.println("해당 유저가 존재하지 않습니다.");
    }



    public void print() {
        if (list.isEmpty()) {
            return;
        }
        System.out.println("======================================");
        for (User data : list) {
            System.out.println(data.getName());
        }
        System.out.println("======================================");
    }


    public void update() {

    }
}
