package com.mrlee.free_board.member.repository;

import com.mrlee.free_board.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
