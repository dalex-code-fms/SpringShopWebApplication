package fr.fms.web;

import fr.fms.business.IJobImpl;
import fr.fms.dao.ArticleRepository;
import fr.fms.dao.CategoryRepository;
import fr.fms.entities.Article;
import fr.fms.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ArticleController {
    private static final String REDIRECT_INDEX = "redirect:/index";
    private static final String ARTICLE = "article";

    @Autowired
    IJobImpl iJob;

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "keyword", defaultValue = "") String keyword,
                        @RequestParam(name = "categoryId", defaultValue = "") Long categoryId) {

        Page<Article> articles = iJob.getArticles(page, keyword, categoryId);
        List<Category> categories = iJob.getAllCategories();

        model.addAttribute("listArticles", articles.getContent());
        model.addAttribute("listCategories", categories);
        model.addAttribute("numberOfPages", new int[articles.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);

        return "articles";
    }

    @GetMapping("/article")
    public String article(Model model) {
        List<Category> listOfCategories = iJob.getAllCategories();
        model.addAttribute(ARTICLE, new Article());
        model.addAttribute("listOfCategories", listOfCategories);

        return ARTICLE;
    }

    @PostMapping("/save")
    public String save(Model model, @Valid Article article, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ARTICLE;

        iJob.saveArticle(article);
        return REDIRECT_INDEX;
    }

    @GetMapping("/delete")
    public String delete(Long id, int page, String keyword) {
        iJob.deleteArticle(id);

        return "redirect:/index?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/modify")
    public String modify(Model model, Long id) {
        Optional<Article> article = iJob.getArticleById(id);

        if (article.isPresent()) {

            List<Category> listOfCategories = iJob.getAllCategories();

            model.addAttribute(ARTICLE, article.get());
            model.addAttribute("listOfCategories", listOfCategories);

            return "modifyArticle";
        } else {
            return REDIRECT_INDEX;
        }
    }

    @GetMapping("/addToCart")
    public String addToCart(Model model, Long id) {
        Optional<Article> article = iJob.getArticleById(id);

        article.ifPresent(iJob::addArticleToCart);

        return REDIRECT_INDEX;
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        model.addAttribute("listOfArticles", iJob.getArticlesInTheCart());
        return "cart";
    }

    @GetMapping("/deleteFromCart")
    public String deleteFromCart(Model model, Long id) {

        iJob.removeArticleFromCart(id);

        model.addAttribute("listOfArticles", iJob.getArticlesInTheCart());
        return "cart";
    }

    @GetMapping("/403")
    public String error() {
        return "403";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logoutConfirm")
    public String logoutConfirm() {
        return "logoutConfirm";
    }
}
