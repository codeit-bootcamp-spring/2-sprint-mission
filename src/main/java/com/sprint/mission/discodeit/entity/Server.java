package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.composit.CategoryAndChannel;

public class Server extends BaseEntity{
    private CategoryAndChannel baseCategory;
    private CategoryAndChannel head;

    public Server(String name) {
        super(name);
    }

}
