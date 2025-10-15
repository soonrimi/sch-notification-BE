package com.schnofiticationbe.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common (공통)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에 오류가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다."), // NoResourceFoundException 등을 위해 사용

    // Auth & Admin (인증 및 관리자 관련)
    ALREADY_EXISTS_USER(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    INVALID_REGISTER_PASSWORD(HttpStatus.BAD_REQUEST, "가입용 비밀번호가 일치하지 않습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다."),
    INVALID_SECURITY_PASSWORD(HttpStatus.UNAUTHORIZED, "보안 비밀번호가 일치하지 않습니다."),
    INVALID_TEMP_PASSWORD(HttpStatus.UNAUTHORIZED, "임시 비밀번호가 일치하지 않습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // User (사용자 관련)
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),

    // Resource Not Found (리소스를 찾을 수 없음)
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 공지사항을 찾을 수 없습니다."),
    KAKAO_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카카오톡 채팅방 정보를 찾을 수 없습니다."),
    SUBSCRIBE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 구독 정보를 찾을 수 없습니다."),
    DEPARTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 학과를 찾을 수 없습니다."),

    // Invalid Parameter (잘못된 파라미터)
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리입니다."),
    GRADE_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 학년입니다.");

    private final HttpStatus status;
    private final String message;
}