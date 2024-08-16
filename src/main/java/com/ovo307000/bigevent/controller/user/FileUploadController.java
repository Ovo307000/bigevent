package com.ovo307000.bigevent.controller.user;

import com.ovo307000.bigevent.response.Result;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("userFileUploadController")
@RequestMapping("/upload")
public class FileUploadController
{
    @PostMapping
    public Result<String> upload(@NotNull(message = "file cannot be null") MultipartFile file)
    {
        // TODO: 处理上传文件逻辑

        return null;
    }
}
