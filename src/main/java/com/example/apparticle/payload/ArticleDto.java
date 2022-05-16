package com.example.apparticle.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ArticleDto {
    @NotNull
    private String article;

    @NotNull
    private UUID userId;
}
