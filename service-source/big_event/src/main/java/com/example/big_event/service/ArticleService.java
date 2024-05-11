package com.example.big_event.service;

import com.example.big_event.DTO.TransferArticleParameter;
import com.example.big_event.Mapper.ArticleMapper;
import com.example.big_event.dao.PageList;
import com.example.big_event.entry.Article;
import com.example.big_event.entry.User;
import com.example.big_event.exception.ArticleException;
import com.example.big_event.exception.PageListException;
import com.example.big_event.message.Result;
import com.example.big_event.util.ThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ArticleService {

    @Resource
    CategoryService categoryService;

    @Resource
    ArticleMapper articleMapper;

    @Value("${upload.absolute-path}")
    String absolutePath;

    /**
     * @param pic 文章封面
     * @param absolutePath 服务器本地存储的绝对基路径
     * @param supportedFormatRegexp 支持的文件格式（是一个正则表达式）
     * @param title 文章标题
     * @param content 文章内容
     * @param status 文章状态
     * @param categoryName 文章分类名称
     * @return 添加的文章实体信息
     * @apiNote 添加文章信息
     */
    public Result addArticle(
            MultipartFile pic,
            String absolutePath,
            String supportedFormatRegexp,
            String title,
            String content,
            String status,
            String categoryName
    ){
        try{
            //验证文章分类
            this.verifyCategory(categoryName);
            //验证文章标题
            this.verifyTitle(title, categoryName);
            //验证文章内容
            this.verifyContent(content);
            //验证文章状态
            this.verifyStatus(status);
            //验证文件类型
            String originalFilename = pic.getOriginalFilename();
            Pattern r = Pattern.compile(supportedFormatRegexp);
            Matcher m = r.matcher(originalFilename);
            if (!m.find()) {
                return new Result(false, "不支持的文件类型",originalFilename);
            }
            //为每一个用户单独创建一个存储文件夹
            String UID = (((User) ((Result) (ThreadLocal.getObject())).getData()).getId()).toString();
            String relativePath = UID + "\\article-cover";   //用户文件的相对基路径
            String dirPath = absolutePath + relativePath;
            File file = new File(dirPath);
            //如果文件路径不存在，那么创建它
            if (!file.exists()) {
                //file.mkdir();
                file.mkdirs();
            }
            String filePath = dirPath + "\\" + originalFilename;
            pic.transferTo(new File(filePath));
            Article article = new Article();
            article.setCreator(((User) ((Result) (ThreadLocal.getObject())).getData()).getId());
            article.setCategoryId(categoryService.getCategoryByCategoryName(categoryName).getId());
            article.setContent(content);
            article.setCoverImg(relativePath + "\\" + originalFilename);
            article.setStatus(status);
            article.setTitle(title);
            articleMapper.addArticle(article);
            return new Result(true, "文章上传成功", file);
        } catch (IOException e) {
            return new Result(false, "文件路径错误", e.getMessage());
        } catch (MaxUploadSizeExceededException maxUploadSizeExceededException) {
            return new Result(false, "上传文件太大，最大允许1GB的文件", maxUploadSizeExceededException.getMessage());
        }catch (NullPointerException nullPointerException){
            return new Result(false,"图片不能为空",nullPointerException.getMessage());
        }catch (ArticleException articleException){
            return new Result(false,articleException.getMessage());
        }
    }

    /**
     * @param title        文章标题
     * @param categoryName 文章分类名称
     * @throws ArticleException 当文章标题不符合规则时，抛出该异常
     * @apiNote 验证文章标题是否符合规则
     */
    private void verifyTitle(String title, String categoryName) throws ArticleException {
        if (title == null || title.length() == 0) {
            throw new ArticleException("文章标题不能为空");
        }
        if (title.length() > 100) {
            throw new ArticleException("文章标题长度超出限制，限制为100字符");
        }
        if (articleMapper.getArticle(title, categoryService.getCategoryByCategoryName(categoryName).getId(), ((User) ((Result) (ThreadLocal.getObject())).getData()).getId()) != null) {
            throw new ArticleException("不允许存在文章分类相同且文章标题相同的文章");
        }
    }

    /**
     * @param content 文章内容
     * @throws ArticleException 当文章内容不符合规则时，抛出该异常
     * @apiNote 验证文章内容是否符合规则
     */
    private void verifyContent(String content) throws ArticleException {
        if (content == null || content.length() == 0) {
            throw new ArticleException("文章内容不能为空");
        }
    }

    /**
     * @param cover 文章封面链接
     * @throws ArticleException 当文章封面链接不符合规则时，抛出该异常
     * @apiNote 验证文章封面链接是否符合规则
     * @deprecated
     */
    private void verifyCoverImg(String cover) throws ArticleException {
        String pattern = "^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(cover);
        if (!m.matches()) {
            throw new ArticleException("文章封面链接不符合规则");
        }
    }

    /**
     * @param status 文章状态
     * @throws ArticleException 当文章状态不符合规则时，抛出该异常
     * @apiNote 验证文章状态是否符合规则
     */
    private void verifyStatus(String status) throws ArticleException {
        if (status != null && !status.equals("已发布") && !status.equals("草稿")) {
            throw new ArticleException("文章状态错误，只能为‘草稿’或‘已发布’状态");
        }
    }

    /**
     * @param category 文章分类名称
     * @throws ArticleException 当文章分类不存在或文章分类名称不符合规则时，抛出该异常
     * @apiNote 验证文章分类是否符合规则
     */
    private void verifyCategory(String category) throws ArticleException {
        if (category == null) {
            throw new ArticleException("文章分类不能为空");
        }
        if (category.length() <= 0 || category.length() >= 21) {
            throw new ArticleException("文章分类名称长度不符合规则");
        }
        if (categoryService.getCategoryByCategoryName(category) == null) {
            throw new ArticleException("不存在该文章分类");
        }
    }

    /**
     * @param pageNum      当前页码
     * @param pageSize     每一页最大的记录数
     * @param categoryName 文章分类名称
     * @param status       文章发布状态
     * @return 分页结果
     * @throws ArticleException  如果文章分类名称不符合规则、文章发布状态不符合规则，则抛出该异常
     * @throws PageListException 如果当前页码不为正数、每一页最大的记录数不为正数，则抛出该异常
     * @apiNote 根据文章分类名称（可选）和文章发布状态（可选）查找文章信息
     */
    public PageList getArticles(Integer pageNum, Integer pageSize, String categoryName, String status) throws ArticleException, PageListException {
        /*
            验证参数
         */
        if (pageNum <= 0) {
            throw new PageListException("当前页码错误，不是一个正整数");
        }
        if (pageSize <= 0) {
            throw new PageListException("每一页最大记录条数错误，不是一个正整数");
        }
        if (categoryName != null && (categoryName.length() <= 0 || categoryName.length() >= 21)) {
            throw new ArticleException("文章分类名称长度不符合规则");
        }
        if (categoryName != null && categoryService.getCategoryByCategoryName(categoryName) == null) {
            throw new ArticleException("不存在该文章分类");
        }
        this.verifyStatus(status);

        /*
            分页查询
         */
        //PageHelper.startPage(pageNum, pageSize);
        LinkedList<Article> articleList;
        if (categoryName == null && status == null) {
            articleList = articleMapper.getAllArticleList(((User) ((Result) (ThreadLocal.getObject())).getData()).getId(),pageSize,(pageNum-1)*pageSize);
        } else if (categoryName == null && status != null) {
            articleList = articleMapper.getAllArticleListDesignationStatus(((User) ((Result) (ThreadLocal.getObject())).getData()).getId(), status,pageSize,(pageNum-1)*pageSize);
        } else if (categoryName != null && status == null) {
            articleList = articleMapper.getAllArticleListDesignationCategoryId(((User) ((Result) (ThreadLocal.getObject())).getData()).getId(), categoryService.getCategoryByCategoryName(categoryName).getId(),pageSize,(pageNum-1)*pageSize);
        } else {
            articleList = articleMapper.getAllArticleListDesignationStatusAndCategoryId(((User) ((Result) (ThreadLocal.getObject())).getData()).getId(), categoryService.getCategoryByCategoryName(categoryName).getId(), status,pageSize,(pageNum-1)*pageSize);
        }
        List<TransferArticleParameter> transferArticleParameterList = new LinkedList<>();
        for(Article article : articleList){
            article.setCoverImg(absolutePath+article.getCoverImg());
            transferArticleParameterList.add(new TransferArticleParameter(article, categoryService.getCategoryByCategoryId(article.getCategoryId()).getCategoryName()));
        }
        //Page<TransferArticleParameter> page = (Page<TransferArticleParameter>) transferArticleParameterList;

        return new PageList(transferArticleParameterList.size(), transferArticleParameterList);
    }
}
