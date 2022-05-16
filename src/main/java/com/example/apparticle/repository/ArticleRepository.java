package com.example.apparticle.repository;

import com.example.apparticle.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "product")
public interface ArticleRepository extends JpaRepository<Article, Long> {

//    Page<Article> findAll(int pageNo, int pageSize);
}

