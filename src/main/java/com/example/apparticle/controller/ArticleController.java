package com.example.apparticle.controller;

import com.example.apparticle.entity.Article;
import com.example.apparticle.payload.ApiResponse;
import com.example.apparticle.payload.ArticleDto;
import com.example.apparticle.service.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleServiceImpl articleServiceImpl;

    @GetMapping("/page/{pageNo}")
    public HttpEntity<?> findPaginated(@PathVariable (value = "pageNo") int pageNo){
        int pageSize = 5;
        Page<Article> page = articleServiceImpl.findPaginated(pageNo, pageSize);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping()
    public HttpEntity<?> getAll(){
        List<Article> allArticles = articleServiceImpl.getAllArticles();
        return ResponseEntity.ok(allArticles);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable Long id){
        Article article = articleServiceImpl.getArticle(id);
        return ResponseEntity.status(article != null ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(article);
    }

    @PostMapping
    public HttpEntity<?> addPost(@RequestBody ArticleDto articleDto){
        ApiResponse apiResponse = articleServiceImpl.addArticle(articleDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deletePost(@PathVariable Long id){
        ApiResponse apiResponse = articleServiceImpl.deleteArticle(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }
}
