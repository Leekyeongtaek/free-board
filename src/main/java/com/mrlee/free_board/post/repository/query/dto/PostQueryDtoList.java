package com.mrlee.free_board.post.repository.query.dto;

import com.mrlee.free_board.member.domain.Member;
import com.mrlee.free_board.post.domain.Post;
import com.mrlee.free_board.post.domain.PostType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostQueryDtoList {

    private Long postId;
    private String title;
    private String content;
    private int views;
    private int likes;
    private int commentCount;
    private PostType type;
    private String author;
    private LocalDateTime createdDate;

    public PostQueryDtoList(Long postId, String title, String content, int views, int likes,
                            PostType type, String author, LocalDateTime createdDate, int commentCount) {
        this.postId = postId;
        this.title = title;
        this.content = reduceContent(content);
        this.views = views;
        this.likes = likes;
        this.type = type;
        this.author = author;
        this.createdDate = createdDate;
        this.commentCount = commentCount;
    }

    private String reduceContent(String content) {
        if (content.length() < 101) {
            return content;
        } else {
            return content.substring(0, 100) + "...";
        }
    }
}
