package com.ovo307000.bigevent.service.user;

import com.ovo307000.bigevent.entity.dto.ArticleDTO;
import com.ovo307000.bigevent.repository.user.ArticleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userArticleService")
public class ArticleService
{
    private final ArticleRepository articleRepository;

    public ArticleService(@Qualifier("userArticleRepository") ArticleRepository articleRepository)
    {
        this.articleRepository = articleRepository;
    }

    public List<ArticleDTO> list()
    {
        return this.articleRepository.findAll();
    }
}
