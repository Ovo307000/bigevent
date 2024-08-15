package com.ovo307000.bigevent.service.user;

import com.ovo307000.bigevent.core.constants.enumeration.status.ArticleAStatus;
import com.ovo307000.bigevent.entity.dto.ArticleDTO;
import com.ovo307000.bigevent.entity.dto.UserDTO;
import com.ovo307000.bigevent.repository.user.ArticleRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    /**
     * 根据类别ID和状态分页列出文章
     * 此方法允许管理员按类别和状态查看文章列表，如果不提供类别ID，则返回所有指定状态的文章
     *
     * @param pageNumber 页码，不能为空
     * @param pageSize   每页大小，不能为空
     * @param categoryId 类别ID，可选
     * @param status     文章状态，比如已发布、草稿等
     *
     * @return 返回一个包含文章DTO的分页对象，如果找不到符合条件的文章，可能返回null
     */
    public @Nullable com.ovo307000.bigevent.response.Page<ArticleDTO> list(
            @NotNull(message = "pageNumber cannot be null") Integer pageNumber,
            @NotNull(message = "pageSize cannot be null") Integer pageSize,
            Long categoryId,
            String status)
    {
        // TODO: 当 Status 为 null 时，应该返回所有状态的文章

        if (pageNumber < 0 || pageSize < 0)
        {
            throw new IllegalArgumentException("pageNumber and pageSize must be positive integers");
        }

        // 从线程本地获取当前用户，如果找不到则抛出异常
        UserDTO                                          user = Objects.requireNonNull(this.userUserService.findUserByThreadLocal(),
                                                                                       "user not found");
        com.ovo307000.bigevent.response.Page<ArticleDTO> page = new com.ovo307000.bigevent.response.Page<>();

        // 当未指定类别ID且文章状态为已发布时，执行此分支
        if (categoryId == null && (status.equals(ArticleAStatus.PUBLISHED)))
        {
            // 调用仓库方法查询指定状态的所有文章
            Page<ArticleDTO> articlePage;
            articlePage = this.articleRepository.findAllByCreateUserAndState(user,
                                                                             status,
                                                                             PageRequest.of(pageNumber, pageSize));

            page.setElements(articlePage.getContent());
            page.setPageNumber(articlePage.getTotalElements());

        }
        else
        {
            // 当指定了类别ID时，执行此分支
            Page<ArticleDTO> articlePageWithCategoryId;
            articlePageWithCategoryId = this.articleRepository.findAllByCreateUserAndCategoryIdAndState(user,
                                                                                                        categoryId,
                                                                                                        status,
                                                                                                        PageRequest.of(
                                                                                                                pageNumber,
                                                                                                                pageSize));

            page.setElements(articlePageWithCategoryId.getContent());
            page.setPageNumber(articlePageWithCategoryId.getTotalElements());

        }

        return page;
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
