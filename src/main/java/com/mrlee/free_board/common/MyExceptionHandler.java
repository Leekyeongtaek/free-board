package com.mrlee.free_board.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class MyExceptionHandler {

    private final MyMailSender myMailSender;
    private final String internalErrorMessage = "서버에 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionReturnDto> internalExceptionHandle(Exception e) {
        log.info("[errorCause][errorMessage]={},{}", e.getCause(), e.getMessage());
        return new ResponseEntity<>(new ExceptionReturnDto(internalErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionReturnDto> illegalArgumentExceptionHandle(IllegalArgumentException e) {
        log.info("[errorCause][errorMessage]={},{}", e.getCause(), e.getMessage());
        return new ResponseEntity<>(new ExceptionReturnDto("요청 데이터의 형식 또는 값이 올바르지 않습니다."), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CriticalException.class)
    public ResponseEntity<ExceptionReturnDto> criticalExceptionHandle(CriticalException e) {
        log.info("[errorCause][errorMessage]={},{}", e.getCause(), e.getMessage());
        myMailSender.sendMail(MyMailMessage.CriticalMailMessage);
        return new ResponseEntity<>(new ExceptionReturnDto(internalErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Getter
    public static class ExceptionReturnDto {
        private final String message;

        public ExceptionReturnDto(String message) {
            this.message = message;
        }
    }
}
