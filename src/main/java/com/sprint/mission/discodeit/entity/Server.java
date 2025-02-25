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

    public void print() {
        baseCategory.printCurrentCategoryAndChannel();
    }

    public void addChannel(String name) {
        //여긴 추상클래스로 만든 뒤, 구체 클래스에서 static count해서 값을 매긴 뒤 넘길 예정
        String test = "Test";
        baseCategory.addChannel(new Channel(test, name));
    }

    public void update() {

    }

}
