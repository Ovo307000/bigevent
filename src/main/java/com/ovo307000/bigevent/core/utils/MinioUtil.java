package com.ovo307000.bigevent.core.utils;

import io.minio.*;
import io.minio.errors.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component("minioUtil")
public class MinioUtil
{
    private final MinioClient minioClient;

    public MinioUtil(MinioClient minioClient)
    {
        this.minioClient = minioClient;
    }

    /**
     * 上传文件到云存储
     *
     * @param file       要上传的文件，不能为空
     * @param bucketName 存储桶名称，不能为空
     * @param objectName 文件在存储桶中的名称，不能为空
     *
     * @throws IOException               当发生输入/输出错误时
     * @throws ServerException           当服务器返回错误时
     * @throws InsufficientDataException 当输入数据不足时
     * @throws ErrorResponseException    当服务器返回错误响应时
     * @throws NoSuchAlgorithmException  当算法未找到时
     * @throws InvalidKeyException       当访问密钥无效时
     * @throws InvalidResponseException  当接收到无效响应时
     * @throws XmlParserException        当XML解析失败时
     * @throws InternalException         当发生内部错误时
     */
    public ObjectWriteResponse upload(@NotNull(message = "File cannot be null") MultipartFile file,
                                      @NotNull(message = "Bucket name cannot be null") String bucketName,
                                      @NotNull(message = "Object name cannot be null") String objectName) throws
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
        if (! this.bucketExists(bucketName))
        {
            this.createBucket(bucketName);
        }

        // 构建上传对象的参数
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                                                   .bucket(bucketName)
                                                   .object(objectName)
                                                   .contentType(file.getContentType())
                                                   .stream(file.getInputStream(), file.getSize(), - 1)
                                                   .build();

        // 使用MinIO客户端上传文件
        return this.minioClient.putObject(putObjectArgs);
    }

    public boolean bucketExists(@NotNull String bucketName) throws
                                                            NoSuchAlgorithmException,
                                                            InvalidKeyException,
                                                            InsufficientDataException,
                                                            ErrorResponseException,
                                                            InternalException,
                                                            ServerException,
                                                            IOException,
                                                            InvalidResponseException,
                                                            XmlParserException
    {
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                                                            .bucket(bucketName)
                                                            .build();

        return this.minioClient.bucketExists(bucketExistsArgs);
    }

    public void createBucket(String bucketName) throws
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
        this.minioClient.makeBucket(MakeBucketArgs.builder()
                                                  .bucket(bucketName)
                                                  .build());
    }
}
