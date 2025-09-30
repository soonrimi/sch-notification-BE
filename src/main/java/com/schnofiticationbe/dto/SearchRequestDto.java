package com.schnofiticationbe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SearchRequestDto {
    private List<Long> ids;
    private String keyword;
}