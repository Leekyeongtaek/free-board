package com.mrlee.free_board.post.repository.query.dto;

import com.mrlee.free_board.member.domain.Member;
import com.mrlee.free_board.post.domain.Comment;
import com.mrlee.free_board.post.domain.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChildCommentQueryDto {

    private Long commentId;
    private Long parentCommentId;
    private Long memberId;
    private String author;
    private String content;
    private int likes;
    private LocalDateTime createdDate;
    private boolean likesYn;
    private boolean authorYn;

    @QueryProjection
    public ChildCommentQueryDto(Long commentId, Long parentCommentId, Long memberId, String author, int likes, LocalDateTime createdDate,
                                String content, boolean hiddenYn, boolean likesYn, Long postAuthorMemberId) {
        this.commentId = commentId;
        this.parentCommentId = parentCommentId;
        this.memberId = memberId;
        this.author = author;
        this.likes = likes;
        this.createdDate = createdDate;
        this.content = hiddenYn ? "삭제된 댓글입니다." : content;
        this.likesYn = likesYn;
        this.authorYn = memberId.equals(postAuthorMemberId);
    }
}
