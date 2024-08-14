package com.ovo307000.bigevent.controller.user;

import com.ovo307000.bigevent.core.constants.enumeration.status.CategoryStatus;
import com.ovo307000.bigevent.entity.dto.CategoryDTO;
import com.ovo307000.bigevent.response.Result;
import com.ovo307000.bigevent.service.user.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

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
    public Result<?> add(@RequestBody CategoryDTO categoryDTO)
    {
        log.info("Adding category: {}", categoryDTO);

        return Objects.equals(this.userCategoryService.add(categoryDTO), CategoryStatus.SUCCESS)
               ? Result.success()
               : Result.fail();
    }
}
