package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.TargetYear;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeptYearBundle {
    private Long departmentId;
    private TargetYear targetYear;

}
