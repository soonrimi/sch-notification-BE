package com.schnofiticationbe.entity;

public enum TargetYear {
    ALL_YEARS("전체"),
    FIRST_YEAR("1학년"),
    SECOND_YEAR("2학년"),
    THIRD_YEAR("3학년"),
    FOURTH_YEAR("4학년"),
    FIFTH_YEAR("5학년");

    private final String description;

    TargetYear(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}