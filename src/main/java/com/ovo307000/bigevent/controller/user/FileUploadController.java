package com.ovo307000.bigevent.controller.user;

import com.ovo307000.bigevent.response.Result;
import com.ovo307000.bigevent.service.user.FileUploadService;
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

@RestController("userFileUploadController")
@RequestMapping("/upload")
public class FileUploadController
{
    private static final Logger            log = LoggerFactory.getLogger(FileUploadController.class);
    private final        FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService)
    {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping
    public Result<?> upload(MultipartFile file) throws
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
        log.info("Try to upload file: {}", file.getOriginalFilename());

        this.fileUploadService.upload(file);

        return Result.success("Upload success");
    }
}
