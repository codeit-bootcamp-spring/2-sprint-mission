package com.sprint.mission.discodeit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.File;

@Configuration
public class StorageConfig {

    @Value("${discodeit.storage.data-dir:data}")
    private String dataDir;

    @Bean
    public String dataDir() {
        // 데이터 저장 디렉토리 초기화
        File dataDirFile = new File(dataDir);
        if (!dataDirFile.exists()) {
            boolean created = dataDirFile.mkdirs();
            if (created) {
                System.out.println("데이터 디렉토리 생성 성공: " + dataDirFile.getAbsolutePath());
            } else {
                System.err.println("데이터 디렉토리 생성 실패: " + dataDirFile.getAbsolutePath());
            }
        } else {
            System.out.println("데이터 디렉토리 이미 존재함: " + dataDirFile.getAbsolutePath());
        }
        return dataDir;
    }
}