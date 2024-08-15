package com.ovo307000.bigevent.service.user;

import com.ovo307000.bigevent.entity.dto.ArticleDTO;
import com.ovo307000.bigevent.repository.user.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service("userArticleService")
public class ArticleService
{
    private static final Logger            log = LoggerFactory.getLogger(ArticleService.class);
    private final        ArticleRepository articleRepository;
    private final        UserService       userUserService;

    public ArticleService(@Qualifier("userArticleRepository") ArticleRepository articleRepository,
                          UserService userUserService)
    {
        this.articleRepository = articleRepository;
        this.userUserService   = userUserService;
    }

    public List<ArticleDTO> list()
    {
        PageRequest pageRequest = PageRequest.of(0, 10);

        return this.articleRepository.findAll(pageRequest)
                                     .getContent()
                                     .stream()
                                     .toList();
    }

    public @Nullable ArticleDTO add(ArticleDTO articleDTO)
    {
        articleDTO.setCreateTime(LocalDateTime.now());
        articleDTO.setUpdateTime(LocalDateTime.now());
        articleDTO.setCreateUser(Objects.requireNonNull(this.userUserService.findUserByThreadLocal(),
                                                        "user not found"));

        log.debug("Try to add article to database: {}", articleDTO);

        return this.articleRepository.save(articleDTO);
    }
}
