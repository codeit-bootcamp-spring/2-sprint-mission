//package com.sprint.mission.discodeit.repository.file;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = false)
//public class FileRepositoryConfig {
//
//    @Bean
//    public FileUserRepository fileUserRepository(/* 팩토리 클래스 */) {
//        //
//
//        return new FileUserRepository();
//    }
//
//    @Bean
//    public FileChannelRepository fileChannelRepository() {
//        return new FileChannelRepository();
//    }
//
//    @Bean
//    public FileMessageRepository fileMessageRepository() {
//        return new FileMessageRepository();
//    }
//
//    @Bean
//    public FileReadStatusRepository fileReadStatusRepository() {
//        return new FileReadStatusRepository();
//    }
//
//    @Bean
//    public FileBinaryContentRepository fileBinaryContentRepository() {
//        return new FileBinaryContentRepository();
//    }
//
//    @Bean
//    public FileUserStatusRepository fileUserStatusRepository() {
//        return new FileUserStatusRepository();
//    }
//}
