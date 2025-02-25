package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.composit.Category;
import com.sprint.mission.discodeit.composit.CategoryAndChannel;
import com.sprint.mission.discodeit.composit.Channel;

import java.util.LinkedList;

public class Server extends BaseEntity{
    private CategoryAndChannel baseCategory;
    private Channel head;

    public Server(String id, String name) {
        super(id, name);
        baseCategory = new Category("B1","BaseCategory");
        head = new Channel("BC1","BaseChannel");
        baseCategory.addChannel(head);
    }

    public void print() {
        baseCategory.printCurrent();
    }

    public void addChannel(String name) {
        //여긴 추상클래스로 만든 뒤, 구체 클래스에서 static count해서 값을 매긴 뒤 넘길 예정
        String test = "Test";
        baseCategory.addChannel(new Channel(test, name));
    }

    public void update(String targetName, String replaceName) {
        LinkedList<CategoryAndChannel> list = baseCategory.getList();
        for (CategoryAndChannel item : list) {
            if (item.getName() == targetName) {
                baseCategory.updateChannel(item,replaceName);
            }
        }
    }

    public void remove(String targetName) {
        LinkedList<CategoryAndChannel> list = baseCategory.getList();
        for (CategoryAndChannel item : list) {
            if (item.getName() == targetName) {
                baseCategory.removeChannel(item);
                return;
            }
        }
        System.out.println("채널 삭제 실패");
    }

}
