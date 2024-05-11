package com.example.big_event.Mapper;

import com.example.big_event.entry.Article;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedList;

@Mapper
public interface ArticleMapper {

    /**
     * @param article 文章实体
     * @apiNote 添加文章信息
     */
    @Insert("INSERT INTO article(title, content, coverImg, status, categoryId, creator, createTime, updateTime) VALUES(#{title}, #{content}, #{coverImg}, #{status}, #{categoryId}, #{creator}, now(),now())")
    void addArticle(Article article);

    /**
     * @param title      文章标题
     * @param categoryId 文章分类id
     * @param creator    文章创建者id
     * @return 如果可以找到指定的文章信息，返回文章实体，否则返回null值
     * @apiNote 查找指定的文章信息
     */
    @Select("SELECT * FROM article WHERE title = #{title} AND categoryId = #{categoryId} AND creator = #{creator}")
    Article getArticle(String title, Integer categoryId, Integer creator);

    /**
     * @apiNote 根据创建者id查找该用户所有的文章记录
     * @param creator 创建者id
     * @param limit 最大记录数
     * @param offset 偏移
     * @return 如果存在文章记录，返回这些文章记录，否则返回null
     */
    @Select("SELECT * FROM article WHERE creator = #{creator} LIMIT #{limit} OFFSET #{offset}")
    LinkedList<Article> getAllArticleList(Integer creator,Integer limit,Integer offset);

    /**
     * @apiNote 根据创建者id和文章发布状态查找该用户所有符合条件的文章记录
     * @param creator 创建者id
     * @param status 文章发布状态
     * @param limit 最大记录数
     * @param offset 偏移
     * @return 如果存在文章记录，返回这些文章记录，否则返回null
     */
    @Select("SELECT * FROM article WHERE creator = #{creator} AND status = #{status} LIMIT #{limit} OFFSET #{offset}")
    LinkedList<Article> getAllArticleListDesignationStatus(Integer creator, String status,Integer limit,Integer offset);

    /**
     * @apiNote 根据创建者id和文章分类id查找查找该用户所有符合条件的文章记录
     * @param creator 创建者id
     * @param categoryId 文章分类id
     * @param limit 最大记录数
     * @param offset 偏移
     * @return 如果存在文章记录，返回这些文章记录，否则返回null
     */
    @Select("SELECT * FROM article WHERE creator = #{creator} AND categoryId = #{categoryId} LIMIT #{limit} OFFSET #{offset}")
    LinkedList<Article> getAllArticleListDesignationCategoryId(Integer creator, Integer categoryId,Integer limit,Integer offset);

    /**
     * @apiNote 根据创建者id、文章分类id和文章发布状态查找该用户所有符合条件的文章记录
     * @param creator 创建者id
     * @param categoryId 文章分类id
     * @param status 文章发布状态
     * @param limit 最大记录数
     * @param offset 偏移
     * @return 如果存在文章记录，返回这些文章记录，否则返回null
     */
    @Select("SELECT * FROM article WHERE creator = #{creator} AND categoryId = #{categoryId} AND status = #{status} LIMIT #{limit} OFFSET #{offset}")
    LinkedList<Article> getAllArticleListDesignationStatusAndCategoryId(Integer creator, Integer categoryId, String status,Integer limit,Integer offset);
}
