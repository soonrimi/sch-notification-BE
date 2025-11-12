package com.schnofiticationbe.Utils; // 패키지명 확인하세요

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Converter
public class JsonStringListConverter implements AttributeConverter<List<String>, String> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null) return null;
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON writing error", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            // JSON 배열로 파싱
            return mapper.readValue(dbData, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            try {
                // 단일 JSON 문자열로 파싱
                String singleValue = mapper.readValue(dbData, String.class);
                return new ArrayList<>(Collections.singletonList(singleValue));
            } catch (Exception e2) {
                System.err.println("WARNING: JSON parsing failed completely for data: [" + dbData + "]. Treating as raw string.");
                return new ArrayList<>(Collections.singletonList(dbData));
            }
        }
    }
}