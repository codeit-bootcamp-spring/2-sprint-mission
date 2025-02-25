package com.sprint.mission.discodeit.composit;

import com.sprint.mission.discodeit.Iterator.Iterator;
import com.sprint.mission.discodeit.entity.BaseEntity;

import java.util.LinkedList;

public abstract class CategoryAndChannel extends BaseEntity {
    public CategoryAndChannel(String id, String name) {
        super(id, name);
    }

    public abstract LinkedList<CategoryAndChannel> getList();
    public abstract void addChannel(CategoryAndChannel channel);

    public abstract void removeChannel(CategoryAndChannel channel);

    public abstract void updateChannel(CategoryAndChannel channel,String replaceName);

    public abstract void printCurrent();

    public abstract Iterator iterator();

}
