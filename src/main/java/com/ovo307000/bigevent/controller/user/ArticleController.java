package com.ovo307000.bigevent.controller.user;

import com.ovo307000.bigevent.core.constants.enumeration.status.ArticleAStatus;
import com.ovo307000.bigevent.entity.dto.ArticleDTO;
import com.ovo307000.bigevent.response.Page;
import com.ovo307000.bigevent.response.Result;
import com.ovo307000.bigevent.service.user.ArticleService;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Validated
@RequestMapping("/article")
@RestController("userArticleController")
public class ArticleController
{
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService)
    {
        this.articleService = articleService;
    }

    @GetMapping()
    public Result<Page<ArticleDTO>> list(@NotNull(message = "pageNumber cannot be null") Integer pageNumber,
                                         @NotNull(message = "pageSize cannot be null") Integer pageSize,
                                         @RequestParam(required = false) Long categoryId,
                                         @RequestParam(required = false) String status)
    {
        return Optional.ofNullable(this.articleService.list(pageNumber, pageSize, categoryId, status))
                       .map(Result::success)
                       .orElse(Result.fail(ArticleAStatus.ARTICLE_NOT_FOUND, null));
    }

    @PutMapping("/add")
    public Result<ArticleDTO> add(@RequestBody @Validated(ArticleDTO.Add.class) ArticleDTO articleDTO)
    {
        return Optional.ofNullable(this.articleService.add(articleDTO))
                       .map(Result::success)
                       .orElse(Result.fail(ArticleAStatus.FAILED, null));
    }
}
