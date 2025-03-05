package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends SharedEntity {
    private String name;
    private String introduction;
    private String category;
    private final List<String> memberName;
    private final String ownerName;

    public Channel(String category, String name, String introduction, String memberName, String ownerName) {
        this.category = category;
        this.name = name;
        this.introduction = introduction;
        this.memberName = new ArrayList<>();
        this.memberName.add(memberName);
        this.ownerName = ownerName;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void updateCategory(String category) {
        this.category = category;
    }

    public List<String> getMemberName() {
        return memberName;
    }

    public void updateMemberName(String memberName) {
        this.memberName.add(memberName);
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public String toString() {
        return String.format("\n id= %s\n category= %s\n name= %s\n introduction= %s\n ownerUuid= %s\n memberUuid= %s\n createdAt= %s\n updatedAt= %s\n",
                uuid, category, name, introduction, ownerName, memberName, createdAt, updatedAt);
    }
}
