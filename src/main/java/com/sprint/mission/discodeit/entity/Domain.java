package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Domain {
    protected String id;
    protected String name;
    protected Long createdAt;

    //지역변수
    private SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");

    public Domain(String id, String name) {
        this.id = id;
        this.name = name;
        this.createdAt = System.currentTimeMillis();
    }

    //GETTER
    public String getId() {
        return id;
    }

    public Long getCreatedAt() {
        System.out.println("생성 시각: " + dayTime.format(new Date(createdAt)));
        return createdAt;
    }

}
