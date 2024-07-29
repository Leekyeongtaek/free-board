package com.mrlee.free_board.post.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikesId {

    @EqualsAndHashCode.Include
    @Column(name = "comment_id")
    private Long commentId;

    @EqualsAndHashCode.Include
    @Column(name = "member_Id")
    private Long memberId;

    public CommentLikesId(Long commentId, Long memberId) {
        this.commentId = commentId;
        this.memberId = memberId;
    }
}
