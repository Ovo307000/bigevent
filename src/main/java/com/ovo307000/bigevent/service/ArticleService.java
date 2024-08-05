package com.ovo307000.bigevent.service;

import com.ovo307000.bigevent.entity.Article;
import com.ovo307000.bigevent.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("articleService")
public class ArticleService
{
    private final ArticleRepository articleRepository;

    public ArticleService(@Qualifier("articleRepository") ArticleRepository articleRepository)
    {
        this.articleRepository = articleRepository;
    }

    public List<Article> list()
    {
        return this.articleRepository.findAll();
    }
}
