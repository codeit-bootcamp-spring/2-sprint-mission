package com.sprint.mission.discodeit.entity;

public class User  extends Common{
    private String name;

    public User(String name){
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void update(String name){
        this.name = name;
        updateUpdatedAt();
    }
}
