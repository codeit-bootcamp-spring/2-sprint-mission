package com.sprint.mission.discodeit.Factory;

import com.sprint.mission.discodeit.entity.Container.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.Scanner;

public class CreateUserFactory implements Factory<User>{
    private static CreateUserFactory instance;
    private Scanner sc = new Scanner(System.in);

    private CreateUserFactory() {
    }

    public static CreateUserFactory getInstance() {
        if (instance == null) {
            instance = new CreateUserFactory();
        }
        return instance;
    }

    @Override
    public User create() {
        System.out.printf("이름을 입력하세요. : ");
        String s = sc.nextLine();
        return create(s);
    }

    @Override
    public User create(String name) {
        System.out.printf("패스워드를 입력하세요. : ");
        String s = sc.nextLine();
        return new User(name, s);
    }

}
