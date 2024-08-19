package com.ovo307000.bigevent.service.user;

import com.ovo307000.bigevent.config.properties.MinioProperties;
import com.ovo307000.bigevent.core.utils.MinioUtil;
import io.minio.MinioClient;
import io.minio.errors.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service("fileUploadService")
public class FileUploadService
{
    private final MinioClient     minioClient;
    private final MinioUtil       minioUtil;
    private final MinioProperties minioProperties;

    public FileUploadService(MinioClient minioClient, MinioUtil minioUtil, MinioProperties minioProperties)
    {
        this.minioClient     = minioClient;
        this.minioUtil       = minioUtil;
        this.minioProperties = minioProperties;
    }

    public void upload(@NotNull MultipartFile file) throws
                                                      ServerException,
                                                      InsufficientDataException,
                                                      ErrorResponseException,
                                                      IOException,
                                                      NoSuchAlgorithmException,
                                                      InvalidKeyException,
                                                      InvalidResponseException,
                                                      XmlParserException,
                                                      InternalException
    {
        this.minioUtil.upload(file, this.minioProperties.getBucketName(), this.generateFileNameByUUID(file));
    }

    public String generateFileNameByUUID(@NotNull MultipartFile file)
    {
        return UUID.randomUUID() + "." + this.getFileSuffix(file);
    }

    /**
     * 获取文件的后缀
     */
    private String getFileSuffix(@NotNull MultipartFile file)
    {
        String fileName = Objects.requireNonNull(file.getOriginalFilename(), "file name is not found");

        return Optional.of(fileName.substring(fileName.lastIndexOf(".")))
                       .orElseThrow(() -> new IllegalArgumentException("file suffix is not found"));
    }
}
