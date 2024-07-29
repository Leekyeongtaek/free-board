package com.mrlee.free_board.post.repository;

import com.mrlee.free_board.post.domain.PostLikesMapping;
import com.mrlee.free_board.post.domain.PostLikesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikesMappingRepository extends JpaRepository<PostLikesMapping, PostLikesId> {
}
