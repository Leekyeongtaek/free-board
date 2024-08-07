package com.mrlee.free_board.post.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikesId {

    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "member_Id")
    private Long memberId;

    public CommentLikesId(Long commentId, Long memberId) {
        this.commentId = commentId;
        this.memberId = memberId;
    }
}
