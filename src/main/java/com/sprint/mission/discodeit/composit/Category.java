package com.sprint.mission.discodeit.composit;

import com.sprint.mission.discodeit.Iterator.ChannelIterator;
import com.sprint.mission.discodeit.Iterator.Iterator;

import java.util.LinkedList;

public class Category extends CategoryAndChannel {
    private CategoryAndChannel head;
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
    public void add(CategoryAndChannel channel) {
        list.add(channel);
        head = channel;
    }

    @Override
    public void remove(CategoryAndChannel channel) {
        list.remove(channel);
    }

    //업데이트 조건, id가 동일해야한다.
    @Override
    public void update(CategoryAndChannel channel, String replaceName) {
        channel.setName(replaceName);
    }

    @Override
    public void add(String str) {
        // static필드 이용해서 선언해야함
//        list.add(new Channel(null, str));
    }

    @Override
    public void remove() {
        if (list.isEmpty()) {
            System.out.println("카테고리와 채널이 없습니다.");
            return;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void print() {
        int j = 0;
        CategoryAndChannel temp1 = head;
        while (j < list.size()) {
            CategoryAndChannel temp2 = head;
            if (checkCategory(list.get(j))) {
                head = list.get(j);
            }
        }
    }

    @Override
    public void printHead() {
        System.out.println(head.getName());
    }

    @Override
    public void printCurrent() {
        if (list.isEmpty()) {
            System.out.println("카테고리와 채널이 없습니다.");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + " : " + list.get(i).getName());
        }
    }

    // 구현해야할 것
    @Override
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
