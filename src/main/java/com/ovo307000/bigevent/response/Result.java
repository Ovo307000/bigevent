package com.ovo307000.bigevent.response;

import java.util.Objects;

public class Result<T>
{
    private String message;
    private int    code;
    private T      data;

    public Result()
    {
    }

    public Result(int code, String message, T data)
    {
        this.code    = code;
        this.message = message;
        this.data    = data;
    }

    public static Result<?> success()
    {
        return new Result<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> success(T data)
    {
        return new Result<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage(), data);
    }

    public static Result<?> success(String message)
    {
        return new Result<>(Code.SUCCESS.getCode(), message, null);
    }

    public static <T> Result<T> success(String message, T data)
    {
        return new Result<>(Code.SUCCESS.getCode(), message, data);
    }

    public static Result<?> fail()
    {
        return new Result<>(Code.FAIL.getCode(), Code.FAIL.getMessage(), null);
    }

    public static <T> Result<T> fail(T data)
    {
        return new Result<>(Code.FAIL.getCode(), Code.FAIL.getMessage(), data);
    }

    public static Result<?> fail(String message)
    {
        return new Result<>(Code.FAIL.getCode(), message, null);
    }

    public static <T> Result<T> fail(String message, T data)
    {
        return new Result<>(Code.FAIL.getCode(), message, data);
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.message, this.code, this.data);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }
        Result<?> result = (Result<?>) o;
        return this.code == result.code &&
               Objects.equals(this.message, result.message) &&
               Objects.equals(this.data, result.data);
    }

    public int getCode()
    {
        return this.code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public T getData()
    {
        return this.data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    enum Code
    {
        SUCCESS(0, "success"),
        FAIL(1, "fail");

        private final int    code;
        private final String message;

        Code(int code, String message)
        {
            this.code    = code;
            this.message = message;
        }

        public int getCode()
        {
            return this.code;
        }

        public String getMessage()
        {
            return this.message;
        }
    }
}
