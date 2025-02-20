package com.sprint.mission.discodeit.entity;

public class Channel extends Domain {
    private static int count;

    public Channel(){
        this("C" + count++, null);
    }

    public Channel(String id, String name) {
        super(id, name);
    }
}
