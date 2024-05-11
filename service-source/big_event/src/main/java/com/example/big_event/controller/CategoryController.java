package com.example.big_event.controller;

import com.example.big_event.exception.CategoryException;
import com.example.big_event.message.Result;
import com.example.big_event.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoryController {

    @Resource
    CategoryService categoryService;

    /**
     * @param categoryName  文章分类名称
     * @param categoryAlias 文章分类别名
     * @return 响应结果
     * @apiNote 添加文章分类
     */
    @PostMapping("/addCategory")
    public Result addCategory(@RequestParam("categoryName") String categoryName, @RequestParam("categoryAlias") String categoryAlias) {
        try {
            return new Result(true, "添加成功", categoryService.addCategory(categoryName, categoryAlias));
        } catch (CategoryException categoryException) {
            return new Result(false, "添加失败", categoryException.getMessage());
        }
    }

    /**
     * @apiNote 获取文章分类列表
     * @return 响应结果
     */
    @GetMapping("/getCategories")
    public Result getCategories(){
        return new Result(true,"获取文章分类成功",categoryService.getCategories());
    }

    /**
     * @apiNote 获取指定的文章分类信息
     * @param categoryName 文章分类名称
     * @return 响应结果
     */
    @GetMapping("/getCategory/{categoryName}")
    public Result getCategory(@PathVariable("categoryName") String categoryName){
        return new Result(true,"获取文章分类成功",categoryService.getCategoryByCategoryName(categoryName));
    }

    /**
     * @apiNote 更新文章分类信息
     * @param categoryName 新的文章分类名称
     * @param categoryAlias 新的文章分类别名
     * @return 响应结果
     */
    @PutMapping("/updateCategory/{oldCategoryName},{newCategoryName},{newCategoryAlias}")
    public Result updateCategory(@PathVariable("oldCategoryName") String oldCategoryName, @PathVariable("newCategoryName") String newCategoryName,@PathVariable("newCategoryAlias") String newCategoryAlias){
        try{
            return new Result(true,"文章分类信息更新成功",categoryService.updateCategory(oldCategoryName,newCategoryName,newCategoryAlias));
        }catch (CategoryException categoryException){
            return new Result(false,"文章分类信息更新失败",categoryException.getMessage());
        }
    }

    /**
     * @apiNote 删除用户指定的文章分类
     * @param categoryName 文章分类名称
     * @return 响应结果
     */
    @DeleteMapping("/deleteCategory/{categoryName}")
    public Result deleteCategory(@PathVariable("categoryName") String categoryName){
        System.out.println(categoryName);
        categoryService.deleteCategoryByCategoryName(categoryName);
        return new Result(true,"文章分类删除成功");
    }
}
