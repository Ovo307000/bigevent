package com.ovo307000.bigevent.controller.user;

import com.ovo307000.bigevent.core.constants.enumeration.status.CategoryStatus;
import com.ovo307000.bigevent.entity.dto.CategoryDTO;
import com.ovo307000.bigevent.response.Result;
import com.ovo307000.bigevent.service.user.CategoryService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Validated
@RequestMapping("/user")
@RestController("userCategoryController")
public class CategoryController
{
    private static final Logger          log = LoggerFactory.getLogger(CategoryController.class);
    private final        CategoryService userCategoryService;

    public CategoryController(CategoryService userCategoryService)
    {
        this.userCategoryService = userCategoryService;
    }

    @PostMapping("/category")
    public Result<?> add(@RequestBody @Validated @NotNull CategoryDTO categoryDTO)
    {
        log.info("Adding category: {}", categoryDTO);

        return Objects.equals(this.userCategoryService.add(categoryDTO), CategoryStatus.SUCCESS)
               ? Result.success()
               : Result.fail();
    }

    @GetMapping("/category/list")
    public Result<List<CategoryDTO>> list()
    {
        log.info("Listing categories...");

        return Optional.ofNullable(this.userCategoryService.list())
                       .map(Result::success)
                       .orElse(Result.fail(CategoryStatus.FAILED, null));
    }

    @GetMapping("/category/detail")
    public Result<CategoryDTO> detail(@RequestParam @NotNull(message = "category id can not be null") Long id)
    {
        log.info("Listing category: {}", id);

        return Optional.ofNullable(this.userCategoryService.findCategoryById(id))
                       .map(Result::success)
                       .orElse(Result.fail(CategoryStatus.FAILED, null));
    }

    @PutMapping("/category")
    public Result<?> update(@RequestBody @Validated @NotNull CategoryDTO categoryDTO)
    {
        log.info("Updating category: {}", categoryDTO);

        return Objects.equals(this.userCategoryService.update(categoryDTO), CategoryStatus.SUCCESS)
               ? Result.success()
               : Result.fail();
    }
}
