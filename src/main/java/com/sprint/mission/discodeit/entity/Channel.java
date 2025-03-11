package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.TimeFormatter;

import java.io.Serializable;

public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    public Channel(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        boolean anyValueUpdated = false;
        if(newName != null && !newName.equals(this.name)){
            anyValueUpdated = true;
        }
        if(anyValueUpdated){
            this.name = newName;
            setUpdatedAt();
        }
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + TimeFormatter.format(createdAt, "yyyy-MM-dd HH:mm:ss") +
                ", updatedAt=" + TimeFormatter.format(updatedAt, "yyyy-MM-dd HH:mm:ss") +
                '}';
    }
}
