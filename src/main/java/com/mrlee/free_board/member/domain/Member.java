package com.mrlee.free_board.member.domain;

import com.mrlee.free_board.common.AuditingTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@Entity
public class Member extends AuditingTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(unique = true, nullable = false, length = 50)
    private String userId;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(unique = true,nullable = false, length = 30)
    private String nickName;

    @Builder
    public Member(String userId, String password, String nickName) {
        this.userId = userId;
        this.password = password;
        this.nickName = nickName;
    }
}
