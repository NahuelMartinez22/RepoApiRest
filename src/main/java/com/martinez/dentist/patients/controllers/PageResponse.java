package com.martinez.dentist.patients.controllers;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import java.util.List;

@Data
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> data;
    private int page;
    private int limit;
    private long total;
    private int totalPages;

    public static <T> PageResponse<T> of(Page<T> page) {
        PageResponse<T> response = new PageResponse<>();
        response.setData(page.getContent());
        response.setPage(page.getNumber());
        response.setLimit(page.getSize());
        response.setTotal(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return response;
    }
}