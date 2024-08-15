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
        if (id < 0)
        {
            throw new IllegalArgumentException("category id must be greater than 0");
        }

        return this.userCategoryRepository.findCategoryDTOById(id);
    }

    /**
     * 更新类别信息
     * <p>
     * 此方法主要用于更新数据库中的类别信息它首先确保传入的类别对象的ID不为null，
     * 然后设置类别的更新时间为当前时间，最后尝试将更新后的类别对象保存回数据库
     *
     * @param categoryDTO 待更新的类别数据传输对象它包含了类别信息，如名称、描述等
     *
     * @return 返回更新操作的状态，成功或失败这通过CategoryStatus枚举表示
     */
    public String update(CategoryDTO categoryDTO)
    {
        // 确保要更新的类别在数据库中存在
        Objects.requireNonNull(this.userCategoryRepository.findCategoryDTOById(categoryDTO.getId()),
                               "Category is not in database");

        // 获取当前操作用户，确保用户存在
        UserDTO user = Objects.requireNonNull(this.userUserService.findUserByThreadLocal(), "user not found");

        // 设置类别的更新时间为当前时间，以及更新操作的用户
        categoryDTO.setUpdateTime(LocalDateTime.now());
        categoryDTO.setCreateUser(user);

        // 记录调试信息，尝试更新数据库中的类别信息
        log.debug("Try to update category in database: {}", categoryDTO);

        // 保存更新后的类别对象到数据库，并返回
        return this.userCategoryRepository.updateCategoryDTO(categoryDTO) == 0
               ? CategoryStatus.FAILED
               : CategoryStatus.SUCCESS;

    }

}
