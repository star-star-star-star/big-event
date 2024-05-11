package com.example.big_event.controller;

import com.example.big_event.exception.ArticleException;
import com.example.big_event.exception.PageListException;
import com.example.big_event.message.Result;
import com.example.big_event.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {

    @Resource
    ArticleService articleService;

    /**
     * @apiNote 添加文章信息
     * @param pic 文章封面
     * @param absolutePath 服务器本地存储的绝对基路径
     * @param supportedFormatRegexp 支持的文件格式（是一个正则表达式）
     * @param title 文章标题
     * @param content 文章内容
     * @param status 文章状态
     * @param categoryName 文章分类名称
     * @return 响应结果
     */
    @PostMapping("/addArticle")
    public Result addArticle(
            @RequestParam("pic") MultipartFile pic,
            @Value("${upload.absolute-path}") String absolutePath,
            @Value("${upload.supported-format-regexp}") String supportedFormatRegexp,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("status") String status,
            @RequestParam("categoryName") String categoryName
    ) {
        return articleService.addArticle(pic,absolutePath,supportedFormatRegexp,title,content,status,categoryName);
    }


    /**
     * @param pageNum      当前页数
     * @param pageSize     每一页的最大记录数
     * @param categoryName 文章分类名称
     * @param status       文章发布状态
     * @return 响应结果
     * @apiNote 根据文章分类名称（非必须）和文章发布状态（非必须）查询文章实体
     */
    @PostMapping("/getArticle")
    public Result getArticle(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam(value = "categoryName", required = false) String categoryName, @RequestParam(value = "status", required = false) String status) {
        try {
            return new Result(true, "获取文章成功", articleService.getArticles(pageNum, pageSize, categoryName, status));
        } catch (ArticleException | PageListException exception) {
            return new Result(false, "获取文章失败", exception.getMessage());
        }
    }
}
