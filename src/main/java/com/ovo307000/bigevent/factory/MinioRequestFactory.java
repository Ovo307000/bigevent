package com.ovo307000.bigevent.factory;

import com.ovo307000.bigevent.core.utils.FileUtil;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component("minioFactory")
public class MinioRequestFactory
{
    private final FileUtil fileUtil;

    public MinioRequestFactory(FileUtil fileUtil)
    {
        this.fileUtil = fileUtil;
    }

    public PutObjectArgs createPutObjectArgs(@NotNull MultipartFile file, @NotNull String bucketName) throws IOException
    {
        return PutObjectArgs.builder()
                            .object(this.fileUtil.generateUUIDFileName(file.getOriginalFilename()))
                            .bucket(bucketName)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), - 1)
                            .build();
    }

    public UploadObjectArgs createUploadObjectArgs(@NotNull MultipartFile file, @NotNull String bucketName)
    {
        return UploadObjectArgs.builder()
                               .bucket(bucketName)
                               .object(this.fileUtil.generateUUIDFileName(file.getOriginalFilename()))
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
