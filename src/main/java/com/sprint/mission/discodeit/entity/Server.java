package com.sprint.mission.discodeit.entity;


import java.io.Serializable;
import java.util.UUID;

public class Server extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    public Server(String name) {
        super();
        this.name = name;
    }

    public Server(UUID id, String name) {
        super(id);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Server{" +
                "name='" + name + '\'' +
                '}';
    }
}
