package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel extends SharedEntity {
    private String name;
    private String introduction;
    private String category;
    private List<UUID> memberKeys;
    private UUID ownerKey;
    private List<String> memberNames;
    private String ownerName;
    public Channel(String category, String name, String introduction, UUID memberKey, UUID ownerKey, String memberName, String ownerName) {
        this.category = category;
        this.name = name;
        this.introduction = introduction;
        this.memberKeys = new ArrayList<>();
        this.memberKeys.add(memberKey);
        this.ownerKey = ownerKey;
        this.memberNames = new ArrayList<>();
        this.memberNames.add(memberName);
        this.ownerName = ownerName;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCategory() {
        return category;
    }

    public void updateCategory(String category) {
        this.category = category;
    }

    public List<UUID> getMemberKeys() {
        return memberKeys;
    }

    public void updateMemberKeys(UUID memberKey) {
        this.memberKeys.add(memberKey);
    }

    public UUID getOwnerKey() {
        return ownerKey;
    }

    public void updateOwnerKey(UUID ownerKey) {
        this.ownerKey = ownerKey;
    }

    public List<String> getMemberNames() {
        return memberNames;
    }

    public void updateMemberNames(String memberName) {
        this.memberNames.add(memberName);
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void updateOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        return String.format("\n key= %s\n category= %s\n name= %s\n introduction= %s\n memberNames= %s\n ownerName= %s\n createdAt= %s\n updatedAt= %s\n",
                uuid, category, name, introduction, memberNames, ownerName, createdAt, updatedAt);
    }
}
