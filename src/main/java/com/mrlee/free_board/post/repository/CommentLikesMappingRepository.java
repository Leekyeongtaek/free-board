package com.mrlee.free_board.post.repository;

import com.mrlee.free_board.post.domain.CommentLikesId;
import com.mrlee.free_board.post.domain.CommentLikesMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikesMappingRepository extends JpaRepository<CommentLikesMapping, CommentLikesId> {
}
