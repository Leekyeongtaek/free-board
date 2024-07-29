package com.mrlee.free_board.post.repository;

import com.mrlee.free_board.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndMemberId(@Param("postId") Long postId, @Param("memberId") Long memberId);

    @Query("SELECT p FROM Post p JOIN FETCH p.member WHERE p.id = :postId")
    Optional<Post> findByIdAndMemberJoinFetch(@Param("postId") Long postId);
}
