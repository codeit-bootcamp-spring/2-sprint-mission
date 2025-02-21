package com.sprint.mission.discodeit.entity;

public class User extends Common {

    //
    private String name;
    private String email;


    public User(String name, String email) {//Common 생성자 호출
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void updateUser(String name, String email) { // 수정시 업데이트 필요함
        this.name = name;
        this.email = email;
        super.update();
    }


}
