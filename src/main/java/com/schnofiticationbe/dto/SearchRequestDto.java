package com.schnofiticationbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(requiredProperties = {"ids", "keyword"})
public class SearchRequestDto {
    private List<Long> ids;
    private String keyword;
}