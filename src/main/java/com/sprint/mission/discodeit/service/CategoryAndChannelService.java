package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.Iterator.Iterator;
import com.sprint.mission.discodeit.composit.CategoryAndChannel;

public interface CategoryAndChannelService {

    public abstract void add(CategoryAndChannel channel);

    public abstract void add(String str);

    public abstract void remove();

    public abstract void remove(CategoryAndChannel channel);

    public abstract void update();

    public abstract void update(CategoryAndChannel channel, String replaceName);

    public abstract void print();

    public abstract void printCurrent();

    public abstract void printHead();

    public abstract boolean checkCategory(CategoryAndChannel item);

    public abstract Iterator iterator();


}
