package com.sprint.mission.discodeit;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileInitializer {
    public static void main(String[] args) {
        String filename = "messages.ser";
        try (FileOutputStream fos = new FileOutputStream(filename, false)) {  // false는 덮어쓰기 모드
            // 빈 내용으로 파일 덮어쓰기 -> 초기화
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(filename + " 파일이 초기화되었습니다.");
    }
}
