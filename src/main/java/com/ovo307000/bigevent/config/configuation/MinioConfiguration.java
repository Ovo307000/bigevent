package com.ovo307000.bigevent.config.configuation;

import com.ovo307000.bigevent.config.properties.MinioProperties;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("minioConfiguration")
public class MinioConfiguration
{
    private static final Logger log = LoggerFactory.getLogger(MinioConfiguration.class);

    /**
     * 配置并返回一个MinioClient实例
     * 该方法通过注入MinioProperties配置来初始化MinioClient，实现了从配置中动态获取Minio连接信息的功能
     *
     * @param minioProperties 包含Minio配置信息的对象，包括端点、访问密钥和秘密密钥等
     *
     * @return 初始化并配置好的MinioClient实例
     */
    @Bean()
    public MinioClient minioClient(MinioProperties minioProperties)
    {
        // 输出调试信息，记录传入的Minio配置信息
        log.debug("minioProperties: {}", minioProperties);

        // 使用MinioClient的构建者模式，根据提供的配置信息创建一个MinioClient实例
        return MinioClient.builder()
                          .endpoint(minioProperties.getEndpoint())
                          .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                          .build();
    }
}
