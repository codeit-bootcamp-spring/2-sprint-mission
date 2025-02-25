package com.sprint.mission.discodeit.composit;

import com.sprint.mission.discodeit.Iterator.Iterator;
import com.sprint.mission.discodeit.entity.BaseEntity;

public abstract class CategoryAndChannel extends BaseEntity {
    public CategoryAndChannel(String id, String name) {
        super(id, name);
    }

    public abstract void addChannel(CategoryAndChannel channel);

    public abstract void removeChannel(CategoryAndChannel channel);

    public abstract void updateChannel(CategoryAndChannel channel);

    public abstract void printCurrentCategoryAndChannel();

    public abstract void printAll();

    public abstract Iterator iterator();

}
