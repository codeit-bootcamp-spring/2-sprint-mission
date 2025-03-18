package com.sprint.discodeit.domain.entity;

public enum StatusType {

    Active("활성 상태"),
    Inactive("비활성 상태"),
    Away("자리 비움");
    private String explanation;

    StatusType(String explanation){
        this.explanation = explanation;
    }

    public String getExplanation(){
        return explanation;
    }

}
