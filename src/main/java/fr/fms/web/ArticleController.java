package fr.fms.web;

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
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CategoryRepository categoryRepository;

    List<Article> listOfArticlesInTheCart = new ArrayList<>();


    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "keyword", defaultValue = "") String keyword,
                        @RequestParam(name = "categoryId", defaultValue = "") Long categoryId) {

        Page<Article> articles;
        if (categoryId != null) {
            articles = articleRepository.findByCategoryId(categoryId, PageRequest.of(page, 5));
        } else {
            articles = articleRepository.findByDescriptionContains(keyword, PageRequest.of(page, 5));
        }

        List<Category> categories = categoryRepository.findAll();
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
        List<Category> listOfCategories = categoryRepository.findAll();
        model.addAttribute("article", new Article());
        model.addAttribute("listOfCategories", listOfCategories);

        return "article";
    }

    @PostMapping("/save")
    public String save(Model model, @Valid Article article, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "article";
        articleRepository.save(article);
        return "redirect:/index";
    }

    @GetMapping("/delete")
    public String delete(Long id, int page, String keyword) {
        articleRepository.deleteById(id);

        return "redirect:/index?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/modify")
    public String modify(Model model, Long id) {
        Optional<Article> article = articleRepository.findById(id);

        List<Category> listOfCategories = categoryRepository.findAll();
        if (article.isPresent()) {
            model.addAttribute("article", article.get());
            model.addAttribute("listOfCategories", listOfCategories);

            return "modifyArticle";
        } else {
            return "redirect:/index";
        }
    }

    @PostMapping("/modify")
    public String modify(Model model, @Valid Article article, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "article";
        articleRepository.save(article);
        return "redirect:/index";
    }

    @GetMapping("/addToCart")
    public String addToCart(Model model, Long id) {
        Optional<Article> article = articleRepository.findById(id);

        if (article.isPresent()) {
            listOfArticlesInTheCart.add(article.get());
        }

        return "redirect:/index";
    }

    @GetMapping("/cart")
    public String cart(Model model) {

        listOfArticlesInTheCart.forEach(System.out::println);
        model.addAttribute("listOfArticles", listOfArticlesInTheCart);
        return "cart";
    }

    @GetMapping("/deleteFromCart")
    public String deleteFromCart(Model model, Long id) {

        boolean removed = listOfArticlesInTheCart.removeIf(article -> article.getId().equals(id));

        if (removed) {
            System.out.println("L'article à bien été supprimé");
        } else {
            System.out.println("L'article n'a pas été supprimé");
        }
        model.addAttribute("listOfArticles", listOfArticlesInTheCart);
        return "cart";
    }

    @GetMapping("/customerDetails")
    public String customerDetails(Model model) {

    }
}
