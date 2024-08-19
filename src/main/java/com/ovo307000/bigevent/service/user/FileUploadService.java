package com.ovo307000.bigevent.service.user;

import com.ovo307000.bigevent.config.properties.MinioProperties;
import com.ovo307000.bigevent.core.utils.FileUtil;
import io.minio.*;
import io.minio.errors.*;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service("fileUploadService")
public class FileUploadService
{
    private static final Logger log = LoggerFactory.getLogger(FileUploadService.class);

    private final MinioClient minioClient;
    private final String      bucketName;
    private final FileUtil    fileUtil;

    public FileUploadService(MinioClient minioClient, MinioProperties minioProperties, FileUtil fileUtil)
    {
        this.minioClient = minioClient;

        this.bucketName = Objects.requireNonNull(minioProperties.getBucketName(), "Minio bucket name cannot be null");

        log.debug("bucketName: {}", this.bucketName);
        this.fileUtil = fileUtil;
    }

    public ObjectWriteResponse upload(@NotNull MultipartFile file) throws
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
        this.createBucketIfNotExists(this.bucketName);

        return this.minioClient.uploadObject(this.getUploadObjectArgs(file, this.bucketName));
    }

    private void createBucketIfNotExists(String bucketName) throws
                                                            IOException,
                                                            ServerException,
                                                            InsufficientDataException,
                                                            ErrorResponseException,
                                                            NoSuchAlgorithmException,
                                                            InvalidKeyException,
                                                            InvalidResponseException,
                                                            XmlParserException,
                                                            InternalException
    {
        if (! this.minioClient.bucketExists(getBucketExistsArgs(bucketName)))
        {
            this.minioClient.makeBucket(getMakeBucketArgs(bucketName));
        }
    }

    private static MakeBucketArgs getMakeBucketArgs(String bucketName)
    {
        return MakeBucketArgs.builder()
                             .bucket(bucketName)
                             .build();
    }

    private static BucketExistsArgs getBucketExistsArgs(String bucketName)
    {
        return BucketExistsArgs.builder()
                               .bucket(bucketName)
                               .build();
    }

    private UploadObjectArgs getUploadObjectArgs(MultipartFile file, String bucketName) throws IOException
    {
        return UploadObjectArgs.builder()
                               .bucket(bucketName)
                               .object(this.fileUtil.generateUUIDFileName(file.getOriginalFilename()))
                               .filename(file.getOriginalFilename())
                               .build();
    }

    public ObjectWriteResponse put(@NotNull MultipartFile file) throws
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
        this.createBucketIfNotExists(this.bucketName);

        return this.minioClient.putObject(this.getPutObjectArgs(file, this.bucketName));
    }

    private PutObjectArgs getPutObjectArgs(MultipartFile file, String bucketName) throws IOException
    {
        return PutObjectArgs.builder()
                            .object(this.fileUtil.generateUUIDFileName(file.getOriginalFilename()))
                            .bucket(bucketName)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), - 1)
                            .build();
    }
}
