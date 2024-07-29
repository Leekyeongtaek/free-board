package com.mrlee.free_board.common;

import lombok.Getter;

@Getter
public class CriticalException extends RuntimeException {

    private final String message;

    public CriticalException(String message) {
        this.message = message;
    }
}
