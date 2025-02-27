package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String username;

    public User(String username) {
        super();
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public User updateUsername(String newUsername) {
        if(this.username.equals(newUsername)){
            System.out.println("같은 이름으로 변경할 수 없습니다.");
            return this;
        }

        this.username = newUsername;
        update();

        return this;
    }
    @Override
    public String toString() {
        return "User{name= '" + username + "', ID= " + getId() +", createdtime= "+getCreatedAt()+", updatedtime= "+getUpdatedAt()+"}";
    }

    public String toString(boolean bool){
        if(bool){
            return "User "+username+"은 삭제되지 않았습니다.";
        }
        return "User "+username+"은 삭제되었습니다.";
    }

}
