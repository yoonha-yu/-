package org.example.article;

import lombok.RequiredArgsConstructor;
import org.example.user.SiteUser;
import org.example.user.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public void create(String title, String content, Principal principal) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setCreateDate(LocalDateTime.now());
        Optional<SiteUser> siteUser = userRepository.findByUsername(principal.getName());
        if (siteUser.isPresent()) {
            article.setAuthor(siteUser.get());
        } else {
            throw new NoSuchElementException("Author not found");
        }


        this.articleRepository.save(article);

    }

    public Article getArticle(Integer id) {
        Optional<Article> article = this.articleRepository.findById(id);
        return article.get();
    }

    public List<Article> getList() {
        return this.articleRepository.findAll();
    }
    public void modify(Article article, String title, String cotent){
        article.setTitle(title);
        article.setContent(cotent);
        article.setModifyDate(LocalDateTime.now());
        this.articleRepository.save(article);
    }
    public void delete(Article article){
        this.articleRepository.delete(article);
    }
}
