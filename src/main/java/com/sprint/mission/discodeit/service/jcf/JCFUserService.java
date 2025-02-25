package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Scanner;

public class JCFUserService implements UserService {
    @Override
    public User register() {
        Scanner sc = new Scanner(System.in);
        User user;

        System.out.printf("사용자 이름을 입력하시오.\n사용자 이름: ");
        String name = sc.nextLine();

        System.out.printf("비밀번호를 입력하시오.\n비밀번호: ");
        String password = sc.nextLine();

        user = new User(name, password);
        System.out.println("사용자 생성 성공");

        sc.close();
        return user;
    }

    @Override
    public User updateName(User user) {
        Scanner sc = new Scanner(System.in);

        System.out.printf("사용자 이름을 입력하시오.\n사용자 이름: ");
        user.setName(sc.nextLine());

        return user;
    }

    @Override
    public User randomRegister() {
        User user = new User("a", "b");
        System.out.println("사용자 생성 성공");
        return user;
    }
}
