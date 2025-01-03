package fr.fms.business;

import fr.fms.dao.ArticleRepository;
import fr.fms.dao.CategoryRepository;
import fr.fms.entities.Article;
import fr.fms.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IJobImpl implements IJob {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private List<Article> listOfArticlesInTheCart = new ArrayList<>();

    public Page<Article> getArticles(int page, String keyword, Long categoryId) {
        if (categoryId != null) {
            return articleRepository.findByCategoryId(categoryId, PageRequest.of(page, 5));
        } else {
            return articleRepository.findByDescriptionContains(keyword, PageRequest.of(page, 5));
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void saveArticle(Article article) {
        articleRepository.save(article);
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public void addArticleToCart(Article article) {
        listOfArticlesInTheCart.add(article);
    }

    public List<Article> getArticlesInTheCart() {
        return listOfArticlesInTheCart;
    }

    public void removeArticleFromCart(Long id) {
        listOfArticlesInTheCart.removeIf(article -> article.getId().equals(id));
    }
}
