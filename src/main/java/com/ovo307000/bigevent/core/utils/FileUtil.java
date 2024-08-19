package com.ovo307000.bigevent.core.utils;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component("fileUtil")
public class FileUtil
{
    /**
     * 生成带有UUID的文件名
     *
     * @param originalFilename 原始文件名，不能为空
     *
     * @return 带有UUID和原始文件后缀的文件名
     */
    public String generateUUIDFileName(@NotNull(message = "Original filename cannot be null") String originalFilename)
    {
        // 使用UUID作为文件名的前缀，以确保文件名的唯一性
        // 后缀使用原始文件的后缀，保持文件类型的一致性
        return java.util.UUID.randomUUID() + "." + this.getFileSuffix(originalFilename);
    }

    /**
     * 获取文件的后缀名
     * <p>
     * 该方法用于提取文件的后缀名，例如从"example.txt"中提取出"txt"
     * 如果原始文件名为空或者不包含"."，则返回null，表示无法确定文件后缀名
     *
     * @param originalFilename 原始文件名，不能为null
     *
     * @return 文件后缀名，如果无法确定则返回null
     */
    public @Nullable String getFileSuffix(
            @NotNull(message = "Original filename cannot be null") String originalFilename)
    {
        // 判断原始文件名是否包含"."
        // 如果不包含，则认为没有文件后缀，返回null
        // 否则，从"."的下一个字符开始提取，直到字符串结尾，这就是文件的后缀名
        return originalFilename.lastIndexOf(".") == - 1
               ? null
               : originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }
}
