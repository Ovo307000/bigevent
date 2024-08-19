package com.ovo307000.bigevent.controller.user;

import com.ovo307000.bigevent.core.constants.enumeration.status.FileStatus;
import com.ovo307000.bigevent.response.Result;
import com.ovo307000.bigevent.service.user.FileService;
import io.minio.ObjectWriteResponse;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController("userFileController")
@RequestMapping()
public class FileController
{
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;

    public FileController(FileService fileService)
    {
        this.fileService = fileService;
    }

    /**
     * 处理HTTP POST请求以上传文件。
     *
     * @param files 一个MultipartFile类型的列表，包含用户上传的所有文件
     *
     * @return 返回一个Result对象，其中包含了上传状态和可能的响应数据
     *
     * @throws ServerException           当服务器内部发生错误时抛出
     * @throws InsufficientDataException 当提供的数据不足以完成操作时抛出
     * @throws ErrorResponseException    当从服务端收到错误响应时抛出
     * @throws IOException               当发生输入输出错误时抛出
     * @throws NoSuchAlgorithmException  当找不到指定的加密算法时抛出
     * @throws InvalidKeyException       当提供的密钥无效时抛出
     * @throws InvalidResponseException  当响应数据格式不正确时抛出
     * @throws XmlParserException        当XML解析过程中出现错误时抛出
     * @throws InternalException         当发生未知的内部错误时抛出
     */
    @PostMapping
    public Result<?> upload(List<MultipartFile> files) throws
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
        log.info("Upload files: {}", files);

        if (files.isEmpty())
        {
            // 如果没有文件被上传，则返回上传失败的结果
            return Result.fail(FileStatus.UPLOAD_FAILURE);
        }
        else if (files.size() > 1)
        {
            // 如果有多个文件，则异步上传所有文件，并返回所有文件的上传响应
            List<ObjectWriteResponse> objectWriteResponses = this.fileService.uploadAsync(files);

            return Result.success(FileStatus.UPLOAD_SUCCESS, objectWriteResponses);
        }
        else
        {
            // 如果只有一个文件，则直接上传该文件，并返回单个文件的上传响应
            return Result.success(FileStatus.UPLOAD_SUCCESS, this.fileService.upload(files.getFirst()));
        }
    }
}
