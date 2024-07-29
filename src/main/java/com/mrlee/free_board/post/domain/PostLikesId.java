package com.mrlee.free_board.post.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikesId implements Serializable {

    @EqualsAndHashCode.Include
    @Column(name = "post_id")
    private Long postId;

    @EqualsAndHashCode.Include
    @Column(name = "member_Id")
    private Long memberId;

    public PostLikesId(Long postId, Long memberId) {
        this.postId = postId;
        this.memberId = memberId;
    }
}
