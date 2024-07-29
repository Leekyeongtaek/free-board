package com.mrlee.free_board.post.repository;

import com.mrlee.free_board.post.domain.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
}
