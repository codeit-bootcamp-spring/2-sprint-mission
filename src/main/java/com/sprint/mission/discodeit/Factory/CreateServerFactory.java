package com.sprint.mission.discodeit.Factory;

import com.sprint.mission.discodeit.entity.Server;

import java.util.Scanner;

public class CreateServerFactory implements Factory<Server> {
    private static volatile CreateServerFactory instance;
    private final Scanner sc = new Scanner(System.in);

    private CreateServerFactory() {
    }

    public static CreateServerFactory getInstance() {
        if (instance == null) {
            synchronized (CreateServerFactory.class) {
                if (instance == null) {
                    instance = new CreateServerFactory();
                }
            }
        }
        return instance;
    }

    @Override
    public Server create() {
        System.out.print("생성할 서버의 이름을 입력하세요. : ");
        String s = sc.nextLine();
        return create(s);
    }

    @Override
    public Server create(String name) {
        return new Server(name);
    }


}
