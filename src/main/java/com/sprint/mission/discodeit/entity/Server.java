package com.sprint.mission.discodeit.entity;


public class Server extends BaseEntity {
    private String name;

    public Server(String name) {
        super();
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
