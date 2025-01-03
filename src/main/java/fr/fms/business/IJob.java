package fr.fms.business;

import fr.fms.entities.Article;
import fr.fms.entities.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IJob {
    public Page<Article> getArticles(int page, String keyword, Long categoryId);

    public List<Category> getAllCategories();

    public void saveArticle(Article article);

    public void deleteArticle(Long id);

    public Optional<Article> getArticleById(Long id);

    public void addArticleToCart(Article article);

    public List<Article> getArticlesInTheCart();

    public void removeArticleFromCart(Long id);
}
