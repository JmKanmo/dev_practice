package com.practice.video.config;

import jakarta.annotation.PreDestroy;
import lombok.Data;
import me.desair.tus.server.TusFileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "yml")
@Data
public class TusProtocolConfig {
    @Value("${tus_util.server_protocol}")
    private String serverProtocol;

    @Value("${tus_util.server_address}")
    private String serverAddress;

    @Value("${tus_util.upload_type}")
    private String uploadType;

    @Value("${tus_util.upload_directory}")
    private String uploadDirectory;

    @Value("${tus_util.save_directory}")
    private String saveDirectory;

    @Value("${tus_util.expiration}")
    private Long expiration;

    @PreDestroy
    public void exit() throws Exception {
        // cleanup any expired uploads and stale locks
        tus().cleanup();
    }

    @Bean
    public TusFileUploadService tus() {
        return new TusFileUploadService()
                .withStoragePath(uploadDirectory)
                .withDownloadFeature()
                .withUploadExpirationPeriod(expiration)
                .withThreadLocalCache(true)
                .withUploadUri("/tus/upload/video");
    }
}
