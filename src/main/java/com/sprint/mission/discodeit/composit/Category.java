package com.sprint.mission.discodeit.composit;

import com.sprint.mission.discodeit.Iterator.ChannelIterator;
import com.sprint.mission.discodeit.Iterator.Iterator;

import java.util.LinkedList;

public class Category extends CategoryAndChannel {
    private LinkedList<CategoryAndChannel> list;

    public Category(String id, String name) {
        super(id, name);
        list = new LinkedList<>();
    }

    @Override
    public LinkedList<CategoryAndChannel> getList() {
        return list;
    }

    @Override
    public void addChannel(CategoryAndChannel channel) {
        list.add(channel);
    }

    @Override
    public void removeChannel(CategoryAndChannel channel) {
        list.remove(channel);
    }

    //업데이트 조건, id가 동일해야한다.
    @Override
    public void updateChannel(CategoryAndChannel channel, String replaceName) {
        channel.setName(replaceName);
    }

    @Override
    public void printCurrent() {
        for (CategoryAndChannel item : list){
            System.out.println(item.getName());
        }
    }

    public boolean checkCategory(CategoryAndChannel item) {
        if (item.getClass().isInstance(Category.class)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Iterator iterator() {
        return new ChannelIterator(this);
    }
}
