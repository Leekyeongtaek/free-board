package com.mrlee.free_board.post.repository.query.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentQueryDtoList {

    private Long commentId;
    private Long memberId;
    private String author;
    private String content;
    private int likes;
    private LocalDateTime createdDate;
    private boolean hasChild;
    private long childCommentCount;
    private boolean authorYn;
    private boolean likesYn;

    @QueryProjection
    public CommentQueryDtoList(Long commentId, String content, int likes, LocalDateTime createdDate, boolean hiddenYn,
                               Long memberId, String author, Boolean likesYn, Long childCommentCount, Long postAuthorMemberId) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.author = author;
        this.content = hiddenYn ? "삭제된 댓글입니다." : content;
        this.likes = likes;
        this.createdDate = createdDate;
        this.childCommentCount = childCommentCount;
        this.hasChild = childCommentCount > 0;
        this.likesYn = likesYn;
        this.authorYn = memberId.equals(postAuthorMemberId);
    }

}
