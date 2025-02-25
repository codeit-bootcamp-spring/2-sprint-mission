package com.sprint.mission.discodeit.entity;


public class Channel extends BaseEntity {
    private String name;


    public Channel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        super.updateUpdateAt();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
