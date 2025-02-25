package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.composit.Category;
import com.sprint.mission.discodeit.composit.CategoryAndChannel;
import com.sprint.mission.discodeit.composit.Channel;

public class Server extends BaseEntity{
    private CategoryAndChannel baseCategory;

    public Server(String id, String name) {
        super(id, name);
        baseCategory = new Category("B1","BaseCategory");
        baseCategory.addChannel(new Channel("BC1","BaseChannel"));
    }


}
