package com.ovo307000.bigevent.controller;

import com.ovo307000.bigevent.entity.Article;
import com.ovo307000.bigevent.core.constants.enumeration.status.ArticleAStatus;
import com.ovo307000.bigevent.response.Result;
import com.ovo307000.bigevent.service.ArticleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/article")
@RestController("articleController")
public class ArticleController
{
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService)
    {
        this.articleService = articleService;
    }

    @GetMapping("/list")
    public Result<?> list()
    {
        return Optional.ofNullable(this.articleService.list())
                       .filter((List<Article> articles) -> ! articles.isEmpty())
                       .map(Result::success)
                       .orElse(Result.fail(ArticleAStatus.ARTICLE_NOT_FOUND.getMessage(), null));
    }
}
