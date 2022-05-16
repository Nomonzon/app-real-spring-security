package com.example.apparticle.service;

import com.example.apparticle.entity.Article;
import com.example.apparticle.entity.User;
import com.example.apparticle.payload.ApiResponse;
import com.example.apparticle.payload.ArticleDto;
import com.example.apparticle.repository.ArticleRepository;
import com.example.apparticle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     *
     * @return
     */
    public List<Article> getAllArticles(){
        return articleRepository.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Article getArticle(Long id){
        return articleRepository.findById(id).orElse(null);
    }

    /**
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<Article> findPaginated(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo -1, pageSize);
        return articleRepository.findAll(pageable);


    }

    /**
     *
     * @param articleDto
     * @return
     */
    public ApiResponse addArticle(ArticleDto articleDto){
        Optional<User> userOptional = userRepository.findById(articleDto.getUserId());
        if (userOptional.isEmpty()){
            return new ApiResponse("Error! User is not found", false);
        }
        Article article = new Article();
        article.setArticle(articleDto.getArticle());
        article.setUser(userOptional.get());
        articleRepository.save(article);
        return new ApiResponse("Success article is successfully added.", true);
    }

    public ApiResponse deleteArticle(Long id){
        try {
            articleRepository.deleteById(id);
            return new ApiResponse("Article deleted.", true);
        }
        catch (Exception e){
            return new ApiResponse("Error article has not deleted.",false);
        }
    }
}
