package com.sprint.mission.discodeit.Factory;

import com.sprint.mission.discodeit.entity.Container.Channel;

import java.util.Scanner;

public class CreateChannalFactory implements Factory<Channel>{
    private static CreateChannalFactory instance;
    private final Scanner sc = new Scanner(System.in);

    private CreateChannalFactory() {
    }

    public static CreateChannalFactory getInstance() {
        if (instance == null) {
            instance = new CreateChannalFactory();
        }
        return instance;
    }

    @Override
    public Channel create() {
        System.out.print("생성할 채널의 이름을 입력하세요. : ");
        String s = sc.nextLine();
        return create(s);
    }

    @Override
    public Channel create(String name) {
        return new Channel(name);
    }


}
