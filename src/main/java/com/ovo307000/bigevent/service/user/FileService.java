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
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service("fileService")
public class FileService
{
    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    private final MinioClient minioClient;
    private final String      bucketName;
    private final FileUtil    fileUtil;

    public FileService(MinioClient minioClient, MinioProperties minioProperties, FileUtil fileUtil)
    {
        this.minioClient = minioClient;

        this.bucketName = Objects.requireNonNull(minioProperties.getBucketName(), "Minio bucket name cannot be null");

        log.debug("bucketName: {}", this.bucketName);
        this.fileUtil = fileUtil;
    }

    /**
     * 批量上传文件到MinIO服务器
     *
     * @param files 待上传的文件列表，不能为空
     *
     * @return 成功上传的文件的ObjectWriteResponse列表
     */
    public List<ObjectWriteResponse> putAsync(@NotNull List<MultipartFile> files)
    {
        // 初始化一个CompletableFuture列表，用于接收异步上传文件的任务
        List<CompletableFuture<ObjectWriteResponse>> completableFutures;

        // 对文件列表进行流处理，每个文件异步上传
        completableFutures = files.stream()
                                  .map((MultipartFile file) -> CompletableFuture.supplyAsync(() ->
                                                                                             {
                                                                                                 try
                                                                                                 {
                                                                                                     // 异步上传单个文件并返回响应
                                                                                                     return FileService.this.put(
                                                                                                             file);
                                                                                                 }
                                                                                                 catch (Exception exception)
                                                                                                 {
                                                                                                     // 记录上传文件到MinIO失败的错误日志
                                                                                                     log.error(
                                                                                                             "Failed to upload file to Minio",
                                                                                                             exception);

                                                                                                     // 异常情况下返回null
                                                                                                     return null;
                                                                                                 }
                                                                                             }))
                                  .toList();

        // 收集所有已完成的异步任务的结果，并过滤掉null值
        return completableFutures.stream()
                                 .map(CompletableFuture::join)
                                 .filter(Objects::nonNull)
                                 .toList();
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

    private PutObjectArgs getPutObjectArgs(MultipartFile file, String bucketName) throws IOException
    {
        return PutObjectArgs.builder()
                            .object(this.fileUtil.generateUUIDFileName(file.getOriginalFilename()))
                            .bucket(bucketName)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), - 1)
                            .build();
    }

    /**
     * 批量上传文件到MinIO
     *
     * @param files 待上传的文件列表，不能为空
     *
     * @return 返回上传到MinIO的文件的ObjectWriteResponse列表
     */
    public List<ObjectWriteResponse> uploadAsync(@NotNull List<MultipartFile> files)
    {
        // 初始化一个CompletableFuture列表，用于接收异步上传文件的任务
        List<CompletableFuture<ObjectWriteResponse>> completableFutures;

        // 对文件列表进行流处理，每个文件异步上传
        completableFutures = files.stream()
                                  .map((MultipartFile file) -> CompletableFuture.supplyAsync(() ->
                                                                                             {
                                                                                                 try
                                                                                                 {
                                                                                                     // 异步上传单个文件并返回响应
                                                                                                     return FileService.this.upload(
                                                                                                             file);
                                                                                                 }
                                                                                                 catch (Exception exception)
                                                                                                 {
                                                                                                     // 记录上传文件到MinIO失败的错误日志
                                                                                                     log.error(
                                                                                                             "Failed to upload file to Minio",
                                                                                                             exception);
                                                                                                 }

                                                                                                 return null;
                                                                                             }))
                                  .toList();

        // 收集所有CompletableFuture的结果，过滤掉null值，并返回一个非null的ObjectWriteResponse列表
        return completableFutures.stream()
                                 .map(CompletableFuture::join)
                                 .filter(Objects::nonNull)
                                 .toList();
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

    private UploadObjectArgs getUploadObjectArgs(MultipartFile file, String bucketName) throws IOException
    {
        return UploadObjectArgs.builder()
                               .bucket(bucketName)
                               .object(this.fileUtil.generateUUIDFileName(file.getOriginalFilename()))
                               .filename(file.getOriginalFilename())
                               .build();
    }
}
