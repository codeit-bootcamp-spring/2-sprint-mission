package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.Objects;


@Getter
public class BinaryContent extends BaseEntity {


    private String fileName;
    private Long size;
    private String contentType;
    private byte[] bytes;

    public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
        super();
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    public void updateBinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryContent binaryContent) {
            return this.getId().equals(binaryContent.getId());
        }
        return false;
    }

    @Override
    public String toString(){
        return "\nContent ID: " + getId();


    }
}
