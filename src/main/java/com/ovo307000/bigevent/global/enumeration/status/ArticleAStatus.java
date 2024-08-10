package com.ovo307000.bigevent.global.enumeration.status;

public enum ArticleAStatus implements Status
{
    ARTICLE_NOT_FOUND("Article not found", 1010);

    private final String  message;
    private final Integer code;

    ArticleAStatus(String message, Integer code)
    {
        this.message = message;
        this.code    = code;
    }

    @Override
    public Integer getCode()
    {
        return this.code;
    }

    @Override
    public String getMessage()
    {
        return this.message;
    }
}
