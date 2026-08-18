package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Department 엔티티의 요약 정보를 담는 공통 DTO입니다.
 * Entity를 직접 API 응답에 노출하지 않기 위해 사용합니다.
 */
@Getter
@AllArgsConstructor
public class DepartmentSummary {
    private Long id;
    private String name;

    public static DepartmentSummary from(Department department) {
        return new DepartmentSummary(department.getId(), department.getName());
    }
}
