package com.ovo307000.bigevent.service.user;

import com.ovo307000.bigevent.config.properties.MinioProperties;
import com.ovo307000.bigevent.factory.MinioRequestFactory;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service("fileService")
public class FileService
{
    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    private final MinioClient         minioClient;
    private final String              bucketName;
    private final MinioRequestFactory requestFactory;

    public FileService(MinioClient minioClient, MinioProperties minioProperties, MinioRequestFactory requestFactory)
    {
        this.bucketName = Objects.requireNonNull(minioProperties.getBucketName(), "Minio bucket name cannot be null");

        this.minioClient    = minioClient;
        this.requestFactory = requestFactory;

        this.createBucketIfNotExists();
    }

    private void createBucketIfNotExists()
    {
        try
        {
            if (! this.minioClient.bucketExists(this.requestFactory.createBucketExistsArgs(this.bucketName)))
            {
                this.minioClient.makeBucket(this.requestFactory.createMakeBucketArgs(this.bucketName));
            }
        }
        catch (Exception e)
        {
            log.error("Failed to create bucket {}", this.bucketName, e);
        }
    }

    public List<CompletableFuture<ObjectWriteResponse>> handleFilesAsync(@NotNull List<MultipartFile> files, Type type)
    {
        return files.stream()
                    .map((MultipartFile file) -> CompletableFuture.supplyAsync(() -> this.handleFile(file, type)))
                    .toList();
    }

    public ObjectWriteResponse handleFile(@NotNull MultipartFile file, Type type)
    {
        try
        {
            switch (type)
            {
                case PUT ->
                {
                    return this.minioClient.putObject(this.requestFactory.createPutObjectArgs(file, this.bucketName));
                }
                case UPLOAD ->
                {
                    return this.minioClient.uploadObject(this.requestFactory.createUploadObjectArgs(file,
                                                                                                    this.bucketName));
                }

                default -> throw new IllegalArgumentException("Unsupported operation type: " + type);
            }
        }
        catch (Exception e)
        {
            log.error("Failed to {} file to Minio",
                      type.name()
                          .toLowerCase(),
                      e);
            return null;
        }
    }

    public enum Type
    {
        PUT,
        UPLOAD
    }
}