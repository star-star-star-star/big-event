package com.example.big_event.Mapper;

import com.example.big_event.entry.Category;
import org.apache.ibatis.annotations.*;

import java.util.LinkedHashSet;

@Mapper
public interface CategoryMapper {

    /**
     * @apiNote 按照文章分类名称和创建者查找指定的文章分类信息
     * @param categoryName 文章分类名称
     * @param creator 创建者id
     * @return 如果可以找到指定的文章分类信息，返回文章分类信息，否则返回null
     */
    @Select("SELECT * FROM category WHERE categoryName = #{categoryName} AND creator = #{creator}")
    Category findCategoryByCreator(String categoryName, Integer creator);

    /**
     * @apiNote 添加文章分类信息
     * @param categoryName 文章分类名称
     * @param categoryAlias 文章分类别名
     * @param creator 创建者id
     */
    @Insert("INSERT INTO category (categoryName, categoryAlias, creator, createTime, updateTime) VALUES (#{categoryName}, #{categoryAlias}, #{creator}, now(), now())")
    void addCategory(String categoryName, String categoryAlias, Integer creator);

    /**
     * @apiNote 查找指定用户的文章分类信息
     * @param creator 创建者id
     * @return 如果该创建者创建了文章分类，则返回这些文章分类信息，否则返回null
     */
    @Select("SELECT * FROM category WHERE creator = #{creator}")
    LinkedHashSet<Category> getCategories(Integer creator);

    /**
     * @apiNote 更新文章分类名称以及文章分类别名
     * @param oldCategoryName 旧的文章分类名称
     * @param newCategoryName 新的文章分类名称
     * @param newCategoryAlias 新的文章分类别名
     * @param creator 创建者id
     */
    @Update("UPDATE category SET categoryName = #{newCategoryName}, categoryAlias = #{newCategoryAlias} WHERE creator = #{creator} AND categoryName = #{oldCategoryName}")
    void updateCategory(String oldCategoryName, String newCategoryName, String newCategoryAlias, Integer creator);

    /**
     * @apiNote 删除指定的文章分类
     * @param categoryName 文章分类名称
     * @param creator 创建者id
     */
    @Delete("DELETE FROM category WHERE categoryName = #{categoryName} AND creator = #{creator}")
    void deleteCategory(String categoryName,Integer creator);

    /**
     * @apiNote 根据文章分类id查找文章分类信息
     * @param categoryId 文章分类id
     * @return 如果存在对应的文章分类，则返回该文章分类实体，否则返回null
     */
    @Select("SELECT * FROM category WHERE id = #{categoryId}")
    Category getCategoryById(Integer categoryId);
}
