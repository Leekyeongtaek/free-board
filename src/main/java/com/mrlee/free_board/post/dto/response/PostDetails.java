package com.mrlee.free_board.post.dto.response;

import com.mrlee.free_board.post.domain.PostType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDetails {

    private Long postId;
    private PostType type;
    private String title;
    private String content;
    private int views;
    private int likes;
    private String author;
    private LocalDateTime createdDate;
    private List<CommentDetails> commentDetailsList;
}
