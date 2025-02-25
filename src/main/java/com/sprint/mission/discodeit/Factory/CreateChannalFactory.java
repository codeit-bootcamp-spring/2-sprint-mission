package com.sprint.mission.discodeit.Factory;

import com.sprint.mission.discodeit.entity.Channel;

public class CreateChannalFactory implements Factory<Channel>{
    private static int count = 0;

    // 싱글톤 패턴 적용
    private static CreateChannalFactory instance;

    private CreateChannalFactory() {
    }

    public static CreateChannalFactory getInstance() {
        if (instance == null) {
            instance = new CreateChannalFactory();
        }
        return instance;
    }


    @Override
    public Channel create(String name) {
        return new Channel(name);
    }


}
