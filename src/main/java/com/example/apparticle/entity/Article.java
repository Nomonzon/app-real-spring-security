package com.example.apparticle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10_000)
    private String article;

    @CreatedBy
    private UUID createdBy;//Who add this article

    @LastModifiedBy
    private UUID updatedBy;//Who update this article

    @CreationTimestamp
    private Timestamp created_at;

    @UpdateTimestamp
    private  Timestamp updated_at;

    @ManyToOne
    private User user;

    public Article(String article, User user) {
        this.article = article;
        this.user = user;
    }
}
