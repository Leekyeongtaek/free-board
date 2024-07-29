package com.mrlee.free_board.post.dto.request;

import com.mrlee.free_board.post.domain.PostType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PostSearchForm {

    private String title;
    private PostType type;

    public PostSearchForm(String title, PostType type) {
        this.title = title;
        this.type = type;
    }
}
