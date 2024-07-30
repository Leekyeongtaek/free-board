package com.mrlee.free_board.post.repository.query.dto;

import com.mrlee.free_board.member.domain.Member;
import com.mrlee.free_board.post.domain.Post;
import com.mrlee.free_board.post.domain.PostLikesMapping;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostQueryDto {

    private Long postId;
    private Long memberId;
    private String title;
    private String content;
    private String author;
    private int views;
    private int likes;
    private boolean likeYn;
    private LocalDateTime createdDate;

    @QueryProjection
    public PostQueryDto(Post post, Member member, Boolean likeYn) {
        this.postId = post.getId();
        this.memberId = member.getId();
        this.author = member.getNickName();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.views = post.getViews();
        this.likes = post.getLikes();
        this.createdDate = post.getCreatedDate();
        this.likeYn = likeYn;
    }
}
