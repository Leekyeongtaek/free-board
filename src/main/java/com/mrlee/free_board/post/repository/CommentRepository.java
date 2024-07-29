package com.mrlee.free_board.post.repository;

import com.mrlee.free_board.post.domain.Comment;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndMemberId(@Param("commentId") Long commentId, @Param("memberId") Long memberId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.post WHERE c.id = :commentId")
    Optional<Comment> findByIdAndPostJoinFetch(@Param("commentId") Long commentId);
}
