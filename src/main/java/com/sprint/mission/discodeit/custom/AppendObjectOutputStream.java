package com.sprint.mission.discodeit.custom;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class AppendObjectOutputStream extends ObjectOutputStream {
    public AppendObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        // 기존 파일에 추가할 때는 헤더를 쓰지 않음
        reset();
    }
}
