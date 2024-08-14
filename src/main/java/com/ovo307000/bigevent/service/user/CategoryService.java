package com.ovo307000.bigevent.service.user;

import com.ovo307000.bigevent.core.constants.enumeration.status.CategoryStatus;
import com.ovo307000.bigevent.entity.dto.CategoryDTO;
import com.ovo307000.bigevent.entity.dto.UserDTO;
import com.ovo307000.bigevent.repository.user.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("userCategoryService")
public class CategoryService
{
    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final UserService        userUserService;
    private final CategoryRepository userCategoryRepository;

    public CategoryService(UserService userUserService, CategoryRepository userCategoryRepository)
    {
        this.userUserService        = userUserService;
        this.userCategoryRepository = userCategoryRepository;
    }

    public String add(CategoryDTO categoryDTO)
    {
        UserDTO user = Objects.requireNonNull(this.userUserService.findUserByThreadLocal(), "user not found");

        categoryDTO.setCreateTime(LocalDateTime.now());
        categoryDTO.setUpdateTime(LocalDateTime.now());
        categoryDTO.setCreateUser(user);

        log.debug("Try to add category to database: {}", categoryDTO);

        return Optional.of(this.userCategoryRepository.save(categoryDTO))
                       .map((CategoryDTO category) -> CategoryStatus.SUCCESS)
                       .orElse(CategoryStatus.FAILED);
    }

    public List<CategoryDTO> list()
    {
        UserDTO user = Objects.requireNonNull(this.userUserService.findUserByThreadLocal(), "user not found");

        return this.userCategoryRepository.findCategoryDTOByCreateUser(user);
    }

    public @Nullable CategoryDTO findCategoryById(Long id)
    {
        Objects.requireNonNull(id, "id cannot be null");

        return this.userCategoryRepository.findCategoryDTOById(id);
    }
}
