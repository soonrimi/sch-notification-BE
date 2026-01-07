package com.schnofiticationbe.Utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtils {

    private static final String DEFAULT_SORT_FIELD = "createdAt";

    public static Pageable toLatestOrder(Pageable pageable) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, DEFAULT_SORT_FIELD)
        );
    }

    public static Pageable toDescOrder(Pageable pageable, String field) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, field)
        );
    }
}