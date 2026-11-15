package com.dalia.ProjetoDalia.Model.DTOS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkRequestDTO {
    private String userId;
    private String articleId;
}