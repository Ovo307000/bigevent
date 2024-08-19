package com.ovo307000.bigevent.factory;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component("minioFactory")
public class MinioRequestFactory
{
    public PutObjectArgs createPutObjectArgs(@NotNull MultipartFile file, @NotNull String bucketName) throws IOException
    {
        return PutObjectArgs.builder()
                            .object(this.generateUUIDFileName(file.getOriginalFilename()))
                            .bucket(bucketName)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), - 1)
                            .build();
    }

    private String generateUUIDFileName(@NotNull String originalFilename)
    {
        return UUID.randomUUID() + "." + this.getFileSuffix(originalFilename);
    }

    private String getFileSuffix(@NotNull String fileName)
    {
        return Optional.of(fileName.substring(fileName.lastIndexOf(".")))
                       .orElseThrow(() -> new IllegalArgumentException("file suffix is not found"));
    }

    public UploadObjectArgs createUploadObjectArgs(@NotNull MultipartFile file, @NotNull String bucketName)
    {
        return UploadObjectArgs.builder()
                               .bucket(bucketName)
                               .object(this.generateUUIDFileName(file.getOriginalFilename()))
                               .build();
    }

    public MakeBucketArgs createMakeBucketArgs(@NotNull String bucketName)
    {
        return MakeBucketArgs.builder()
                             .bucket(bucketName)
                             .build();
    }

    public BucketExistsArgs createBucketExistsArgs(@NotNull String bucketName)
    {
        return BucketExistsArgs.builder()
                               .bucket(bucketName)
                               .build();
    }
}
