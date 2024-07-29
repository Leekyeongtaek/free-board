package com.mrlee.free_board.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MyMailMessage {

    CriticalMailMessage("[치명적 오류 발생 알림] 자유 게시판 관리자 확인 요망", "서버 로그를 확인 해주시기 바랍니다."),
    LowQualityMailMessage("[매우 늦은 쿼리 발생 알림] API 응답 속도가 매우 느림 관리자 확인 요망", "서버 로그를 확인 해주시기 바랍니다.");

    MyMailMessage(String subject, String text) {
        this.subject = subject;
        this.text = text;
    }

    private String subject;
    private String text;
}
