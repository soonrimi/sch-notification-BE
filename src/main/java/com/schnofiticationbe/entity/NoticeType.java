package com.schnofiticationbe.entity;

public enum NoticeType {
    CRAWL("크롤링"),
    INTERNAL("내부공지"),
    BOARD("건의사항");

    private final String description;

    NoticeType(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
