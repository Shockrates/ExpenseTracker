package com.sokratis.ExpenseTracker.DTO.Common;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.query.Meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    PaginationMeta meta;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.meta = new PaginationMeta(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumberOfElements(),
                page.isLast());
    }
}
