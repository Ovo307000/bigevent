package com.ovo307000.bigevent.core.handler;

import com.ovo307000.bigevent.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice()
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e)
    {
        log.error("An exception occurred: ", e);

        return Result.fail(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "An error occurred.");
    }
}
