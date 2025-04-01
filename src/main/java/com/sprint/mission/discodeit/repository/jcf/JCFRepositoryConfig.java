//package com.sprint.mission.discodeit.repository.jcf;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
//public class JCFRepositoryConfig {
//
//    @Bean
//    public JCFUserRepository jcfUserRepository() {
//        return new JCFUserRepository();
//    }
//
//    @Bean
//    public JCFChannelRepository jcfChannelRepository() {
//        return new JCFChannelRepository();
//    }
//
//    @Bean
//    public JCFMessageRepository jcfMessageRepository() {
//        return new JCFMessageRepository();
//    }
//
//    @Bean
//    public JCFReadStatusRepository jcfReadStatusRepository() {
//        return new JCFReadStatusRepository();
//    }
//
//    @Bean
//    public JCFBinaryContentRepository jcfBinaryContentRepository() {
//        return new JCFBinaryContentRepository();
//    }
//
//    @Bean
//    public JCFUserStatusRepository jcfUserStatusRepository() {
//        return new JCFUserStatusRepository();
//    }
//}
