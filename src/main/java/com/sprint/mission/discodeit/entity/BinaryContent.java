package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.awt.image.BufferedImage;
import java.nio.file.Path;


@Getter
public class BinaryContent extends BaseEntity {

    private byte[] image;

    public BinaryContent(byte[] image) {;
        this.image = image;

    }

    public void updateBinaryContent(byte[] image) {
        this.image = image;

    }

    @Override
    public String toString(){
        return "\nContent ID: " + getId();


    }
}
