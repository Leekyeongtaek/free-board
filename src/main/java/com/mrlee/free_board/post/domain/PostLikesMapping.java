package com.mrlee.free_board.post.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post_likes_mapping")
@Entity
public class PostLikesMapping {

    @EmbeddedId
    private PostLikesId id;
    private LocalDateTime createdDate;

    public PostLikesMapping(PostLikesId id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }
}
