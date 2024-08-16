package com.ovo307000.bigevent.service.common;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service("r2Service")
public class R2Service
{
    private static final Logger log = LoggerFactory.getLogger(R2Service.class);

    private final MinioClient minioClient;


    public R2Service(MinioClient minioClient)
    {
        this.minioClient = minioClient;
    }

    /**
     * 上传文件到CloudFlare R2存储桶
     *
     * @param bucketName  存储桶名称，不能为空
     * @param objectName  对象（文件）名称，不能为空
     * @param inputStream 待上传的文件输入流，不能为空
     *                    <p>
     *                    使用MinIO客户端将文件上传到指定的CloudFlare R2存储桶如果上传失败，将记录错误日志
     *                    <p>
     *                    注意：此方法不返回上传结果的直接反馈如果需要确认上传状态，应另行处理
     */
    public void uploadFile(@NotNull String bucketName, @NotNull String objectName, @NotNull InputStream inputStream)
            throws
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
        // 如果存储桶不存在，则创建
        this.createBucketIfNotExists(bucketName);

        try
        {
            // 构建上传对象的参数
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                                                       .bucket(bucketName)
                                                       .object(objectName)
                                                       .stream(inputStream, inputStream.available(), - 1)
                                                       .build();

            // 执行文件上传
            this.minioClient.putObject(putObjectArgs);

            // 记录上传成功的日志
            log.info("Uploaded file {} to CloudFlare R2 bucket {}", objectName, bucketName);
        }
        catch (MinioException minioException)
        {
            // 记录上传失败的错误日志和HTTP调用跟踪信息
            log.error("Failed to upload file to CloudFlare R2", minioException);
            log.error("Http traceback: {}", minioException.httpTrace());
        }
        catch (Exception e)
        {
            // 对于非MinioException的异常，抛出RuntimeException
            throw new RuntimeException(e);
        }
    }


    /**
     * 如果尚不存在，则创建指定名称的存储桶
     *
     * @param bucketName 存储桶名称，不能为空
     *
     * @throws ServerException           服务器异常
     * @throws InsufficientDataException 数据不足异常
     * @throws ErrorResponseException    错误响应异常
     * @throws IOException               输入输出异常
     * @throws NoSuchAlgorithmException  无此类算法异常
     * @throws InvalidKeyException       非法密钥异常
     * @throws InvalidResponseException  非法响应异常
     * @throws XmlParserException        XML解析异常
     * @throws InternalException         内部异常
     */
    private void createBucketIfNotExists(@NotNull String bucketName) throws
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
        // 检查指定名称的存储桶是否已存在
        if (this.minioClient.bucketExists(BucketExistsArgs.builder()
                                                          .bucket(bucketName)
                                                          .build()))
        {
            log.debug("Bucket {} already exists", bucketName);
        }
        else
        {
            // 创建指定名称的存储桶
            this.minioClient.makeBucket(MakeBucketArgs.builder()
                                                      .bucket(bucketName)
                                                      .build());

            log.debug("Bucket {} created", bucketName);
        }
    }
}