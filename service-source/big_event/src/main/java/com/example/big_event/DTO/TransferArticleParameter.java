package com.example.big_event.DTO;

import com.example.big_event.entry.Article;

public class TransferArticleParameter {
    private Article article;
    private String categoryName;

    public TransferArticleParameter() {
    }

    public TransferArticleParameter(Article article, String categoryName) {
        this.article = article;
        this.categoryName = categoryName;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
