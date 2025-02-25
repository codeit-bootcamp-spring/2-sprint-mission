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

    public LinkedList<CategoryAndChannel> getList() {
        return list;
    }

    @Override
    public void addChannel(CategoryAndChannel channel) {
        list.add(channel);
    }

    @Override
    public void removeChannel(CategoryAndChannel channel) {
        for (CategoryAndChannel temp : list) {
            if (temp.getId() == channel.getId()) {
                list.remove(temp);
                return;
            }
        }
        System.out.println("삭제할 대상이 존재하지 않습니다.");
    }

    //업데이트 조건, id가 동일해야한다.
    @Override
    public void updateChannel(CategoryAndChannel channel) {
        for (CategoryAndChannel item : list) {
            if (item.getId() == channel.getId()) {
                list.set(list.indexOf(item), channel);
                return;
            }
        }
        System.out.println("업데이트할 대상이 존재하지 않습니다.");
    }

    @Override
    public void printCurrentCategoryAndChannel() {
        for (CategoryAndChannel item : list){
            System.out.println(item.getName());
        }
    }

    @Override
    public void printAll() {

    }

    @Override
    public Iterator iterator() {
        return new ChannelIterator(this);
    }
}
