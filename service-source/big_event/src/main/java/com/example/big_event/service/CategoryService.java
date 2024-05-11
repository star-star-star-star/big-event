package com.example.big_event.service;

import com.example.big_event.Mapper.CategoryMapper;
import com.example.big_event.entry.Category;
import com.example.big_event.entry.User;
import com.example.big_event.exception.CategoryException;
import com.example.big_event.message.Result;
import com.example.big_event.util.ThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;

@Service
public class CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    /**
     * @param categoryName  文章分类名称
     * @param categoryAlias 文章分类别名
     * @return 添加的文章分类信息
     * @throws CategoryException 添加失败时抛出该异常
     * @apiNote 添加文章分类
     */
    public Category addCategory(String categoryName, String categoryAlias) throws CategoryException {
        //校验文章分类名称以及文章分类别名
        this.verifyCategory(categoryName, categoryAlias);

        //添加文章分类
        categoryMapper.addCategory(categoryName, categoryAlias, ((User) ((Result) (ThreadLocal.getObject())).getData()).getId());

        //返回添加的文章分类信息
        return this.getCategoryByCategoryName(categoryName);
    }

    /**
     * @param categoryName  文章分类名称
     * @param categoryAlias 文章分类别名
     * @throws CategoryException 如果文章分类名称不符合规则、文章分类别名不符合规则、文章分类名称已经存在，抛出该异常
     * @apiNote 检查文章分类名称以及文章分类别名是否符合规则
     */
    private void verifyCategory(String categoryName, String categoryAlias) throws CategoryException {
        /*
            检查文章分类名称以及文章分类别名是否符合规则
         */

        if (categoryName == null || categoryName.length() == 0) {
            throw new CategoryException("文章分类名称不能为空");
        }
        if (categoryName.length() < 0 || categoryName.length() >= 21) {
            throw new CategoryException("文章分类名称长度不符合规则");
        }
        if (categoryAlias == null || categoryAlias.length() == 0) {
            throw new CategoryException("文章分类别名不能为空");
        }
        if (categoryAlias.length() < 0 || categoryAlias.length() >= 21) {
            throw new CategoryException("文章分类别名长度不符合规则");
        }

        /*
            检查文章分类名称是否重复
         */
        if (this.getCategoryByCategoryName(categoryName) != null) {
            throw new CategoryException("该文章分类已经存在");
        }
    }

    /**
     * @param categoryName 文章分类名称
     * @return 如果可以找到对应的文章分类信息，则返回文章分类实体，否则返回null
     * @apiNote 根据文章分类名称和创建者查找指定的文章分类信息
     */
    public Category getCategoryByCategoryName(String categoryName) {
        return categoryMapper.findCategoryByCreator(categoryName, ((User) ((Result) (ThreadLocal.getObject())).getData()).getId());
    }

    /**
     * @apiNote 获取指定用户的文章分类信息
     * @return 如果用户添加了文章分类，则返回这些文章分类信息，否则返回null
     */
    public LinkedHashSet<Category> getCategories(){
        return categoryMapper.getCategories(((User)((Result)(ThreadLocal.getObject())).getData()).getId());
    }

    /**
     * @apiNote 更新文章分类名称以及文章分类别名
     * @param oldCategoryName 原来的文章分类名称
     * @param newCategoryName 新的文章分类名称
     * @param newCategoryAlias 新的文章分类别名
     * @return 更新后的文章分类信息
     * @throws CategoryException 如果文章分类名称不符合规则、文章分类别名不符合规则、文章分类名称已经存在，则抛出该异常
     */
    public Category updateCategory(String oldCategoryName, String newCategoryName, String newCategoryAlias) throws CategoryException {
        //验证文章分类名称以及文章分类别名
        this.verifyCategory(newCategoryName, newCategoryAlias);

        //更新文章分类信息
        categoryMapper.updateCategory(oldCategoryName, newCategoryName, newCategoryAlias,((User)((Result)(ThreadLocal.getObject())).getData()).getId());

        //返回更新后的文章分类信息
        return this.getCategoryByCategoryName(newCategoryName);
    }

    /**
     * @apiNote 删除当前用户指定的文章分类
     * @param categoryName 文章分类名称
     */
    public void deleteCategoryByCategoryName(String categoryName){
        categoryMapper.deleteCategory(categoryName,((User)((Result)(ThreadLocal.getObject())).getData()).getId());
    }

    /**
     * @apiNote 根据文章分类id获取文章分类
     * @param categoryId 文章分类id
     * @return 如果存在对应的文章分类，返回文章分类实体。否则返回null
     */
    public Category getCategoryByCategoryId(Integer categoryId){
        return categoryMapper.getCategoryById(categoryId);
    }
}
