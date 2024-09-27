package org.example.article;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ArticleForm {
    private String title;

    private String content;
}
