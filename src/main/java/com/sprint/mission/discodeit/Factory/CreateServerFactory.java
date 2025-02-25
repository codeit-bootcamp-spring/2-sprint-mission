package com.sprint.mission.discodeit.Factory;

import com.sprint.mission.discodeit.entity.Server;

public class CreateServerFactory implements Factory{
    private static int count = 0;

    // 싱글톤 패턴 적용
    private static CreateServerFactory instance;

    private CreateServerFactory() {
    }

    public static CreateServerFactory getInstance() {
        if (instance == null) {
            instance = new CreateServerFactory();
        }
        return instance;
    }

    @Override
    public Server create(String str) {
        return new Server("S"+count++,str);
    }
}
