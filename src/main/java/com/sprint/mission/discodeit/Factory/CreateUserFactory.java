package com.sprint.mission.discodeit.Factory;

import com.sprint.mission.discodeit.entity.User;

import java.util.Scanner;

public class CreateUserFactory implements Factory<User>{
    private static volatile CreateUserFactory instance;
    private final Scanner sc = new Scanner(System.in);

    private CreateUserFactory() {
    }

    public static CreateUserFactory getInstance() {
        if (instance == null) {
            synchronized (CreateUserFactory.class){
                if (instance == null) {
                    instance = new CreateUserFactory();
                }
            }
        }
        return instance;
    }

    @Override
    public User create() {
        System.out.print("생성할 유저의 이름을 입력하세요. : ");
        String s = sc.nextLine();
        return create(s);
    }

    @Override
    public User create(String name) {
        System.out.print("패스워드를 입력하세요. : ");
        String s = sc.nextLine();
        return new User(name, s);
    }

}
